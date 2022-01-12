package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import slimeknights.tconstruct.tools.TinkerModifiers;
import tonite.tinkersarchery.TinkersArchery;

import javax.annotation.Nullable;
import java.util.List;

public class Chaining extends Modifier {

    public Chaining() {
        super(0xFF21007F);
    }

    @Override
    public float getEntityDamage(IModifierToolStack tool, int level, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker().hasEffect(TinkersArchery.chainingEffect.get())) {
            return damage + (tool.getModifier(ToolStats.ATTACK_DAMAGE) * 3 * level);
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

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey key, TooltipFlag flag) {
        float bonus = level * 3;
        if (player != null && key == TooltipKey.SHIFT) {
            bonus = 0;

            if (player.hasEffect(TinkersArchery.chainingEffect.get())) {
                bonus = level * 3;
            }
        }
        addDamageTooltip(tool, bonus, tooltip);
    }
}
