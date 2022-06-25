package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class GravityTrajectory extends ProjectileTrajectory {

    private float gravity;

    public GravityTrajectory(float gravity) {
        super();
        this.gravity = gravity;
    }

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, float stability, float resistance, Object data) {

        ((GravityData)data).motion = ((GravityData)data).motion.add(0, ((GravityData)data).gravity, 0).scale(1/(1+Math.pow(Math.E, -4.6 * stability)));

        return ((GravityData)data).motion;

    }

    @Override
    public void load(Vector3d originalDirection, float weight, float stability, Object internalData, CompoundNBT data) {
        ListNBT directionList = data.getList("Motion", 6);
        ((GravityData)internalData).motion = new Vector3d(directionList.getDouble(0), directionList.getDouble(1), directionList.getDouble(2));
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight, float stability) {
        return new GravityData(gravity * weight, originalDirection);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, float stability, Object o) {
        GravityData gravityData = (GravityData)o;

        data.put("Motion", newDoubleList(gravityData.motion.x, gravityData.motion.y, gravityData.motion.z));
    }

    private static class GravityData {
        public double gravity;

        public Vector3d motion;

        public GravityData(double gravity, Vector3d originalDirection) {
            this.gravity = gravity;
            motion = originalDirection;
        }
    }
}
