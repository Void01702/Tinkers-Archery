package tonite.tinkersarchery.modifiers.slotless.flares;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;

// Thanks to _XyJ_ for some ideas
public class FlameFlare extends Modifier implements IProjectileModifier {
    public FlameFlare() {
        super(0xFFFFC445);
    }

    @Override
    public void onProjectileFlyTick(IModifierToolStack tool, int level, ProjectileEntity arrow) {

        if (level == 1) {
            arrow.level.addParticle(ParticleTypes.FLAME, arrow.getX(), arrow.getY(), arrow.getZ(), 0, 0, 0);
        } else {
            arrow.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, arrow.getX(), arrow.getY(), arrow.getZ(), 0, 0, 0);
        }

    }

    @Override
    public ITextComponent getDisplayName(int level) {
        // displays special names for levels of haste
        if (level <= 2) {
            return applyStyle(new TranslationTextComponent(getTranslationKey() + "." + level));
        }
        return super.getDisplayName(level);
    }
}
