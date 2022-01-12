package tonite.tinkersarchery.modifiers.abilities;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IBowModifier;

import java.util.List;

public class Autoaim extends SingleUseModifier implements IBowModifier {

    public Autoaim() {
        super(0xFFFF7600);
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, float drawPortion, float power, World world, LivingEntity shooter){
        List<LivingEntity> targets = world.getNearbyEntities(LivingEntity.class, new EntityPredicate(), shooter, shooter.getBoundingBox().inflate(34, 34, 34));

        Vector3d testPos = shooter.getEyePosition(1.0f).add(shooter.getLookAngle().scale(17));

        LivingEntity target = world.getNearestEntity(targets, new EntityPredicate(), shooter, testPos.x, testPos.y, testPos.z);



        if (target != null && target.position().subtract(testPos).length() < 16) {

            Vector3d direction = target.getEyePosition(1.0f).subtract(shooter.getEyePosition(1.0f)).normalize();

            arrow.shoot(direction.x, direction.y, direction.z, power * 3, 1);

        }
    }

}
