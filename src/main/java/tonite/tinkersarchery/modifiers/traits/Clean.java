package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import slimeknights.tconstruct.library.utils.Util;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.library.modifier.IBowModifier;

import javax.annotation.Nullable;
import java.util.List;

public class Clean extends SingleUseModifier implements IBowModifier {

    private static final ITextComponent ACCURACY = new TranslationTextComponent(Util.makeTranslationKey("modifier", TinkersArchery.getResource("clean.accuracy")));

    public Clean() {
        super(0xFFD9D9D9);
    }

    @Override
    public float getAccuracy(IModifierToolStack tool, int level, float drawPortion, float baseAccuracy, float accuracy, World world, LivingEntity shooter) {
        if (drawPortion == 1.0f) {
            return accuracy * (1 + 0.3f * level);
        } else {
            return accuracy;
        }
    }

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey key, TooltipFlag flag) {
        if (tool.hasTag(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)) {
            float bonus = level * 1;
            addPercentTooltip(ACCURACY, bonus, tooltip);
        }
    }
}
