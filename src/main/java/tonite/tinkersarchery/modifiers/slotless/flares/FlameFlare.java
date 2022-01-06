package tonite.tinkersarchery.modifiers.slotless.flares;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particles.ParticleTypes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;

// Thanks to _XyJ_ for some ideas
public class FlameFlare extends Modifier implements IProjectileModifier {
    public FlameFlare() {
        super(0xFF8A8A8A);
    }

    @Override
    public void onProjectileFlyTick(IModifierToolStack tool, int level, ProjectileEntity arrow) {

        if (level == 1) {
            arrow.level.addParticle(ParticleTypes.FLAME, arrow.getX(), arrow.getY(), arrow.getZ(), 0, 0, 0);
        } else {
            arrow.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, arrow.getX(), arrow.getY(), arrow.getZ(), 0, 0, 0);
        }

    }
}
