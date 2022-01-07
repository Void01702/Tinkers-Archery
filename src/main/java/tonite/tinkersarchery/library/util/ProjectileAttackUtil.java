package tonite.tinkersarchery.library.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.IProjectileItem;
import tonite.tinkersarchery.library.projectileinterfaces.ICriticalProjectile;
import tonite.tinkersarchery.library.projectileinterfaces.IDamageProjectile;
import tonite.tinkersarchery.library.projectileinterfaces.IKnockbackProjectile;

public class ProjectileAttackUtil {

    public static boolean dealDefaultDamage(ProjectileEntity projectile, LivingEntity attacker, Entity target, float damage) {
        return target.hurt(DamageSource.thrown(projectile, attacker), damage);
    }

    public static boolean attackEntity(Item weapon, ProjectileEntity projectile, IModifierToolStack tool, LivingEntity attackerLiving,
                                       Entity targetEntity, boolean isExtraAttack, IModifierToolStack bowStack) {

        boolean wasCritical = false;

        if (projectile instanceof ICriticalProjectile) {
            wasCritical = ((ICriticalProjectile)projectile).getCritical();
        }

        LivingEntity targetLiving = targetEntity instanceof LivingEntity ? (LivingEntity) targetEntity: null;

        if (attackerLiving == null) {
            return false;
        }

        ToolAttackContext context = new ToolAttackContext(attackerLiving,
                attackerLiving instanceof PlayerEntity ? (PlayerEntity) attackerLiving: null, Hand.MAIN_HAND,
                targetEntity, targetLiving, wasCritical, 1, isExtraAttack);

        float damage;
        float knockback;

        if (projectile instanceof IDamageProjectile) {
            damage = ((IDamageProjectile)projectile).calculateDamage();
        } else {
            damage = tool.getDamage();
        }

        float baseDamage = damage;

        for (ModifierEntry e: tool.getModifierList()) {
            damage = e.getModifier().getEntityDamage(tool, e.getLevel(), context, baseDamage, damage);
        }

        if (bowStack != null) {
            baseDamage = damage;

            for (ModifierEntry e: bowStack.getModifierList()) {
                damage = e.getModifier().getEntityDamage(tool, e.getLevel(), context, baseDamage, damage);
            }
        }

        if (projectile instanceof IKnockbackProjectile) {
            knockback = ((IKnockbackProjectile)projectile).calculateKnockback();
        } else {
            knockback = 0.4f;
        }

        float baseKnockback = knockback;

        for (ModifierEntry e: tool.getModifierList()) {
            knockback = e.getModifier().beforeEntityHit(tool, e.getLevel(), context, damage, baseKnockback, knockback);
        }

        if (bowStack != null) {
            baseKnockback = knockback;

            for (ModifierEntry e: bowStack.getModifierList()) {
                knockback = e.getModifier().beforeEntityHit(tool, e.getLevel(), context, damage, baseKnockback, knockback);
            }
        }

        float oldHealth = 0.0F;
        if (context.getLivingTarget() != null) {
            oldHealth = context.getLivingTarget().getHealth();
        }

        boolean didHit;
        if (isExtraAttack || !(weapon instanceof IProjectileItem)) {
            didHit = dealDefaultDamage(projectile, attackerLiving, targetEntity, damage);
        } else {
            didHit = ((IProjectileItem)weapon).dealDamageProjectile(projectile, tool, context, damage);
        }

        if (didHit) {

            float damageDealt = damage;
            if (targetLiving != null) {
                damageDealt = oldHealth - targetLiving.getHealth();
            }

            if (knockback > 0) {
                Vector3d vector3d = projectile.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)knockback * 0.6D);
                if (vector3d.lengthSqr() > 0.0D) {
                    targetEntity.push(vector3d.x, 0.1D, vector3d.z);
                }
            }

            for (ModifierEntry e: tool.getModifierList()) {
                e.getModifier().afterEntityHit(tool, e.getLevel(), context, damageDealt);
            }

            if (bowStack != null) {
                for (ModifierEntry e: bowStack.getModifierList()) {
                    e.getModifier().afterEntityHit(tool, e.getLevel(), context, damageDealt);
                }
            }

            return true;

        } else {

            for (ModifierEntry e: tool.getModifierList()) {
                e.getModifier().failedEntityHit(tool, e.getLevel(), context);
            }

            if (bowStack != null) {
                for (ModifierEntry e: bowStack.getModifierList()) {
                    e.getModifier().failedEntityHit(tool, e.getLevel(), context);
                }
            }

            return !isExtraAttack;

        }
    }
}
