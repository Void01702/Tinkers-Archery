package tonite.tinkersarchery.modifiers.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.library.projectileinterfaces.IPiercingProjectile;

public class Piercing extends Modifier implements IProjectileModifier {
    public Piercing() {
        super(0xFFE68600);
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, Vector3d direction, Entity shooter) {
        if (arrow instanceof IPiercingProjectile) {
            IPiercingProjectile projectile = (IPiercingProjectile) arrow;

            projectile.setPierceLevel(projectile.getPierceLevel() + level);
        }
    }
}
