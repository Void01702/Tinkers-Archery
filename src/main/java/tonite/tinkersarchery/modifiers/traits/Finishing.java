package tonite.tinkersarchery.modifiers.traits;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

public class Finishing extends Modifier {

    public static float HEALTH_PORTION = 0.5f;
    public static float MULTIPLIER = 0.25f;

    public Finishing() {
        super(0xFF9EB9D4);
    }

    @Override
    public float getEntityDamage(IModifierToolStack tool, int level, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getLivingTarget().getHealth()/context.getLivingTarget().getMaxHealth() < HEALTH_PORTION) {
            return damage * (1 + level * MULTIPLIER);
        }else{
            return damage;
        }
    }
}
