package tonite.tinkersarchery.library;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.TinkersArcheryMaterialIds;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.entities.TinkersArrowEntityLegacy;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.library.projectileinterfaces.ICriticalProjectile;
import tonite.tinkersarchery.library.projectileinterfaces.IStoreBowProjectile;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;
import tonite.tinkersarchery.stats.BowStringMaterialStats;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ShootableTool extends ModifiableItem {

    private static final List<MaterialId> RENDER_MATERIALS = Arrays.asList(
            new MaterialId(TConstruct.MOD_ID, "ui_render_handle"),
            new MaterialId(TConstruct.MOD_ID, "ui_render_head"),
            new MaterialId(TConstruct.MOD_ID, "ui_render_extra"),
            new MaterialId(TConstruct.MOD_ID, "ui_render_large"));

    private ItemStack toolForRendering;

    public ShootableTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public int getItemAmount(ItemStack itemStack, int desiredAmount, boolean isHand) {
        if (itemStack.getItem() instanceof IProjectileItem) {
            return ((IProjectileItem)itemStack.getItem()).getAmmo(itemStack, desiredAmount);
        }

        if (itemStack.getItem() instanceof ArrowItem) {
            return Math.min(itemStack.getCount(), desiredAmount);
        } else {
            return 0;
        }
    }

    private static int addItemToList(ShootableTool shootableTool, List<AmmoListEntry> list, ItemStack itemStack, int currentlyDesired, boolean isHand) {
        int heldItemCount = shootableTool.getItemAmount(itemStack, currentlyDesired, isHand);
        if (heldItemCount > 0) {
            list.add(new AmmoListEntry(itemStack, heldItemCount));
            return heldItemCount;
        }
        return 0;
    }

    public static ItemStack getFirstProjectile(PlayerEntity player, ItemStack bowItemStack) {
        if (!(bowItemStack.getItem() instanceof ShootableTool)) {
            return ItemStack.EMPTY;
        } else {
            ShootableTool shootableTool = ((ShootableTool) bowItemStack.getItem());

            ItemStack itemstack = player.getItemInHand(Hand.MAIN_HAND);
            if (itemstack != bowItemStack) {
                if (shootableTool.getItemAmount(itemstack, 1, true) > 0) {
                    return itemstack;
                }
            }
            itemstack = player.getItemInHand(Hand.OFF_HAND);
            if (itemstack != bowItemStack) {
                if (shootableTool.getItemAmount(itemstack, 1, true) > 0) {
                    return itemstack;
                }
            }

            for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
                itemstack= player.inventory.getItem(i);
                if (itemstack != bowItemStack) {
                    if (shootableTool.getItemAmount(itemstack, 1, false) > 0) {
                        return itemstack;
                    }
                }
            }

            if (player.abilities.instabuild || (ToolStack.from(bowItemStack).getModifierLevel(TinkersArchery.INFINITY_MODIFIER.get()) > 0)) {
                return new ItemStack(Items.ARROW);
            }

            return ItemStack.EMPTY;
        }
    }

    public static List<AmmoListEntry> getProjectile(PlayerEntity player, ItemStack bowItemStack, int desiredAmount) {
        if (!(bowItemStack.getItem() instanceof ShootableTool)) {
            return new ArrayList<>();
        } else {
            ShootableTool shootableTool = ((ShootableTool) bowItemStack.getItem());
            int currentlyDesired = desiredAmount;

            ArrayList<AmmoListEntry> result = new ArrayList<>();

            ItemStack itemstack = player.getItemInHand(Hand.MAIN_HAND);
            if (itemstack != bowItemStack) {
                currentlyDesired -= addItemToList(shootableTool, result, itemstack, currentlyDesired, true);
            }
            itemstack = player.getItemInHand(Hand.OFF_HAND);
            if (itemstack != bowItemStack) {
                currentlyDesired -= addItemToList(shootableTool, result, itemstack, currentlyDesired, true);
            }

            for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
                itemstack= player.inventory.getItem(i);
                if (itemstack != bowItemStack) {
                    currentlyDesired -= addItemToList(shootableTool, result, itemstack, currentlyDesired, false);
                }
            }

            if (currentlyDesired > 0 && (player.abilities.instabuild || (ToolStack.from(bowItemStack).getModifierLevel(TinkersArchery.INFINITY_MODIFIER.get()) > 0))) {
                result.add(new AmmoListEntry(new ItemStack(Items.ARROW), currentlyDesired));
            }

            return result;
        }
    }

    public int[] getArrowCounts(ItemStack bow, World world, LivingEntity shooter, float drawPortion) {
        List<ModifierEntry> modifierList = new ArrayList<>();

        if(!bow.hasTag()) {
            return new int[0];
        }

        ToolStack tool = ToolStack.from(bow);

        for (ModifierEntry m : tool.getModifierList()) {
            if (m.getModifier() instanceof IBowModifier){
                modifierList.add(m);
            }
        }

        int[] result = new int[modifierList.size()];

        for(int i = 0; i < result.length; i++) {
            ModifierEntry entry = modifierList.get(i);

            result[i] = ((IBowModifier)entry.getModifier()).getArrowCount(tool, entry.getLevel(), drawPortion, world, shooter);
        }

        return result;
    }

    public static int compileArrowCounts(int[] arrowCounts) {

        int result = 1;

        for (int arrowCount : arrowCounts) {
            result += arrowCount;
        }

        return result;

    }

    public void shootBow(ItemStack bow, World world, LivingEntity shooter, float drawPortion, List<AmmoListEntry> arrowItems, int[] arrowCounts){

        List<ModifierEntry> modifierList = new ArrayList<>();

        if(!bow.hasTag()) {
            return;
        }

        ToolStack tool = ToolStack.from(bow);
        float powerModifier = tool.getStats().getFloat(BowAndArrowToolStats.ELASTICITY);
        float accuracyModifier = tool.getStats().getFloat(BowAndArrowToolStats.ACCURACY);

        for (ModifierEntry m : tool.getModifierList()) {
            if (m.getModifier() instanceof IBowModifier){
                modifierList.add(m);
            }
        }

        float power = drawPortion * powerModifier;
        float accuracy = accuracyModifier;

        List<IBowModifier.ArrowData> arrows = new ArrayList<>();

        for (ModifierEntry entry : modifierList) {
            power = ((IBowModifier) entry.getModifier()).getPower(tool, entry.getLevel(), drawPortion, power, world, shooter);
            accuracy = ((IBowModifier) entry.getModifier()).getAccuracy(tool, entry.getLevel(), drawPortion, accuracyModifier, accuracy, world, shooter);
        }

        arrows.add(new IBowModifier.ArrowData(Quaternion.ONE, power, accuracy));

        Vector3f lookingDirection = new Vector3f( shooter.getViewVector(1.0F) );

        for(int i = 0; i < Math.min(modifierList.size(), arrowCounts.length); i++) {
            ModifierEntry entry = modifierList.get(i);
            lookingDirection = ((IBowModifier) entry.getModifier()).onReleaseBow(tool, entry.getLevel(), drawPortion, power, accuracy, arrows, arrowCounts[i], world, lookingDirection, shooter);
        }

        Vector3d motion = shooter.getDeltaMovement();
        motion = new Vector3d(motion.x(), shooter.isOnGround() ? 0.0f : motion.y(), motion.z());

        int ammoListIndex = 0;

        AmmoListEntry ammoListEntry = arrowItems.get(ammoListIndex);

        ItemStack arrowItem = ammoListEntry.itemStack;
        int arrowItemCount = ammoListEntry.amount;

        for (IBowModifier.ArrowData data: arrows) {

            Vector3f arrowDirection = new Vector3f(lookingDirection.x(), lookingDirection.y(), lookingDirection.z());
            arrowDirection.transform(data.direction);

            ProjectileEntity projectile = createArrow(bow, world, arrowDirection, drawPortion, shooter, arrowItem);

            float adjustedWeight = 2f - calculateWeight();
            projectile.shoot(arrowDirection.x(), arrowDirection.y(), arrowDirection.z(), power * adjustedWeight, 1.0F / accuracy);

            supplyInfoToArrow(projectile, bow, world, projectile.getDeltaMovement().add(motion), drawPortion, shooter, arrowItem);

            for (ModifierEntry entry : modifierList) {
                ((IBowModifier) entry.getModifier()).onArrowShot(tool, entry.getLevel(), projectile, drawPortion, power, world, shooter);
            }

            world.addFreshEntity(projectile);

            arrowItemCount--;

            if (arrowItemCount <= 0) {
                ammoListIndex++;

                if (ammoListIndex < arrowItems.size()) {
                    ammoListEntry = arrowItems.get(ammoListIndex);

                    arrowItem = ammoListEntry.itemStack;
                    arrowItemCount = ammoListEntry.amount;
                } else {
                    break;
                }
            }
        }
    }

    public ProjectileEntity createArrow(ItemStack bow, World world, Vector3f direction, float drawPortion, LivingEntity shooter, ItemStack arrowItem) {

        if (arrowItem.getItem() instanceof IProjectileItem) {
            return ((IProjectileItem)arrowItem.getItem()).createProjectile(arrowItem, world, shooter, bow);
        }

        ArrowItem arrowItemClass;

        if (arrowItem.getItem() instanceof ArrowItem) {
            arrowItemClass = (ArrowItem)arrowItem.getItem();
        } else {
            arrowItemClass = (ArrowItem)Items.ARROW;
        }

        AbstractArrowEntity arrow = arrowItemClass.createArrow(world, arrowItem, shooter);

        if (drawPortion == 1.0F) {
            arrow.setCritArrow(true);
        }

        if (shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.instabuild) {
            arrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
        }

        return arrow;
    }

    public void supplyInfoToArrow(ProjectileEntity projectile, ItemStack bow, World world, Vector3d direction, float drawPortion, LivingEntity shooter, ItemStack arrowItem) {

        if (projectile instanceof ICriticalProjectile && drawPortion == 1.0F) {
            ((ICriticalProjectile)projectile).setCritical(true);
        }

        if (arrowItem.getItem() instanceof IProjectileItem) {
            ((IProjectileItem)arrowItem.getItem()).supplyInfoToProjectile(projectile, arrowItem, world, shooter, bow);
        }

        if (projectile instanceof IStoreBowProjectile) {
            ((IStoreBowProjectile)projectile).setBow(bow);
        }

        if (projectile instanceof AbstractArrowEntity) {
            AbstractArrowEntity arrow = (AbstractArrowEntity)projectile;

            if (drawPortion == 1.0F) {
                arrow.setCritArrow(true);
            }

            if (shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.instabuild) {
                arrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
            }
        }

        if (projectile instanceof TinkersArrowEntityLegacy) {
            ((TinkersArrowEntityLegacy)projectile).changeDirection(direction);
        } else {
            projectile.setDeltaMovement(direction);
        }
    }

    public final void consumeAmmo(List<AmmoListEntry> ammo, PlayerEntity player) {
        if (!player.abilities.instabuild) {
            for (AmmoListEntry entry : ammo) {
                consumeAmmo(entry, player);
            }
        }
    }

    public void consumeAmmo(AmmoListEntry ammo, PlayerEntity player) {
        if (ammo.itemStack.getItem() instanceof IProjectileItem) {
            ((IProjectileItem)ammo.itemStack.getItem()).consumeAmmo(ammo.itemStack, ammo.amount, player);
            return;
        }

        ammo.itemStack.shrink(ammo.amount);
    }

    @Override
    public List<ITextComponent> getStatInformation(IModifierToolStack tool, @Nullable PlayerEntity player, List<ITextComponent> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        Item item = tool.getItem();

        if (TinkerTags.Items.DURABILITY.contains(item)) {
            builder.addDurability();
        }
        if (TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE.contains(item)) {
            builder.add(BowAndArrowToolStats.ELASTICITY);
            builder.add(BowAndArrowToolStats.DRAW_SPEED);
            builder.add(BowAndArrowToolStats.ACCURACY);
        }

        builder.addAllFreeSlots();

        for (ModifierEntry entry : tool.getModifierList()) {
            entry.getModifier().addInformation(tool, entry.getLevel(), player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }

    @Override
    public ItemStack getRenderTool() {
        if (toolForRendering == null) {
            // if no parts, just return the item directly with the display tag
            toolForRendering = new ItemStack(this);
            if (getToolDefinition().isMultipart()) {
                toolForRendering = new MaterialIdNBT(RENDER_MATERIALS).updateStack(toolForRendering);
            }
            toolForRendering.getOrCreateTag().putBoolean(TooltipUtil.KEY_DISPLAY, true);
        }
        return toolForRendering;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            ToolDefinition definition = getToolDefinition();
            boolean isMultipart = definition.isMultipart();
            if (!definition.isDataLoaded() || (isMultipart && !MaterialRegistry.isFullyLoaded())) {
                // not loaded? cannot properly build it
                items.add(new ItemStack(this));
            } else if (!isMultipart) {
                // no parts? just add this item
                items.add(ToolBuildHandler.buildItemFromMaterials(this, Collections.emptyList()));
            } else {
                // if a specific material is set, show just that
                String showOnlyId = Config.COMMON.showOnlyToolMaterial.get();
                boolean added = false;
                if (!showOnlyId.isEmpty()) {
                    MaterialId materialId = MaterialId.tryCreate(showOnlyId);
                    if (materialId != null) {
                        IMaterial material = MaterialRegistry.getMaterial(materialId);
                        if (material != IMaterial.UNKNOWN) {
                            if (addSubItem(this, items, material, new IMaterial[0])) {
                                added = true;
                            }
                        }
                    }
                }
                // if the material was not applicable or we do not have a filter set, search the rest
                if (!added) {
                    for (IMaterial material : MaterialRegistry.getInstance().getVisibleMaterials()) {
                        // if we added it and we want a single material, we are done
                        if (addSubItem(this, items, material, new IMaterial[0]) && !showOnlyId.isEmpty()) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean addSubItem(IModifiable item, List<ItemStack> items, IMaterial material, IMaterial[] fixedMaterials) {
        List<PartRequirement> required = item.getToolDefinition().getData().getParts();
        List<IMaterial> materials = new ArrayList<>(required.size());
        for (int i = 0; i < required.size(); i++) {
            if (fixedMaterials.length > i && fixedMaterials[i] != null && required.get(i).canUseMaterial(fixedMaterials[i])) {
                materials.add(fixedMaterials[i]);
            } else if (required.get(i).getStatType() == BowStringMaterialStats.ID && material.getIdentifier().equals(MaterialIds.wood) && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(MaterialIds.string))) {
                materials.add(MaterialRegistry.getMaterial(MaterialIds.string));
            } else if (required.get(i).canUseMaterial(material)) {
                materials.add(material);
            } else if (required.get(i).getStatType() == BowStringMaterialStats.ID) {
                if (material.getIdentifier().getPath().contains("slime") && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(MaterialIds.enderslimeVine))) {
                    materials.add(MaterialRegistry.getMaterial(MaterialIds.enderslimeVine));
                } else if (material.getTier() <= 1 && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(MaterialIds.wood))) {
                    materials.add(MaterialRegistry.getMaterial(MaterialIds.wood));
                } else if (material.getTier() <= 2 && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(MaterialIds.skyslimeVine))) {
                    materials.add(MaterialRegistry.getMaterial(MaterialIds.skyslimeVine));
                } else if (material.getTier() <= 3 && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.silky_cloth))) {
                    materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.silky_cloth));
                } else if (material.getTier() <= 4 && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.blazing_string))) {
                    materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.blazing_string));
                } else if (required.get(i).canUseMaterial(MaterialRegistry.getMaterial(MaterialIds.enderslimeVine))) {
                    materials.add(MaterialRegistry.getMaterial(MaterialIds.enderslimeVine));
                } else if (required.get(i).canUseMaterial(MaterialRegistry.getMaterial(MaterialIds.string))) {
                    materials.add(MaterialRegistry.getMaterial(MaterialIds.string));
                }
            } else {
                return false;
            }
        }
        items.add(ToolBuildHandler.buildItemFromMaterials(item, materials));
        return true;
    }

    public static class AmmoListEntry {
        public ItemStack itemStack;
        public int amount;

        public AmmoListEntry (ItemStack itemStack, int amount) {
            this.itemStack = itemStack;
            this.amount = amount;
        }
    }
}
