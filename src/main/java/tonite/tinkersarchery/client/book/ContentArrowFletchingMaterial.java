package tonite.tinkersarchery.client.book;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.ForgeI18n;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.screen.book.BookScreen;
import slimeknights.mantle.client.screen.book.element.BookElement;
import slimeknights.mantle.client.screen.book.element.ItemElement;
import slimeknights.mantle.client.screen.book.element.TextElement;
import slimeknights.mantle.recipe.RecipeHelper;
import slimeknights.tconstruct.library.book.content.ContentMaterial;
import slimeknights.tconstruct.library.book.elements.TinkerItemElement;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.recipe.RecipeTypes;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.ArrowHeadMaterialStats;
import tonite.tinkersarchery.stats.ArrowShaftMaterialStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContentArrowFletchingMaterial extends ContentMaterial {

    public static final String ID = "arrowfletchingmaterial";

    private transient IMaterial material;
    private transient List<ItemStack> repairStacks;

    public ContentArrowFletchingMaterial(IMaterial material) {
        super(material, true);
        this.material = material;
    }

    @Override
    public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
        IMaterial material = getMaterial();
        this.addTitle(list, getTitle().getString(), true, material.getColor().getValue());

        // the cool tools to the left/right
        this.addDisplayItems(list, rightSide ? BookScreen.PAGE_WIDTH - 18 : 0, material.getIdentifier());

        int col_margin = 22;
        int top = getTitleHeight();
        int left = rightSide ? 0 : col_margin;

        int y = top + 5;
        int x = left + 5;
        int w = BookScreen.PAGE_WIDTH / 2 - 5;

        // inspirational quote
        MaterialId id = material.getIdentifier();

        String flavorKey = String.format("material.%s.%s.flavor", id.getNamespace(), id.getPath());
        if (I18n.exists(flavorKey)) {
            // using forge instead of I18n.format as that prevents % from being interpreted as a format key
            String translated = ForgeI18n.getPattern(flavorKey);
            translated = '"' + translated + '"';
            TextData flavourData = new TextData(translated);
            flavourData.italic = true;
            list.add(new TextElement(x + w, y, w - 22, 60, flavourData));
        }

        // boring description text
        String descriptionKey = String.format("material.%s.%s.arrow", id.getNamespace(), id.getPath());
        if (I18n.exists(descriptionKey)) {
            // using forge instead of I18n.format as that prevents % from being interpreted as a format key
            String translated = ForgeI18n.getPattern(descriptionKey);
            TextData descriptionData = new TextData(translated);
            descriptionData.italic = false;
            list.add(new TextElement(x, y, w - 22, 60, descriptionData));
        }

        y += 65;

        // how weight affects it
        String weightKey = String.format("material.%s.%s.weight", id.getNamespace(), id.getPath());
        if (I18n.exists(weightKey)) {
            // using forge instead of I18n.format as that prevents % from being interpreted as a format key
            String translated = ForgeI18n.getPattern(weightKey);
            TextData weightData = new TextData(translated);
            weightData.italic = false;
            list.add(new TextElement(x, y, w - 22, 60, weightData));
        }
    }

    @Override
    protected void addDisplayItems(ArrayList<BookElement> list, int x, MaterialId materialId) {
        List<ItemElement> displayTools = Lists.newArrayList();

        // add display items
        displayTools.add(new TinkerItemElement(0, 0, 1f, getRepairStacks()));
        addPrimaryDisplayItems(displayTools, materialId);

        // fill in leftover space
        if (displayTools.size() < 9) {
            toolLoop:
            for (Item item : TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE.getValues()) {
                if (item instanceof IModifiable) {
                    IModifiable tool = ((IModifiable)item);
                    List<PartRequirement> requirements = tool.getToolDefinition().getData().getParts();
                    // start building the tool with the given material
                    List<IMaterial> materials = new ArrayList<>(requirements.size());
                    IMaterial material = MaterialRegistry.getMaterial(materialId);
                    boolean usedMaterial = false;
                    for (PartRequirement part : requirements) {
                        // if any stat type of the tool is not supported by this page, skip the whole tool
                        if (!supportsStatType(part.getStatType())) {
                            continue toolLoop;
                        }
                        // if the stat type is not supported by the material, substitute
                        if (hasStatType(materialId, part.getStatType())) {
                            materials.add(material);
                            usedMaterial = true;
                        } else {
                            materials.add(getFirstMaterialWithType(part.getStatType()));
                        }
                    }

                    // only add a stack if our material showed up
                    if (usedMaterial) {
                        ItemStack display = ToolBuildHandler.buildItemFromMaterials(tool, materials);
                        displayTools.add(new TinkerItemElement(display));
                        if (displayTools.size() == 9) {
                            break;
                        }
                    }
                }
            }
        }

        // built tools
        if (!displayTools.isEmpty()) {
            int y = getTitleHeight() - 5;
            for (ItemElement element : displayTools) {
                element.x = x;
                element.y = y;
                element.scale = 1f;
                y += ItemElement.ITEM_SIZE_HARDCODED;

                list.add(element);
            }
        }
    }

    private static boolean hasStatType(MaterialId materialId, MaterialStatsId statsId) {
        return MaterialRegistry.getInstance().getMaterialStats(materialId, statsId).isPresent();
    }

    private static IMaterial getFirstMaterialWithType(MaterialStatsId statsId) {
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            if (hasStatType(material.getIdentifier(), statsId)) {
                return material;
            }
        }
        return IMaterial.UNKNOWN;
    }

    protected boolean supportsStatType(MaterialStatsId statsId) {
        return statsId.equals(ArrowHeadMaterialStats.ID) || statsId.equals(ArrowShaftMaterialStats.ID)|| statsId.equals(ArrowFletchingMaterialStats.ID);
    }

    @Override
    protected List<ItemStack> getRepairStacks() {
        if (repairStacks == null) {
            World world = Minecraft.getInstance().level;
            if (world == null) {
                return Collections.emptyList();
            }
            // simply combine all items from all recipes
            repairStacks = RecipeHelper.getUIRecipes(world.getRecipeManager(), RecipeTypes.MATERIAL, MaterialRecipe.class, recipe -> recipe.getMaterial() == material)
                    .stream()
                    .flatMap(recipe -> recipe.getDisplayItems().stream())
                    .collect(Collectors.toList());
            // no repair items? use the repair kit
            if (repairStacks.isEmpty()) {
                TinkersArchery.LOGGER.debug("Material with id " + material.getIdentifier() + " has no representation items associated with it, using arrow fletching");
                // bypass the valid check, because we need to show something
                repairStacks = Collections.singletonList(TinkersArchery.arrow_fletching.get().withMaterialForDisplay(material.getIdentifier()));
            }
        }
        return repairStacks;
    }

}
