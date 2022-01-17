package tonite.tinkersarchery.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
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
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArcheryConfig;
import tonite.tinkersarchery.data.TinkersArcheryMaterialIds;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.entities.TinkersArrowEntityLegacy;
import tonite.tinkersarchery.library.IProjectileItem;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrowTool extends ModifiableItem implements IProjectileItem {

    public static final ToolType TOOL_TYPE = ToolType.get("arrow");

    public ArrowTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public boolean hasInfinity (ItemStack itemStack) {
        CompoundNBT tag = itemStack.getTag();

        return tag.contains("infinity") && tag.getBoolean("infinity");
    }

    @Override
    public int consumeAmmo(ItemStack ammo, int desiredAmount, LivingEntity player) {
        if (hasInfinity(ammo)) {
            return desiredAmount;
        }
        if (getDamage(ammo) < getMaxDamage(ammo)) {
            return damageItem(ammo, desiredAmount, player, a -> {});
        } else {
            return 0;
        }
    }

    @Override
    public int getAmmo(ItemStack ammo, int desiredAmount) {
        ToolStack toolStack = ToolStack.from(ammo);
        if (hasInfinity(ammo)) {
            return desiredAmount;
        }
        return Math.min(toolStack.getCurrentDurability(), desiredAmount);
    }

    @Override
    public ProjectileEntity createProjectile(ItemStack ammo, World world, LivingEntity player, ItemStack bow) {
        ToolStack tool = ToolStack.from(ammo);

        return new TinkersArrowEntityLegacy(world, player, tool.getStats());
    }

    @Override
    public void supplyInfoToProjectile(ProjectileEntity projectile, ItemStack ammo, World world, LivingEntity shooter, ItemStack bow) {
        TinkersArrowEntityLegacy newProjectile = ((TinkersArrowEntityLegacy)projectile);
        newProjectile.setTool(ammo);
        newProjectile.setBow(bow);

        ToolStack toolStack = ToolStack.from(ammo);

        for(ModifierEntry m: toolStack.getModifierList()) {
            if (m.getModifier() instanceof IProjectileModifier) {
                ((IProjectileModifier) m.getModifier()).onArrowShot(toolStack, m.getLevel(), projectile, newProjectile.getOriginalDirection(), projectile.getOwner());
            }
        }
    }

    @Override
    public List<ITextComponent> getStatInformation(IModifierToolStack tool, @Nullable PlayerEntity player, List<ITextComponent> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        Item item = tool.getItem();

        if (TinkerTags.Items.DURABILITY.contains(item)) {
            builder.addDurability();
        }
        if (TinkerTags.Items.MELEE.contains(item)) {
            builder.addWithAttribute(ToolStats.ATTACK_DAMAGE, Attributes.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        if (TinkerTags.Items.HARVEST.contains(item)) {
            if (TinkerTags.Items.HARVEST_PRIMARY.contains(tool.getItem())) {
                builder.add(ToolStats.HARVEST_LEVEL);
            }
            builder.add(ToolStats.MINING_SPEED);
        }
        if (TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE.contains(item)) {
            builder.add(BowAndArrowToolStats.ACCURACY);
            builder.add(BowAndArrowToolStats.WEIGHT);
        }

        builder.addAllFreeSlots();

        for (ModifierEntry entry : tool.getModifierList()) {
            entry.getModifier().addInformation(tool, entry.getLevel(), player, tooltips, key, tooltipFlag);
        }
        return tooltips;
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

        if (!TinkersArcheryConfig.COMMON.legacyArrows.get()) {
            return false;
        }

        List<PartRequirement> required = item.getToolDefinition().getData().getParts();
        List<IMaterial> materials = new ArrayList<>(required.size());
        for (int i = 0; i < required.size(); i++) {
            if (fixedMaterials.length > i && fixedMaterials[i] != null && required.get(i).canUseMaterial(fixedMaterials[i])) {
                materials.add(fixedMaterials[i]);
            } else if (required.get(i).canUseMaterial(material)) {
                materials.add(material);
            } else if (required.get(i).getStatType() == ArrowFletchingMaterialStats.ID) {
                if (material.getTier() <= 1 && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.feather))) {
                    materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.feather));
                } else if (material.getTier() <= 2) {
                    if (material.getIdentifier().getPath().contains("slime") && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.slime_leaf))) {
                        materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.slime_leaf));
                    } else if (required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.leaf))) {
                        materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.leaf));
                    }
                } else if (material.getTier() <= 3 && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.silky_cloth))) {
                    materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.silky_cloth));
                } else if (material.getTier() <= 4 && required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.blazing_string))) {
                    materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.blazing_string));
                } else if (required.get(i).canUseMaterial(MaterialRegistry.getMaterial(MaterialIds.phantom))) {
                    materials.add(MaterialRegistry.getMaterial(MaterialIds.phantom));
                } else if (required.get(i).canUseMaterial(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.feather))) {
                    materials.add(MaterialRegistry.getMaterial(TinkersArcheryMaterialIds.feather));
                }
            } else {
                return false;
            }
        }
        items.add(ToolBuildHandler.buildItemFromMaterials(item, materials));
        return true;
    }
}
