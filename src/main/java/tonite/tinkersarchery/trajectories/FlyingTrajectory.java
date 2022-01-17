package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class    FlyingTrajectory extends ProjectileTrajectory {
    public static final float GRAVITY = -0.05f;

    public FlyingTrajectory() {
        super();
    }

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, float stability, float resistance, Object data) {

        ((FlyingData)data).motion = ((FlyingData)data).motion.add(0, ((FlyingData)data).gravity, 0).scale(resistance + ((FlyingData)data).resistance_addition);

        return ((FlyingData)data).motion;

    }

    @Override
    public void load(Vector3d originalDirection, float weight, float stability, Object internalData, CompoundNBT data) {
        ListNBT directionList = data.getList("Motion", 6);
        ((FlyingData)internalData).motion = new Vector3d(directionList.getDouble(0), directionList.getDouble(1), directionList.getDouble(2));
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight, float stability) {
        return new FlyingData(GRAVITY * weight, stability, originalDirection);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, float stability, Object o) {
        FlyingData gravityData = (FlyingData)o;

        data.put("Motion", newDoubleList(gravityData.motion.x, gravityData.motion.y, gravityData.motion.z));
    }

    private static class FlyingData {
        public double gravity;

        public float resistance_addition;

        public Vector3d motion;

        public FlyingData(double gravity, float stability, Vector3d originalDirection) {
            this.gravity = gravity;
            resistance_addition = (float)(2.2 * Math.log(stability) + 1.5) / 100;
            motion = originalDirection;
        }
    }
}
