package tonite.tinkersarchery.library.projectileinterfaces;

import net.minecraft.util.math.vector.Vector3d;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public interface ITrajectoryProjectile {

    ProjectileTrajectory getProjectileTrajectory();

    void setProjectileTrajectory(ProjectileTrajectory trajectory);

    Vector3d getOriginalDirection();

    void setOriginalDirection(Vector3d originalDirection);

    void changeDirection(Vector3d direction);

    int getTrajectoryTime();

    void setTrajectoryTime(int ticks);

}
