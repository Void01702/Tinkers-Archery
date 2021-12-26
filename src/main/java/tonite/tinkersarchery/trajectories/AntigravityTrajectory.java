package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class AntigravityTrajectory extends ProjectileTrajectory {

    public static final int CUTOFF = 60;
    public static final float INITIAL_SPEED = 0.5f;
    public static final float SPEED_LOSS = 0.25f;

    public AntigravityTrajectory() {
        super();
    }

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object data) {

        if (time < ((AntigravityData)data).cutoffTicks) {
            return originalDirection.scale(INITIAL_SPEED - ((AntigravityData)data).speedLoss * time / ((AntigravityData)data).cutoffTicks);
        } else {
            return originalDirection.scale(INITIAL_SPEED - ((AntigravityData)data).speedLoss).add(0, -0.05 * (time - ((AntigravityData)data).cutoffTicks), 0);
        }

    }

    @Override
    public void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data) {}

    @Override
    public Object onCreated(Vector3d originalDirection, float weight) {
        return new AntigravityData(weight);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, Object o) {

    }

    private static class AntigravityData {
        public int cutoffTicks;
        public float speedLoss;

        public AntigravityData(float weight) {
            cutoffTicks = (int)(weight * CUTOFF);
            speedLoss = Math.min(0.49f, SPEED_LOSS / weight);
        }
    }
}