package tonite.tinkersarchery.library;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ProjectileTrajectory extends ForgeRegistryEntry<ProjectileTrajectory> {

    public ProjectileTrajectory() {}

    public abstract Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, float stability, float resistance, Object internalData);

    public abstract void load(Vector3d originalDirection, float weight, float stability, Object internalData, CompoundNBT data);

    public abstract Object onCreated(Vector3d originalDirection, float weight, float stability);

    public abstract void save(CompoundNBT data, Vector3d originalDirection, float weight, float stability, Object internalData);

    public static ListNBT newDoubleList(double... p_70087_1_) {
        ListNBT listnbt = new ListNBT();

        for(double d0 : p_70087_1_) {
            listnbt.add(DoubleNBT.valueOf(d0));
        }

        return listnbt;
    }

}
