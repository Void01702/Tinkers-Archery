package tonite.tinkersarchery.client.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import slimeknights.mantle.util.ItemLayerPixels;
import slimeknights.mantle.util.JsonHelper;
import slimeknights.mantle.util.ReversedListBuilder;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.client.model.tools.MaterialModel;
import slimeknights.tconstruct.library.client.model.tools.ToolModel;
import slimeknights.tconstruct.library.client.modifiers.IBakedModifierModel;
import slimeknights.tconstruct.library.client.modifiers.ModifierModelManager;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

// I'm sorry TConstruct Team, but you made me copy the entirety of your code since you didn't make the ToolModel class very inheritable
public class BowModel implements IModelGeometry<BowModel> {
    /** Shared loader instance */
    public static final BowModel.Loader LOADER = new BowModel.Loader();

    /** List of tool parts in this model */
    private final List<BowModel.ToolPart> toolParts;
    /** If true, this is a large tool and uses double resolution textures in hand */
    private final boolean isLarge;
    /** Transform matrix to apply to child parts */
    private final Vector2f offset;
    /** Location to fetch modifier textures for small variant */
    private final List<ResourceLocation> smallModifierRoots;
    /** Location to fetch modifier textures for large variant */
    private final List<ResourceLocation> largeModifierRoots;

    public BowModel(List<BowModel.ToolPart> toolParts, boolean isLarge, Vector2f offset, List<ResourceLocation> smallModifierRoots, List<ResourceLocation> largeModifierRoots) {
        this.toolParts = toolParts;
        this.isLarge = isLarge;
        this.offset = offset;
        this.smallModifierRoots = smallModifierRoots;
        this.largeModifierRoots = largeModifierRoots;
    }

    /** Models for the relevant modifiers */
    private Map<Modifier,IBakedModifierModel> modifierModels = Collections.emptyMap();

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String,String>> missingTextureErrors) {
        Set<RenderMaterial> allTextures = Sets.newHashSet();
        if (toolParts.isEmpty()) {
            allTextures.add(owner.resolveTexture("tool"));
            if (owner.isTexturePresent("broken")) {
                allTextures.add(owner.resolveTexture("broken"));
            }
            if (isLarge) {
                allTextures.add(owner.resolveTexture("tool_large"));
                if (owner.isTexturePresent("broken_large")) {
                    allTextures.add(owner.resolveTexture("broken_large"));
                }
            }
        } else {
            for (ToolPart part : toolParts) {
                // if material variants, fetch textures from the material model
                if (part.hasMaterials()) {
                    MaterialModel.getMaterialTextures(allTextures, owner, part.getName(false, false), null);
                    if (part.hasBroken()) {
                        MaterialModel.getMaterialTextures(allTextures, owner, part.getName(true, false), null);
                    }
                    if (isLarge) {
                        MaterialModel.getMaterialTextures(allTextures, owner, part.getName(false, true), null);
                        if (part.hasBroken()) {
                            MaterialModel.getMaterialTextures(allTextures, owner, part.getName(true, true), null);
                        }
                    }
                } else {
                    // static texture
                    allTextures.add(owner.resolveTexture(part.getName(false, false)));
                    if (part.hasBroken()) {
                        allTextures.add(owner.resolveTexture(part.getName(true, false)));
                    }
                    if (isLarge) {
                        allTextures.add(owner.resolveTexture(part.getName(false, true)));
                        if (part.hasBroken()) {
                            allTextures.add(owner.resolveTexture(part.getName(true, true)));
                        }
                    }
                }
            }
        }
        // load modifier models
        modifierModels = ModifierModelManager.getModelsForTool(smallModifierRoots, isLarge ? largeModifierRoots : Collections.emptyList(), allTextures);

        return allTextures;
    }

    /**
     * adds quads for relevant modifiers
     * @param spriteGetter    Sprite getter instance
     * @param modifierModels  Map of modifier models
     * @param tool            Tool instance
     * @param quadConsumer    Consumer for finished quads
     * @param transforms      Transforms to apply
     * @param isLarge         If true, the quads are for a large tool
     */
    private static void addModifierQuads(Function<RenderMaterial, TextureAtlasSprite> spriteGetter, Map<Modifier,IBakedModifierModel> modifierModels, IModifierToolStack tool, Consumer<ImmutableList<BakedQuad>> quadConsumer, ItemLayerPixels pixels, TransformationMatrix transforms, boolean isLarge) {
        if (!modifierModels.isEmpty()) {
            // keep a running tint index so models know where they should start, currently starts at 0 as the main model does not use tint indexes
            int modelIndex = 0;
            // reversed order to ensure the pixels is updated correctly
            List<ModifierEntry> modifiers = tool.getUpgrades().getModifiers();
            for (int i = modifiers.size() - 1; i >= 0; i--) {
                ModifierEntry entry = modifiers.get(i);
                IBakedModifierModel model = modifierModels.get(entry.getModifier());
                if (model != null) {
                    quadConsumer.accept(model.getQuads(tool, entry, spriteGetter, transforms, isLarge, modelIndex, pixels));
                    modelIndex += model.getTintIndexes();
                }
            }
        }
    }

    /**
     * Same as {@link #bake(IModelConfiguration, ModelBakery, Function, IModelTransform, ItemOverrideList, ResourceLocation)}, but uses fewer arguments and does not require an instance
     * @param owner           Model configuration
     * @param spriteGetter    Sprite getter function
     * @param largeTransforms Transform to apply to the large parts. If null, only generates small parts
     * @param parts           List of tool parts in this tool
     * @param modifierModels  Map of modifier models for this tool
     * @param materials       Materials to use for the parts
     * @param tool            Tool instance for modifier parsing
     * @param overrides       Override instance to use, will either be empty or {@link ToolModel.MaterialOverrideHandler}
     * @return  Baked model
     */
    private static IBakedModel bakeInternal(IModelConfiguration owner, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, @Nullable TransformationMatrix largeTransforms,
                                            List<BowModel.ToolPart> parts, Map<Modifier,IBakedModifierModel> modifierModels,
                                            List<MaterialId> materials, @Nullable IModifierToolStack tool, ItemOverrideList overrides) {
        boolean isBroken = tool != null && tool.isBroken();
        TextureAtlasSprite particle = null;
        // we create both builders always, though large may be unused
        ReversedListBuilder<BakedQuad> smallBuilder = new ReversedListBuilder<>();
        ReversedListBuilder<BakedQuad> largeBuilder = new ReversedListBuilder<>();
        ItemLayerPixels smallPixels = new ItemLayerPixels();
        ItemLayerPixels largePixels = new ItemLayerPixels();
        Consumer<ImmutableList<BakedQuad>> smallConsumer;
        Consumer<ImmutableList<BakedQuad>> largeConsumer = largeBuilder::addAll;
        // for large tools, we don't need non-south small quads
        if (largeTransforms != null) {
            smallConsumer = quads ->
                smallBuilder.addAll(quads.stream().filter(quad -> quad.getDirection() == Direction.SOUTH).collect(Collectors.toList()));
        } else {
            smallConsumer = smallBuilder::addAll;
        }

        // add quads for all modifiers first, for the sake of the item layer pixels
        if (tool != null && !modifierModels.isEmpty()) {
            addModifierQuads(spriteGetter, modifierModels, tool, smallConsumer, smallPixels, TransformationMatrix.identity(), false);
            if (largeTransforms != null) {
                addModifierQuads(spriteGetter, modifierModels, tool, largeConsumer, largePixels, largeTransforms, true);
            }
        }

        // add quads for all parts
        if (parts.isEmpty()) {
            particle = spriteGetter.apply(owner.resolveTexture(isBroken && owner.isTexturePresent("broken") ? "broken" : "tool"));
            smallConsumer.accept(ItemLayerModel.getQuadsForSprite(-1, particle, TransformationMatrix.identity()));
            if (largeTransforms != null) {
                largeConsumer.accept(ItemLayerModel.getQuadsForSprite(-1, spriteGetter.apply(owner.resolveTexture(isBroken && owner.isTexturePresent("broken_large") ? "broken_large" : "tool_large")), largeTransforms));
            }
        } else {
            for (int i = parts.size() - 1; i >= 0; i--) {
                ToolPart part = parts.get(i);
                if (part.hasMaterials()) {
                    // part with materials
                    int index = part.getIndex();
                    MaterialId material = null;
                    if (index < materials.size()) {
                        material = materials.get(index);
                    }
                    // add needed quads
                    particle = MaterialModel.getPartQuads(smallConsumer, owner, spriteGetter, TransformationMatrix.identity(), part.getName(isBroken, false), -1, material, smallPixels);
                    if (largeTransforms != null) {
                        MaterialModel.getPartQuads(largeConsumer, owner, spriteGetter, largeTransforms, part.getName(isBroken, true), -1, material, largePixels);
                    }
                } else {
                    // part without materials
                    particle = spriteGetter.apply(owner.resolveTexture(part.getName(isBroken, false)));
                    smallConsumer.accept(ItemLayerModel.getQuadsForSprite(-1, particle, TransformationMatrix.identity()));
                    if (largeTransforms != null) {
                        largeConsumer.accept(ItemLayerModel.getQuadsForSprite(-1, spriteGetter.apply(owner.resolveTexture(part.getName(isBroken, true))), largeTransforms));
                    }
                }
            }
        }

        // bake model - while the transform may not be identity, it never has rotation so its safe to say untransformed
        ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap = Maps.immutableEnumMap(PerspectiveMapWrapper.getTransforms(owner.getCombinedTransform()));

        // large models use a custom model here
        if (largeTransforms != null) {
            return new BowModel.BakedLargeToolModel(largeBuilder.build(), smallBuilder.build(), particle, transformMap, overrides, owner.isSideLit());
        }
        // for small, we leave out the large quads, so the baked item model logic is sufficient
        return new BakedItemModel(smallBuilder.build(), particle, transformMap, overrides, true, owner.isSideLit());
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial,TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        // load in modifiers
        // Map<Modifier,IBakedModifierModel> modifierModels = ModifierModelManager.getModelsForTool(smallModifierRoots, isLarge ? largeModifierRoots : Collections.emptyList());

        TransformationMatrix largeTransforms = isLarge ? new TransformationMatrix(new Vector3f((offset.x - 8) / 32, (-offset.y - 8) / 32, 0), null, new Vector3f(2, 2, 1), null) : null;
        overrides = new BowModel.MaterialOverrideHandler(owner, toolParts, largeTransforms, modifierModels, overrides);
        // bake the original with no modifiers or materials
        return bakeInternal(owner, spriteGetter, largeTransforms, toolParts, modifierModels, Collections.emptyList(), null, overrides);
    }

    /** Data class for a single tool part */
    private static class ToolPart {
        private final String name;

        private final int index;
        @Nullable
        private final String broken;

        public ToolPart(String name, int index, String broken){
            this.name = name;
            this.index = index;
            this.broken = broken;
        }

        public int getIndex() {return index;}

        /** If true, this part has material variants */
        public boolean hasMaterials() {
            return index >= 0;
        }

        /** If true, this part has a broken texture */
        public boolean hasBroken() {
            return broken != null;
        }

        /**
         * Gets the name for this part
         * @param isBroken  If true, this part is broken
         * @param isLarge   If true, rendering a large tool
         * @return  Texture name for this part
         */
        public String getName(boolean isBroken, boolean isLarge) {
            String name = this.name;
            if (isBroken && broken != null) {
                name = broken;
            }
            if (isLarge) {
                name = "large_" + name;
            }
            return name;
        }

        /** Reads a part from JSON */
        public static BowModel.ToolPart read(JsonObject json) {
            String name = JSONUtils.getAsString(json, "name");
            int index = JSONUtils.getAsInt(json, "index");
            String broken = null;
            if (json.has("broken")) {
                broken = JSONUtils.getAsString(json, "broken");
            }
            return new BowModel.ToolPart(name, index, broken);
        }
    }

    /**
     * Dynamic override handler to swap in the material texture
     */
    public static final class MaterialOverrideHandler extends ItemOverrideList {
        // contains all the baked models since they'll never change, cleared automatically as the baked model is discarded
        private final Cache<BowToolCacheKey, IBakedModel> cache = CacheBuilder
                .newBuilder()
                // ensure we can display every single tool that shows in JEI, plus a couple extra
                .maximumSize(MaterialRenderInfoLoader.INSTANCE.getAllRenderInfos().size() * 3 / 2)
                .build();

        // parameters needed for rebaking
        private final IModelConfiguration owner;
        private final List<BowModel.ToolPart> toolParts;
        @Nullable
        private final TransformationMatrix largeTransforms;
        private final Map<Modifier,IBakedModifierModel> modifierModels;
        private final ItemOverrideList parent;

        private MaterialOverrideHandler(IModelConfiguration owner, List<BowModel.ToolPart> toolParts, @Nullable TransformationMatrix largeTransforms, Map<Modifier,IBakedModifierModel> modifierModels, ItemOverrideList parent) {
            this.owner = owner;
            this.toolParts = toolParts;
            this.largeTransforms = largeTransforms;
            this.modifierModels = modifierModels;
            this.parent = parent;
        }

        /**
         * Gets the modifier model for this instance
         * @param modifier  Modifier
         * @return  Model for the modifier
         */
        @Nullable
        public IBakedModifierModel getModifierModel(Modifier modifier) {
            return modifierModels.get(modifier);
        }

        /**
         * Bakes a copy of this model using the given material
         * @param materials  New materials for the model
         * @return  Baked model
         */
        private IBakedModel bakeDynamic(List<MaterialId> materials, IModifierToolStack tool) {
            // bake internal does not require an instance to bake, we can pass in whatever material we want
            // use empty override list as the sub model never calls overrides, and already has a material
            return bakeInternal(owner, ModelLoader.defaultTextureGetter(), largeTransforms, toolParts, modifierModels, materials, tool, parent);
        }

        @Override
        public IBakedModel resolve(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {

            // use material IDs for the sake of internal rendering materials
            List<MaterialId> materialIds = MaterialIdNBT.from(stack).getMaterials();
            ToolStack tool = ToolStack.from(stack);
            boolean broken = ToolDamageUtil.isBroken(stack);

            // if nothing unique, render original
            if (!broken && materialIds.isEmpty() && tool.getUpgrades().isEmpty()) {
                return originalModel;
            }

            // build the cache key for the modifiers, based on what the modifier requests
            // for many, it is just the modifer entry, but they can have more complex keys if needed
            ImmutableList.Builder<Object> builder = ImmutableList.builder();
            for (ModifierEntry entry : tool.getUpgrades().getModifiers()) {
                IBakedModifierModel model = modifierModels.get(entry.getModifier());
                if (model != null) {
                    Object cacheKey = model.getCacheKey(tool, entry);
                    if (cacheKey != null) {
                        builder.add(cacheKey);
                    }
                }
            }

            // render special model
            try {
                IBakedModel nested = parent.resolve(cache.get(new BowToolCacheKey(materialIds, builder.build(), broken), () -> bakeDynamic(materialIds, tool)), stack, world, entity);

                return nested.getOverrides().resolve(nested, stack, world, entity);
            } catch (ExecutionException e) {
                //log.error(e);
                return originalModel;
            } catch (RuntimeException e) {
                return originalModel;
            }
        }
    }

    /** Baked model for large tools, has separate quads in GUIs */
    private static class BakedLargeToolModel implements IBakedModel {
        private final ImmutableList<BakedQuad> largeQuads;
        private final TextureAtlasSprite particleTexture;
        private final ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transforms;
        private final ItemOverrideList overrides;
        private final boolean isSideLit;
        private final IBakedModel guiModel;

        private BakedLargeToolModel(ImmutableList<BakedQuad> largeQuads, ImmutableList<BakedQuad> smallQuads, TextureAtlasSprite particle, ImmutableMap<ItemCameraTransforms.TransformType,TransformationMatrix> transforms, ItemOverrideList overrides, boolean isSideLit) {
            this.largeQuads = largeQuads;
            this.particleTexture = particle;
            this.transforms = transforms;
            this.overrides = overrides;
            this.isSideLit = isSideLit;
            this.guiModel = new BowModel.BakedLargeToolGui(this, smallQuads);
        }

        public TextureAtlasSprite getParticleIcon() {
            return particleTexture;
        }

        public ItemOverrideList getOverrides() {
            return overrides;
        }

        public boolean getIsSideLit(){
            return isSideLit;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
            if (side == null) {
                return largeQuads;
            }
            return ImmutableList.of();
        }

        @Override
        public IBakedModel handlePerspective(ItemCameraTransforms.TransformType type, MatrixStack mat) {
            if (type == ItemCameraTransforms.TransformType.GUI) {
                return this.guiModel.handlePerspective(type, mat);
            }
            return PerspectiveMapWrapper.handlePerspective(this, transforms, type, mat);
        }

        /* Misc properties */

        @Override
        public boolean usesBlockLight() {
            return isSideLit;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }
    }

    /** Baked model for large tools in the GUI, small quads */
    private static class BakedLargeToolGui extends BakedModelWrapper<BowModel.BakedLargeToolModel> {
        private final List<BakedQuad> guiQuads;
        public BakedLargeToolGui(BowModel.BakedLargeToolModel model, List<BakedQuad> guiQuads) {
            super(model);
            this.guiQuads = guiQuads;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
            // these quads are only shown in handle perspective for GUI
            if (side == null) {
                return guiQuads;
            }
            return ImmutableList.of();
        }

        @Override
        public boolean doesHandlePerspectives() {
            return true;
        }

        @Override
        public IBakedModel handlePerspective(ItemCameraTransforms.TransformType transform, MatrixStack mat) {
            return PerspectiveMapWrapper.handlePerspective(this, originalModel.transforms, transform, mat);
        }
    }

    /**
     * Model loader logic, use {@link #LOADER} to access instance
     */
    private static class Loader implements IModelLoader<BowModel> {
        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}

        @Override
        public BowModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
            List<BowModel.ToolPart> parts = Collections.emptyList();
            if (modelContents.has("parts")) {
                parts = JsonHelper.parseList(modelContents, "parts", BowModel.ToolPart::read);
            }
            boolean isLarge = JSONUtils.getAsBoolean(modelContents, "large", false);
            Vector2f offset = Vector2f.ZERO;
            if (modelContents.has("large_offset")) {
                offset = MaterialModel.arrayToObject(modelContents, "large_offset");
            }
            // modifier root fetching
            List<ResourceLocation> smallModifierRoots = Collections.emptyList();
            List<ResourceLocation> largeModifierRoots = Collections.emptyList();
            if (modelContents.has("modifier_roots")) {
                // large model requires an object
                if (isLarge) {
                    JsonObject modifierRoots = JSONUtils.getAsJsonObject(modelContents, "modifier_roots");
                    BiFunction<JsonElement,String,ResourceLocation> parser = (element, string) -> new ResourceLocation(JSONUtils.convertToString(element, string));
                    smallModifierRoots = JsonHelper.parseList(modifierRoots, "small", parser);
                    largeModifierRoots = JsonHelper.parseList(modifierRoots, "large", parser);
                } else {
                    // small requires an array
                    smallModifierRoots = JsonHelper.parseList(modelContents, "modifier_roots", (element, string) -> new ResourceLocation(JSONUtils.convertToString(element, string)));
                }
            }

            return new BowModel(parts, isLarge, offset, smallModifierRoots, largeModifierRoots);
        }
    }

    /** Simple data class to cache built tool modifiers, contains everything unique in the textures */
    private static class BowToolCacheKey {

        private final List<MaterialId> materials;
        private final List<Object> modifierData;
        private final boolean broken;

        public BowToolCacheKey(List<MaterialId> materials, List<Object> modifierData, boolean broken){
            this.materials = materials;
            this.modifierData = modifierData;
            this.broken = broken;
        }

        public List<MaterialId> getMaterials(){
            return materials;
        }

        public List<Object> getModifierData(){
            return modifierData;
        }

        public boolean isBroken(){
            return broken;
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (!(o instanceof BowToolCacheKey)) return false;
            BowToolCacheKey other = (BowToolCacheKey) o;

            if (!other.getMaterials().equals(getMaterials())) return false;

            if (!other.getModifierData().equals(getModifierData())) return false;

            if (!(other.isBroken() == isBroken())) return false;

            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((getMaterials() == null) ? 0 : getMaterials().hashCode());
            result = prime * result + ((getModifierData() == null) ? 0 : getModifierData().hashCode());
            result = prime * result + (isBroken()?1:0);
            return result;
        }
    }
}
