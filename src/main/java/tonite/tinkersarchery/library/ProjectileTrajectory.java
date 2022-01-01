package tonite.tinkersarchery.library;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ProjectileTrajectory extends ForgeRegistryEntry<ProjectileTrajectory> {

    public ProjectileTrajectory() {}

    public abstract Vector3d getMotionDirection(int time, Vector3d originalDirection, float weight, Object internalData);

    public abstract void load(Vector3d originalDirection, float weight, Object internalData, CompoundNBT data);

    public abstract Object onCreated(Vector3d originalDirection, float weight);

    public abstract void save(CompoundNBT data, Vector3d originalDirection, float weight, Object internalData);

}
