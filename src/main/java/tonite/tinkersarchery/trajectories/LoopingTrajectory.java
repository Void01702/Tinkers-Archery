package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class LoopingTrajectory extends ProjectileTrajectory {

    public static final float ANGLE = (float)(Math.PI / 8);
    public static final int INITIAL_STRAIGHT_TIME = 10;
    public static final int MIDDLE_STRAIGHT_TIME = 30;

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, float stability, float resistance, Object internalData) {
        LoopingData data = (LoopingData)internalData;

        if (time > INITIAL_STRAIGHT_TIME) {
            time -= INITIAL_STRAIGHT_TIME;

            if (time > data.initialLoopTime) {
                time -= data.initialLoopTime;

                if (time > data.straightTime) {
                    time -= data.straightTime;

                    if (time > data.finalLoopTime) {
                        return data.immediatelyUp.reverse();

                    } else {
                        float newSin = data.currentCos * data.sin + data.currentSin * data.cos;
                        float newCos = data.currentCos * data.cos - data.currentSin * data.sin;

                        data.currentSin = newSin;
                        data.currentCos = newCos;

                        return data.flatForward.scale(newCos).add(data.immediatelyUp.scale(newSin));
                    }
                } else {
                    data.currentSin = 0;
                    data.currentCos = 1;

                    return  data.flatForward;
                }
            } else {
                float newSin = data.currentCos * data.sin + data.currentSin * data.cos;
                float newCos = data.currentCos * data.cos - data.currentSin * data.sin;

                data.currentSin = newSin;
                data.currentCos = newCos;

                return originalDirection.scale(newCos).add(data.up.scale(newSin));
            }
        } else {
            return originalDirection;
        }
    }

    @Override
    public void load(Vector3d originalDirection, float weight, float stability, Object internalData, CompoundNBT data) {
        LoopingData loopingData = (LoopingData)internalData;

        loopingData.currentSin = data.getFloat("Sin");
        loopingData.currentCos = data.getFloat("Cos");
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight, float stability) {
        return new LoopingData(originalDirection, weight, stability);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, float stability, Object internalData) {
        LoopingData loopingData = (LoopingData)internalData;

        data.putFloat("Sin", loopingData.currentSin);
        data.putFloat("Cos", loopingData.currentCos);
    }

    private static class LoopingData {

        public int initialLoopTime;
        public int straightTime;
        public int finalLoopTime;

        public float sin;
        public float cos;

        public float currentSin;
        public float currentCos;

        public Vector3d up;
        public Vector3d flatForward;
        public Vector3d immediatelyUp;

        public LoopingData(Vector3d originalDirection, float weight, float stability) {

            float angle = ANGLE * stability;

            currentSin = 0;
            currentCos = 1;

            sin = (float)Math.sin(angle);
            cos = (float)Math.cos(angle);

            Vector3d right = originalDirection.cross(new Vector3d (0, 1d, 0)).normalize();
            up = right.cross(originalDirection).normalize().scale(originalDirection.length());
            flatForward = new Vector3d(0, 1d, 0).cross(right).scale(originalDirection.length());

            straightTime = (int)(MIDDLE_STRAIGHT_TIME / weight);

            finalLoopTime = (int)(3 * Math.PI / (2 * angle));

            float dAngle = (float)Math.acos(new Vector3d(0, 1d, 0).dot(originalDirection.normalize()));

            initialLoopTime = finalLoopTime + (int)(dAngle / angle) + 1;

            immediatelyUp = new Vector3d(0, originalDirection.length(), 0);
        }
    }
}
