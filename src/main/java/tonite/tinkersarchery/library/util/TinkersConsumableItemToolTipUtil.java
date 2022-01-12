package tonite.tinkersarchery.library.util;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.item.ITinkerStationDisplay;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import tonite.tinkersarchery.library.ITinkersConsumableItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class TinkersConsumableItemToolTipUtil {

    private static final ITextComponent NO_DATA = TConstruct.makeTranslation("tooltip", "missing_data").withStyle(TextFormatting.GRAY);
    private static final ITextComponent UNINITIALIZED = TConstruct.makeTranslation("tooltip", "uninitialized").withStyle(TextFormatting.GRAY);
    private static final ITextComponent RANDOM_MATERIALS = TConstruct.makeTranslation("tooltip", "random_materials").withStyle(TextFormatting.GRAY);

    /** Translates client side only logic to a method that exists on serverside, used primarily since vanilla is annoying and takes away player access in the tooltip */
    @OnlyIn(Dist.CLIENT)
    public static void addInformation(IModifiableDisplay item, ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, TooltipKey tooltipKey, ITooltipFlag tooltipFlag) {
        PlayerEntity player = world == null ? null : Minecraft.getInstance().player;
        addInformation(item, stack, player, tooltip, tooltipKey, TooltipFlag.fromVanilla(tooltipFlag));
    }

    /**
     * Full logic for adding tooltip information, other than attributes
     */
    public static void addInformation(IModifiableDisplay item, ItemStack stack, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        // if the display tag is set, just show modifiers
        ToolDefinition definition = item.getToolDefinition();
        if (TooltipUtil.isDisplay(stack)) {
            ToolStack tool = ToolStack.createTool(item.asItem(), definition, ITinkersConsumableItem.getMaterials(stack).getMaterials());
            TooltipUtil.addModifierNames(stack, tool, tooltip);
            // No definition?
        } else if (!definition.isDataLoaded()) {
            tooltip.add(NO_DATA);

            // if not initialized, show no data tooltip on non-standard items
        } else if (!ITinkersConsumableItem.isInitialized(stack)) {
            tooltip.add(UNINITIALIZED);
            if (definition.isMultipart()) {
                CompoundNBT nbt = stack.getTag();
                if (nbt == null || !nbt.contains(ToolStack.TAG_MATERIALS, Constants.NBT.TAG_LIST)) {
                    tooltip.add(RANDOM_MATERIALS);
                }
            }
        } else {
            switch (tooltipKey) {
                case SHIFT:
                    item.getStatInformation(ToolStack.createTool(item.asItem(), definition, ITinkersConsumableItem.getMaterials(stack).getMaterials()), player, tooltip, tooltipKey, tooltipFlag);
                    break;
                case CONTROL:
                    if (definition.isMultipart()) {
                        TooltipUtil.getComponents(item, stack, tooltip);
                        break;
                    }
                    // intentional fallthrough
                default:
                    ToolStack tool = ToolStack.createTool(item.asItem(), definition, ITinkersConsumableItem.getMaterials(stack).getMaterials());
                    getDefaultInfo(stack, tool, tooltip);
                    break;
            }
        }
    }

    /**
     * Gets the display name for a tool including the head material in the name
     * @param stack  Stack instance
     * @return  Display name including the head material
     */
    public static ITextComponent getDisplayName(ItemStack stack, ToolDefinition toolDefinition) {
        List<PartRequirement> components = toolDefinition.getData().getParts();
        ITextComponent baseName = new TranslationTextComponent("item." + stack.getItem().getRegistryName().getNamespace() + "." + stack.getItem().getRegistryName().getPath());
        if (components.isEmpty()) {
            return baseName;
        }
        // if the tool is not named we use the repair tools for a prefix like thing
        // we save all the ones for the name in a set so we don't have the same material in it twice
        Set<IMaterial> nameMaterials = Sets.newLinkedHashSet();
        List<IMaterial> materials = ITinkersConsumableItem.getMaterials(stack).getMaterials();
        if (materials.size() == components.size()) {
            nameMaterials.add(materials.get(0));
        }
        return ITinkerStationDisplay.getCombinedItemName(stack, baseName, nameMaterials);
    }


    /**
     * Adds modifier names to the tooltip
     * @param stack      Stack instance. If empty, skips adding enchantment names
     * @param tool       Tool instance
     * @param tooltips   Tooltip list
     */
    public static void addModifierNames(ItemStack stack, IModifierToolStack tool, List<ITextComponent> tooltips) {
        ModifierNBT.Builder modBuilder = ModifierNBT.builder();
        modBuilder.add(tool.getUpgrades());
        modBuilder.add(tool.getDefinition().getData().getTraits());
        List<PartRequirement> parts = tool.getDefinition().getData().getParts();
        List<IMaterial> materials = tool.getMaterialsList();
        int max = Math.min(materials.size(), parts.size());
        for (int i = 0; i < max; i++) {
            modBuilder.add(MaterialRegistry.getInstance().getTraits(materials.get(i).getIdentifier(), parts.get(i).getStatType()));
        }
        ModifierNBT modifiers = modBuilder.build();

        for (ModifierEntry entry : modifiers.getModifiers()) {
            if (entry.getModifier().shouldDisplay(false)) {
                tooltips.add(entry.getModifier().getDisplayName(tool, entry.getLevel()));
            }
        }
    }


    /**
     * Adds information when holding neither control nor shift
     * @param tool      Tool stack instance
     * @param tooltips  Tooltip list
     */
    public static void getDefaultInfo(ItemStack stack, IModifierToolStack tool, List<ITextComponent> tooltips) {
        // modifier tooltip
        addModifierNames(stack, tool, tooltips);
        tooltips.add(StringTextComponent.EMPTY);
        tooltips.add(TooltipUtil.TOOLTIP_HOLD_SHIFT);
        if (tool.getDefinition().isMultipart()) {
            tooltips.add(TooltipUtil.TOOLTIP_HOLD_CTRL);
        }
    }
}
