package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class FlyingTrajectory extends ProjectileTrajectory {
    public FlyingTrajectory() {
        super();
    }

    public static final int CUTOFF = 20;
    public static final double GRAVITY = 0.05f;

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object data) {


        if (time <= ((FlyingData)data).cutoffTicks) {

            return originalDirection.add(0, ((FlyingData)data).antigravity * ( time ), 0);

        } else {

            return originalDirection.add(0, -((FlyingData)data).gravity * ( time - ((FlyingData)data).cutoffTicks ) + ((FlyingData)data).antigravity * ((FlyingData)data).cutoffTicks, 0);

        }
    }

    @Override
    public void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data) {
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight) {
        return new FlyingData(weight);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, Object o) {

    }

    private static class FlyingData {
        public int cutoffTicks;
        public double gravity;
        public double antigravity;

        public FlyingData(float weight) {
            cutoffTicks = (int)(weight * CUTOFF);
            gravity = GRAVITY / weight;
            antigravity = GRAVITY * weight;
        }
    }
}
