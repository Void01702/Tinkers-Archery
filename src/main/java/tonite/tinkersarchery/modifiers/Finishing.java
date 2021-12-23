package tonite.tinkersarchery.modifiers;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

public class Finishing extends Modifier {

    public static float HEALTH_PORTION = 0.5f;
    public static float MULTIPLIER = 3;

    public Finishing() {
        super(1908001);
    }

    @Override
    public float getEntityDamage(IModifierToolStack tool, int level, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getLivingTarget().getHealth()/context.getLivingTarget().getMaxHealth() < HEALTH_PORTION) {
            return damage * MULTIPLIER;
        }else{
            return damage;
        }
    }
}
