package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.library.IProjectileModifier;
import tonite.tinkersarchery.library.ProjectileTrajectory;

import java.util.function.Supplier;

public class TrajectoryApplier extends Modifier implements IProjectileModifier {

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
