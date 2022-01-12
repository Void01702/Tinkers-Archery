package tonite.tinkersarchery.library.modifier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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

    /**
     * Called every tick an arrow with this modifier is loaded.
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param arrow          Arrow shot from bow
     */
    default void onProjectileTick(IModifierToolStack tool, int level, ProjectileEntity arrow) {}

    /**
     * Called every tick an arrow with this modifier flies through the air (or water).
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param arrow          Arrow shot from bow
     */
    default void onProjectileFlyTick(IModifierToolStack tool, int level, ProjectileEntity arrow) {}

    /**
     * Called every tick an arrow with this modifier flies through the air (or water).
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param arrow          Arrow shot from bow
     */
    default void onProjectileGroundTick(IModifierToolStack tool, int level, ProjectileEntity arrow) {}

    /**
     * Called when an arrow with this modifier hits a block.
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param arrow          Arrow shot from bow
     * @param block          Block the arrow hit
     * @param direction      Direction the arrow moved when it hit the block
     */
    default boolean onProjectileHitBlock(IModifierToolStack tool, int level, ProjectileEntity arrow, BlockState block, BlockPos pos, Vector3d direction) {return true;}

}
