package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class GravityTrajectory extends ProjectileTrajectory {

    public GravityTrajectory() {
        super();
    }

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object data) {

        return originalDirection.add(0, ((GravityData)data).gravity * time, 0);

    }

    @Override
    public void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data) {}

    @Override
    public Object onCreated(Vector3d originalDirection, float weight) {
        return new GravityData(-0.05 / weight);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, Object o) {}

    private static class GravityData {
        public double gravity;

        public GravityData(double gravity) {
            this.gravity = gravity;
        }
    }
}
