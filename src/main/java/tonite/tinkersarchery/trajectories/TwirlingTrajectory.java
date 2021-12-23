package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class TwirlingTrajectory extends ProjectileTrajectory {

    public static final float ANGLE = (float)(Math.PI / 4);
    public static final float TWIRL_SPEED = 0.3f;
    public static final int FALLOFF_START = 30;
    public static final int FALLOFF_TIME = 20;

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object internalData) {
        TwirlingData data = (TwirlingData)internalData;

        if (time < data.falloffStart + data.falloffTime) {

            float falloff = 1.0f;

            if (time >= data.falloffStart) {
                falloff = 1.0f - ((float) (time - data.falloffStart)) / data.falloffTime;
            }

            float newSin = data.currentCos * data.sin + data.currentSin * data.cos;
            float newCos = data.currentCos * data.cos - data.currentSin * data.sin;

            data.currentSin = newSin;
            data.currentCos = newCos;

            return originalDirection.add(data.right.scale(data.currentCos).add(data.up.scale(data.currentSin)).scale(TWIRL_SPEED * falloff / weight));
        } else {
            return originalDirection.add(0, -0.1f * (time - data.falloffStart - data.falloffTime), 0);
        }
    }

    @Override
    public void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data) {
        TwirlingData twirlingData = (TwirlingData)internalData;

        twirlingData.currentSin = data.getFloat("Sin");
        twirlingData.currentCos = data.getFloat("Cos");
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight) {
        return new TwirlingData(originalDirection, weight);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, Object internalData) {
        TwirlingData twirlingData = (TwirlingData)internalData;

        data.putFloat("Sin", twirlingData.currentSin);
        data.putFloat("Cos", twirlingData.currentCos);
    }

    private static class TwirlingData {

        public float sin;
        public float cos;

        public float currentSin;
        public float currentCos;

        public Vector3d right;
        public Vector3d up;

        public int falloffStart;
        public int falloffTime;

        public TwirlingData(Vector3d originalDirection, float weight) {
            currentSin = 0;
            currentCos = 1;

            sin = (float)Math.sin(ANGLE * weight);
            cos = (float)Math.cos(ANGLE * weight);

            right = originalDirection.cross(new Vector3d (0, 1f, 0)).normalize();
            up = right.cross(originalDirection).normalize();

            falloffStart = (int)(FALLOFF_START * weight);
            falloffTime = (int)(FALLOFF_TIME * weight);
        }
    }
}
