package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.library.projectileinterfaces.ICriticalProjectile;

public class Slicing extends SingleUseModifier implements IBowModifier {
    public Slicing() {
        super(0xFF959595);
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, float drawPortion, float power, World world, LivingEntity shooter) {
        if (arrow instanceof AbstractArrowEntity) {
            ((AbstractArrowEntity)arrow).setCritArrow(true);
        }

        if (arrow instanceof ICriticalProjectile) {
            ((ICriticalProjectile)arrow).setCritical(true);
        }
    }
}
