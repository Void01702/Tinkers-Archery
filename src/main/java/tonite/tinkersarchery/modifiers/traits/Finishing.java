package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import slimeknights.tconstruct.library.utils.Util;
import tonite.tinkersarchery.TinkersArchery;

import javax.annotation.Nullable;
import java.util.List;

public class Finishing extends Modifier {

    private static final ITextComponent ATTACK_DAMAGE = new TranslationTextComponent(Util.makeTranslationKey("modifier", TinkersArchery.getResource("finishing.attack_damage")));

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

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey key, TooltipFlag flag) {
        if (tool.hasTag(TinkerTags.Items.MELEE_OR_HARVEST)) {
            float bonus = level * MULTIPLIER;
            addPercentTooltip(ATTACK_DAMAGE, bonus, tooltip);
        }
    }
}
