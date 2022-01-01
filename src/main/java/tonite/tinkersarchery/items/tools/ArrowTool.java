package tonite.tinkersarchery.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.library.IProjectileItem;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nullable;
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
        if (getDamage(ammo) < getMaxDamage(ammo) - 1) {
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
        return new TinkersArrowEntity(world, player);
    }

    @Override
    public void supplyInfoToProjectile(ProjectileEntity projectile, ItemStack ammo, World world, LivingEntity shooter, ItemStack bow) {
        TinkersArrowEntity newProjectile = ((TinkersArrowEntity)projectile);
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
            builder.add(BowAndArrowToolStats.SPEED);
            builder.add(BowAndArrowToolStats.ACCURACY);
            builder.add(BowAndArrowToolStats.WEIGHT);
        }

        builder.addAllFreeSlots();

        for (ModifierEntry entry : tool.getModifierList()) {
            entry.getModifier().addInformation(tool, entry.getLevel(), player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}
