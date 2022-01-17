package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.library.projectileinterfaces.ITrajectoryProjectile;

public class Uplifting extends SingleUseModifier implements IBowModifier {
    public Uplifting() {
        super(0xFFF7CDBB);
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, float drawPortion, float power, World world, LivingEntity shooter){
        if (arrow instanceof ITrajectoryProjectile) {
            ITrajectoryProjectile newArrow = (ITrajectoryProjectile)arrow;
            newArrow.changeDirection(newArrow.getOriginalDirection().add(new Vector3d(0, level * power * 0.5 / 3, 0)));
        } else {
            arrow.setDeltaMovement(arrow.getDeltaMovement().add(new Vector3d(0, level * power * 0.5 / 3, 0)));
        }
    }
}
