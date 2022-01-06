package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class BouncingTrajectory extends ProjectileTrajectory {

    public static final int BOUNCE_LENGTH = 10;
    public static final int BOUNCE_COUNT = 5;

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object internalData) {
        BouncingData data = (BouncingData)internalData;

        if (time < data.bounceLength * BOUNCE_COUNT) {
            return originalDirection.add(0, data.startVelocity - data.gravity * (time % data.bounceLength), 0);
        } else {
            return originalDirection.add(0, -data.startVelocity - data.gravity * (time - data.bounceLength * BOUNCE_COUNT), 0);
        }
    }

    @Override
    public void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data) {

    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight) {
        return new BouncingData(weight);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, Object internalData) {

    }

    private static class BouncingData {
        public double gravity;
        public int bounceLength;
        public double startVelocity;

        public BouncingData(double weight) {
            this.gravity = 0.15 / weight;
            this.bounceLength =  (int)(BOUNCE_LENGTH * weight);
            this.startVelocity = gravity * (bounceLength - 1) / 2f;
        }
    }
}
