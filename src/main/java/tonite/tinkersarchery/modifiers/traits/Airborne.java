package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.entities.TinkersArrowEntityLegacy;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.library.projectileinterfaces.ITrajectoryProjectile;

public class Airborne extends SingleUseModifier implements IBowModifier {
    public Airborne() {
        super(0xFF00F4DA);
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, float drawPortion, float power, World world, LivingEntity shooter) {
        Vector3d direction = shooter.getDeltaMovement().reverse();

        if (shooter.isOnGround()) {
            direction = new Vector3d(direction.x, 0, direction.z);
        }

        if (arrow instanceof ITrajectoryProjectile) {
            ((ITrajectoryProjectile)arrow).changeDirection(((ITrajectoryProjectile)arrow).getOriginalDirection().add(direction));
        } else {
            arrow.setDeltaMovement(arrow.getDeltaMovement().add(direction));
        }
    }
}
