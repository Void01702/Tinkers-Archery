package tonite.tinkersarchery.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.capability.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.item.ITinkerStationDisplay;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArcheryConfig;
import tonite.tinkersarchery.data.TinkersArcheryMaterialIds;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.library.ITinkersConsumableItem;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nullable;
import java.util.*;

public class TinkersArrowItem extends ArrowItem implements ITinkersConsumableItem, IModifiable, IModifiableDisplay, ITinkerStationDisplay {

    private static final List<MaterialId> RENDER_MATERIALS = Arrays.asList(
            new MaterialId(TConstruct.MOD_ID, "ui_render_head"),
            new MaterialId(TConstruct.MOD_ID, "ui_render_handle"),
            new MaterialId(TConstruct.MOD_ID, "ui_render_extra"));

    public final ToolDefinition definition;
    private ItemStack toolForRendering;

    public TinkersArrowItem(Properties properties, ToolDefinition definition) {
        super(properties);

        this.definition = definition;
    }

    public AbstractArrowEntity createArrow(World world, ItemStack arrow, LivingEntity shooter) {
        TinkersArrowEntity arrowentity = new TinkersArrowEntity(world, shooter, this, definition, ITinkersConsumableItem.getMaterials(arrow));
        return arrowentity;
    }

    public ToolDefinition getToolDefinition() {
        return definition;
    }

    @Override
    public ItemStack getRenderTool() {
        if (toolForRendering == null) {
            toolForRendering = ITinkersConsumableItem.of(this, RENDER_MATERIALS);
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

    private static boolean addSubItem(ITinkersConsumableItem item, List<ItemStack> items, IMaterial material, IMaterial[] fixedMaterials) {

        if (TinkersArcheryConfig.COMMON.legacyArrows.get()) {
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
        items.add(ITinkersConsumableItem.of(item, new MaterialNBT(materials)));
        return true;
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        return TooltipUtil.getDisplayName(stack, this.getToolDefinition());
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        TooltipUtil.addInformation(this, stack, worldIn, tooltip, TooltipKey.fromScreen(), flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        // don't care about non-living, they skip most tool context
        if (entityIn instanceof LivingEntity) {
            ToolStack tool = ToolStack.from(stack);
            if (!worldIn.isClientSide) {
                tool.ensureHasData();
            }

            List<ModifierEntry> modifiers = tool.getModifierList();
            if (!modifiers.isEmpty()) {
                LivingEntity living = (LivingEntity)entityIn;
                boolean isHeld = isSelected || living.getOffhandItem() == stack;
                Iterator var10 = modifiers.iterator();

                while(var10.hasNext()) {
                    ModifierEntry entry = (ModifierEntry)var10.next();
                    entry.getModifier().onInventoryTick(tool, entry.getLevel(), worldIn, living, itemSlot, isSelected, isHeld, stack);
                }
            }
        }
    }

    @Override
    public List<ITextComponent> getStatInformation(IModifierToolStack tool, @Nullable PlayerEntity player, List<ITextComponent> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);

        builder.add(ToolStats.ATTACK_DAMAGE);
        builder.add(BowAndArrowToolStats.ACCURACY);
        builder.add(BowAndArrowToolStats.WEIGHT);
        builder.add(BowAndArrowToolStats.STABILITY);

        builder.addAllFreeSlots();

        for (ModifierEntry entry : tool.getModifierList()) {
            entry.getModifier().addInformation(tool, entry.getLevel(), player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }

    protected static boolean shouldInteract(@Nullable LivingEntity player, ToolStack toolStack, Hand hand) {
        IModDataReadOnly volatileData = toolStack.getVolatileData();
        if (volatileData.getBoolean(NO_INTERACTION)) {
            return false;
        } else {
            return hand == Hand.OFF_HAND || player == null || !volatileData.getBoolean(DEFER_OFFHAND) || player.getOffhandItem().isEmpty();
        }
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        ToolStack tool = ToolStack.from(stack);
        Hand hand = context.getHand();
        if (shouldInteract(context.getPlayer(), tool, hand)) {
            EquipmentSlotType slot = Util.getSlotType(hand);
            Iterator var6 = tool.getModifierList().iterator();

            while(var6.hasNext()) {
                ModifierEntry entry = (ModifierEntry)var6.next();
                ActionResultType result = entry.getModifier().beforeBlockUse(tool, entry.getLevel(), context, slot);
                if (result.consumesAction()) {
                    return result;
                }
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        ToolStack tool = ToolStack.from(context.getItemInHand());
        Hand hand = context.getHand();
        if (shouldInteract(context.getPlayer(), tool, hand)) {
            EquipmentSlotType slot = Util.getSlotType(hand);
            Iterator var5 = tool.getModifierList().iterator();

            while(var5.hasNext()) {
                ModifierEntry entry = (ModifierEntry)var5.next();
                ActionResultType result = entry.getModifier().afterBlockUse(tool, entry.getLevel(), context, slot);
                if (result.consumesAction()) {
                    return result;
                }
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        ToolStack tool = ToolStack.from(stack);
        if (shouldInteract(playerIn, tool, hand)) {
            EquipmentSlotType slot = Util.getSlotType(hand);
            Iterator var7 = tool.getModifierList().iterator();

            while(var7.hasNext()) {
                ModifierEntry entry = (ModifierEntry)var7.next();
                ActionResultType result = entry.getModifier().afterEntityUse(tool, entry.getLevel(), playerIn, target, hand, slot);
                if (result.consumesAction()) {
                    return result;
                }
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack stack = playerIn.getItemInHand(hand);
        ToolStack tool = ToolStack.from(playerIn.getItemInHand(hand));
        if (shouldInteract(playerIn, tool, hand)) {
            EquipmentSlotType slot = Util.getSlotType(hand);
            Iterator var7 = tool.getModifierList().iterator();

            while(var7.hasNext()) {
                ModifierEntry entry = (ModifierEntry)var7.next();
                ActionResultType result = entry.getModifier().onToolUse(tool, entry.getLevel(), worldIn, playerIn, hand, slot);
                if (result.consumesAction()) {
                    return new ActionResult(result, stack);
                }
            }
        }

        ActionResultType result = ToolInventoryCapability.tryOpenContainer(stack, tool, playerIn, Util.getSlotType(hand));
        return new ActionResult(result, stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ToolStack tool = ToolStack.from(stack);
        Iterator var5 = tool.getModifierList().iterator();

        ModifierEntry entry;
        do {
            if (!var5.hasNext()) {
                return stack;
            }

            entry = (ModifierEntry)var5.next();
        } while(!entry.getModifier().onFinishUsing(tool, entry.getLevel(), worldIn, entityLiving));

        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        Iterator var6 = tool.getModifierList().iterator();

        boolean result;
        do {
            if (!var6.hasNext()) {
                return;
            }

            ModifierEntry entry = (ModifierEntry)var6.next();
            result = entry.getModifier().onStoppedUsing(tool, entry.getLevel(), worldIn, entityLiving, timeLeft);
        } while(!result);

    }

    @Override
    public int getUseDuration(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        Iterator var3 = tool.getModifierList().iterator();

        int result;
        do {
            if (!var3.hasNext()) {
                return 0;
            }

            ModifierEntry entry = (ModifierEntry)var3.next();
            result = entry.getModifier().getUseDuration(tool, entry.getLevel());
        } while(result <= 0);

        return result;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        Iterator var3 = tool.getModifierList().iterator();

        UseAction result;
        do {
            if (!var3.hasNext()) {
                return UseAction.NONE;
            }

            ModifierEntry entry = (ModifierEntry)var3.next();
            result = entry.getModifier().getUseAction(tool, entry.getLevel());
        } while(result == UseAction.NONE);

        return result;
    }
}
