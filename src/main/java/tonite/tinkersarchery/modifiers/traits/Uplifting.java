package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.library.IBowModifier;
import tonite.tinkersarchery.library.ProjectileTrajectory;

public class Uplifting extends Modifier implements IBowModifier {
    public Uplifting() {
        super(0xFF119B85);
    }

    @Override
    public void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, float drawPortion, float power, World world, LivingEntity shooter){
        if (arrow instanceof TinkersArrowEntity) {
            TinkersArrowEntity newArrow = (TinkersArrowEntity)arrow;
            newArrow.changeDirection(arrow.getDeltaMovement().add(new Vector3d(0, level * power * 0.5, 0)));
        } else {
            arrow.setDeltaMovement(arrow.getDeltaMovement().add(new Vector3d(0, level * power * 0.5, 0)));
        }
    }
}
