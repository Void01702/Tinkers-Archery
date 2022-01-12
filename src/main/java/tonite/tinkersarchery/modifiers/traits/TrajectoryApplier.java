package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.library.ProjectileTrajectory;
import tonite.tinkersarchery.library.projectileinterfaces.ITrajectoryProjectile;

import java.util.function.Supplier;

public class TrajectoryApplier extends SingleUseModifier implements IProjectileModifier {

    Supplier<ProjectileTrajectory> trajectory;

    public TrajectoryApplier(int color, Supplier<ProjectileTrajectory> trajectory) {
        super(color);
        this.trajectory = trajectory;
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, Vector3d direction, Entity shooter) {

        ((ITrajectoryProjectile) arrow).setProjectileTrajectory(trajectory.get());

    }
}
