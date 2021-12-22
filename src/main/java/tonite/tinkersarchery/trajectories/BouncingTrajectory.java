package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class BouncingTrajectory extends ProjectileTrajectory {
    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object internalData) {
        return null;
    }

    @Override
    public void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data) {

    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight) {
        return null;
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, Object internalData) {

    }
}
