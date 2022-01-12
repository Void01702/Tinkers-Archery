package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.ProjectileTrajectory;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.library.projectileinterfaces.ITrajectoryProjectile;

import java.util.function.Supplier;

public class BouncyTrajectoryApplier extends TrajectoryApplier {

    public BouncyTrajectoryApplier(int color, Supplier<ProjectileTrajectory> trajectory) {
        super(color, trajectory);
    }

    @Override
    public boolean onProjectileHitBlock(IModifierToolStack tool, int level, ProjectileEntity arrow, BlockState block, BlockPos pos, Vector3d direction) {

        if (arrow.getDeltaMovement().y() > -0.3f || arrow.getY() < pos.getY() + 1) return true;

        arrow.setPosRaw(arrow.getX(), pos.getY() + 1, arrow.getZ());

        if (arrow instanceof ITrajectoryProjectile) {
            ((ITrajectoryProjectile)arrow).changeDirection(arrow.getDeltaMovement().add(0, 0.1f, 0).multiply(1, -0.75, 1));
            ((ITrajectoryProjectile)arrow).setTrajectoryTime(0);
        }

        return false;
    }


}
