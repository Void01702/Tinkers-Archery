package tonite.tinkersarchery.modifiers.slotless.flares;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particles.ParticleTypes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;

// Thanks to _XyJ_ for some ideas
public class ShulkerFlare extends SingleUseModifier implements IProjectileModifier {
    public ShulkerFlare() {
        super(0xFFD0D0D0);
    }

    @Override
    public void onProjectileFlyTick(IModifierToolStack tool, int level, ProjectileEntity arrow) {

        arrow.level.addParticle(ParticleTypes.END_ROD, arrow.getX(), arrow.getY(), arrow.getZ(), 0, 0, 0);

    }
}
