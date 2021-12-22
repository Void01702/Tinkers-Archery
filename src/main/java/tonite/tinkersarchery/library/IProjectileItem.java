package tonite.tinkersarchery.library;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IProjectileItem {

    int consumeAmmo(ItemStack ammo, int amount, LivingEntity player);

    int getAmmo(ItemStack ammo, LivingEntity player);

    ProjectileEntity createProjectile(ItemStack ammo, World world, LivingEntity shooter, ItemStack bow);

    void supplyInfoToProjectile(ProjectileEntity projectile, ItemStack ammo, World world, LivingEntity shooter, ItemStack bow);

}
