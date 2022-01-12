package tonite.tinkersarchery.trajectories;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class TwirlingTrajectory extends ProjectileTrajectory {

    public static final float ANGLE = (float)(Math.PI / 6);
    public static final float SPREAD = 1f;
    public static final float SPREAD_RATE = 0.3f;
    public static final float ANTIGRAVITY_THRESHOLD = 0.3f;

    @Override
    public Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, float stability, float resistance, Object internalData) {
        TwirlingData data = (TwirlingData)internalData;

        if (data.motion.length() < data.antigravityThreshold) {
            data.falling = true;
        }

        if (data.falling) {
            data.motion = data.motion.add(0, -0.1, 0).scale(resistance);
        } else {
            data.motion = data.motion.scale(resistance);
        }

        float oldSin = data.currentSin;
        float oldCos = data.currentCos;

        float newSin = data.currentCos * data.sin + data.currentSin * data.cos;
        float newCos = data.currentCos * data.cos - data.currentSin * data.sin;

        data.currentSin = newSin;
        data.currentCos = newCos;

        float expansionOld = getExpansionScale(time, data);
        float expansionNew = getExpansionScale(time + 1, data);

        Vector3d init = data.right.scale(oldCos).add(data.up.scale(oldSin)).scale(expansionOld);
        Vector3d end = data.right.scale(newCos).add(data.up.scale(newSin)).scale(expansionNew);

        return data.motion.add(end).subtract(init);
    }

    private static float getExpansionScale(int time, TwirlingData data) {
        return data.spread * (1.0f - 1.0f / (1 + time * data.spreadRate));
    }

    @Override
    public void load(Vector3d originalDirection, float weight, float stability, Object internalData, CompoundNBT data) {
        TwirlingData twirlingData = (TwirlingData)internalData;

        twirlingData.falling = data.getBoolean("Falling");

        twirlingData.currentSin = data.getFloat("Sin");
        twirlingData.currentCos = data.getFloat("Cos");

        ListNBT directionList = data.getList("Motion", 6);
        twirlingData.motion = new Vector3d(directionList.getDouble(0), directionList.getDouble(1), directionList.getDouble(2));
    }

    @Override
    public Object onCreated(Vector3d originalDirection, float weight, float stability) {
        return new TwirlingData(originalDirection, weight, stability);
    }

    @Override
    public void save(CompoundNBT data, Vector3d originalDirection, float weight, float stability, Object internalData) {
        TwirlingData twirlingData = (TwirlingData)internalData;

        data.putBoolean("Falling", twirlingData.falling);

        data.putFloat("Sin", twirlingData.currentSin);
        data.putFloat("Cos", twirlingData.currentCos);

        data.put("Motion", newDoubleList(twirlingData.motion.x, twirlingData.motion.y, twirlingData.motion.z));
    }

    private static class TwirlingData {

        public float spread;
        public float spreadRate;

        public float sin;
        public float cos;

        public float currentSin;
        public float currentCos;

        public Vector3d right;
        public Vector3d up;

        public float antigravityThreshold;
        public boolean falling = false;

        public Vector3d motion;

        public TwirlingData(Vector3d originalDirection, float weight, float stability) {
            spread = SPREAD / stability;
            spreadRate = SPREAD_RATE * stability;

            currentSin = 0;
            currentCos = 1;

            sin = (float)Math.sin(ANGLE / stability);
            cos = (float)Math.cos(ANGLE / stability);

            right = originalDirection.cross(new Vector3d (0, 1f, 0)).normalize();
            up = right.cross(originalDirection).normalize();

            antigravityThreshold = (float)Math.pow(ANTIGRAVITY_THRESHOLD, 1 / weight);

            motion = originalDirection;
        }
    }
}
