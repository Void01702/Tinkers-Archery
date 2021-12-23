package tonite.tinkersarchery.library;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

public interface IProjectileModifier {

    /**
     * Called when an arrow with this modifier is shot.
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param arrow          Arrow shot from bow
     * @param shooter        Entity who shot tool
     */
    default void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, Vector3d direction, Entity shooter) {}

    /**
     * Called when an arrow with this modifier is loaded.
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param arrow          Arrow shot from bow
     */
    default void onArrowLoaded(IModifierToolStack tool, int level, ProjectileEntity arrow) {}

}
