package tonite.tinkersarchery.modifiers.traits;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

public class Swiftstrike extends Modifier {

    public static float MULTIPLIER = 2f;

    public Swiftstrike() {
        super(0xFF5079FF);
    }

    @Override
    public float getEntityDamage(IModifierToolStack tool, int level, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker() != null) {
            return damage * (float) (1 + context.getAttacker().getDeltaMovement().length() * level * MULTIPLIER);
        } else {
            return damage;
        }
    }
}
