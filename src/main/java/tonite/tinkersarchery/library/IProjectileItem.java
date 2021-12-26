package tonite.tinkersarchery.library;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.util.ProjectileAttackUtil;

public interface IProjectileItem {

    int consumeAmmo(ItemStack ammo, int desiredAmount, LivingEntity player);

    int getAmmo(ItemStack ammo, int desiredAmount);

    ProjectileEntity createProjectile(ItemStack ammo, World world, LivingEntity shooter, ItemStack bow);

    void supplyInfoToProjectile(ProjectileEntity projectile, ItemStack ammo, World world, LivingEntity shooter, ItemStack bow);

    default boolean dealDamageProjectile(ProjectileEntity projectile, IModifierToolStack stack, ToolAttackContext context, float damage) {
        return ProjectileAttackUtil.dealDefaultDamage(projectile, context.getAttacker(), context.getTarget(), damage);
    }

}
