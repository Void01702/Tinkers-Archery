package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import tonite.tinkersarchery.TinkersArchery;

import javax.annotation.Nullable;
import java.util.List;

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

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey key, TooltipFlag flag) {
        float bonus = level * 2;
        addDamageTooltip(tool, bonus, tooltip);
    }
}
