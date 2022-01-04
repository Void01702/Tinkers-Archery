package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import tonite.tinkersarchery.TinkersArchery;

public class Chaining extends Modifier {

    public static float MULTIPLIER = 1;

    public Chaining() {
        super(0xFF21007F);
    }

    @Override
    public float getEntityDamage(IModifierToolStack tool, int level, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker().hasEffect(TinkersArchery.chainingEffect.get())) {
            return damage * (1 + level * MULTIPLIER);
        }
        return damage;
    }

    @Override
    public int afterEntityHit(IModifierToolStack tool, int level, ToolAttackContext context, float damageDealt) {

        if (!context.isExtraAttack()) {
            LivingEntity target = context.getLivingTarget();
            // if the entity is dead now
            if (target != null && target.getHealth() == 0) {
                TinkersArchery.chainingEffect.get().apply(context.getAttacker(), 15 * 20, 0, true);
            } else {
                context.getAttacker().removeEffect(TinkersArchery.chainingEffect.get());
            }
        }
        return 0;
    }
}
