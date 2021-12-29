package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.library.projectileinterfaces.ICriticalProjectile;
import tonite.tinkersarchery.library.projectileinterfaces.IWeightProjectile;

public class Weighty extends Modifier implements IBowModifier {
    public Weighty() {
        super(0xFF5079FF);
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, float drawPortion, float power, World world, LivingEntity shooter) {
        if (arrow instanceof IWeightProjectile) {
            IWeightProjectile projectile = ((IWeightProjectile)arrow);
            projectile.setWeight(level * 0.2f);
        }
    }
}
