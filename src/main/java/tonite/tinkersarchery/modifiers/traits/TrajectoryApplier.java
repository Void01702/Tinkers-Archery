package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.projectile.ProjectileEntity;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.library.ProjectileTrajectory;

import java.util.function.Supplier;

public class TrajectoryApplier extends SingleUseModifier implements IProjectileModifier {

    Supplier<ProjectileTrajectory> trajectory;

    public TrajectoryApplier(int color, Supplier<ProjectileTrajectory> trajectory) {
        super(color);
        this.trajectory = trajectory;
    }

    @Override
    public void onArrowLoaded(IModifierToolStack tool, int level, ProjectileEntity arrow) {

        ((TinkersArrowEntity)arrow).setTrajectory(trajectory.get());

    }
}
