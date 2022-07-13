package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class AntigravityTrajectory extends ProjectileTrajectory {

    public static final int CUTOFF = 17;

    public AntigravityTrajectory() {
        super();
    }

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, float stability, float resistance, Object data) {

        if (time < ((AntigravityData)data).cutoffTicks) {
            return originalDirection;
        } else {
            ((AntigravityData)data).motion = ((AntigravityData)data).motion.add(0, -0.1f, 0).scale((float)Math.pow(resistance, 1 / stability));

            return ((AntigravityData)data).motion;
        }

    }

    @Override
    public void load(Vector3d originalDirection, float weight, float stability, Object internalData, CompoundNBT data) {
        ListNBT directionList = data.getList("Motion", 6);
        ((AntigravityData)internalData).motion = new Vector3d(directionList.getDouble(0), directionList.getDouble(1), directionList.getDouble(2));
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight, float stability) {
        return new AntigravityData(1, originalDirection);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, float stability, Object o) {
        AntigravityData gravityData = (AntigravityData)o;

        data.put("Motion", newDoubleList(gravityData.motion.x, gravityData.motion.y, gravityData.motion.z));
    }

    private static class AntigravityData {
        public int cutoffTicks;

        public Vector3d motion;

        public AntigravityData(float weight, Vector3d originalDirection) {
            cutoffTicks = (int)(CUTOFF/ weight);

            motion = originalDirection;
        }
    }
}
