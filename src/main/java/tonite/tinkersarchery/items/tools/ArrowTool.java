package tonite.tinkersarchery.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.library.IProjectileItem;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;

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
        TinkersArrowEntity arrow = new TinkersArrowEntity(world, player);

        return arrow;
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
}
