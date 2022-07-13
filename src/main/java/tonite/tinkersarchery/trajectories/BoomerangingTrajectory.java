package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class BoomerangingTrajectory extends ProjectileTrajectory {

    public static final float ANGLE = (float)(Math.PI / 30);
    public static final int INITIAL_FORWARD_TIME = 10;
    public static final float INITIAL_SIDE_SCALE = -0.5f;

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, float stability, float resistance, Object internalData) {
        BoomerangingData data = (BoomerangingData)internalData;

        if (time > data.initialForwardTime) {
            time -= data.initialForwardTime;

            if (time > data.loopTime) {

                data.motion = data.motion.add(0, -0.1f, 0).scale((float)Math.pow(resistance, 1 / stability));

                return data.motion;

            } else {

                float newSin = data.currentCos * data.sin + data.currentSin * data.cos;
                float newCos = data.currentCos * data.cos - data.currentSin * data.sin;

                data.currentSin = newSin;
                data.currentCos = newCos;

                return originalDirection.scale(newCos).add(data.right.scale(newSin)).scale(resistance);
            }

        } else {
            return originalDirection.add(data.right.scale(data.initialSideScale * (data.initialForwardTime - time) / data.initialForwardTime)).scale(resistance);
        }
    }

    @Override
    public void load(Vector3d originalDirection, float weight, float stability, Object internalData, CompoundNBT data) {
        BoomerangingData loopingData = (BoomerangingData)internalData;

        loopingData.currentSin = data.getFloat("Sin");
        loopingData.currentCos = data.getFloat("Cos");

        ListNBT directionList = data.getList("Motion", 6);
        loopingData.motion = new Vector3d(directionList.getDouble(0), directionList.getDouble(1), directionList.getDouble(2));
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight, float stability) {
        return new BoomerangingData(originalDirection, 1, stability);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, float stability, Object internalData) {
        BoomerangingData loopingData = (BoomerangingData)internalData;

        data.putFloat("Sin", loopingData.currentSin);
        data.putFloat("Cos", loopingData.currentCos);

        data.put("Motion", newDoubleList(loopingData.motion.x, loopingData.motion.y, loopingData.motion.z));
    }

    private static class BoomerangingData {

        public int initialForwardTime;
        public float initialSideScale;
        public int loopTime;

        public float sin;
        public float cos;

        public float currentSin;
        public float currentCos;

        public Vector3d right;

        public Vector3d motion;

        public BoomerangingData(Vector3d originalDirection, float weight, float stability) {

            initialSideScale = INITIAL_SIDE_SCALE / stability;
            float angle = ANGLE / (initialSideScale * initialSideScale);

            currentSin = 0;
            currentCos = 1;

            sin = (float)Math.sin(angle);
            cos = (float)Math.cos(angle);

            right = originalDirection.cross(new Vector3d (0, 1d, 0)).normalize().scale(originalDirection.length());

            initialForwardTime = (int)(INITIAL_FORWARD_TIME / weight);
            loopTime = (int)(Math.PI * 6 / (2 * angle));

            motion = originalDirection.reverse();

        }
    }
}
