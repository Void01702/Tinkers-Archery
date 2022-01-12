package tonite.tinkersarchery.modifiers.abilities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;

public class Explosive extends Modifier implements IProjectileModifier {
    public Explosive() {
        super(0xFF8A8A8A);
    }

    @Override
    public boolean onProjectileHitBlock(IModifierToolStack tool, int level, ProjectileEntity arrow, BlockState block, BlockPos pos, Vector3d direction) {

        arrow.level.explode(arrow.getOwner(), arrow.getX(), arrow.getY(), arrow.getZ(), 2 * level, Explosion.Mode.BREAK);

        arrow.remove();

        return true;
    }
}
