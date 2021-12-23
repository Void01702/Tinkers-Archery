package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class FlyingTrajectory extends ProjectileTrajectory {
    public FlyingTrajectory() {
        super();
    }

    public static final int CUTOFF = 20;

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object data) {


        if (time <= CUTOFF) {

            return originalDirection.add(0, 0.05 * ( time ), 0);

        } else {

            return originalDirection.add(0, -0.05 * ( time - CUTOFF ) + 0.05 * CUTOFF, 0);

        }
    }

    @Override
    public void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data) {
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight) {
        return null;
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, Object o) {

    }
}
