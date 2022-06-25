package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nullable;
import java.util.List;

public class Groovy extends Modifier implements IBowModifier {
    public Groovy() {
        super(0xFF21007F);
    }

    @Override
    public float getDrawSpeed(IModifierToolStack tool, int level, float baseDrawSpeed, float drawSpeed, World world, LivingEntity shooter) {

        if(shooter.hasEffect(TinkersArchery.groovyEffect.get())) {

            int effectLevel = TinkersArchery.groovyEffect.get().getLevel(shooter) + 1;

            return drawSpeed + tool.getModifier(BowAndArrowToolStats.DRAW_SPEED) * effectLevel * 0.2f;

        } else {

            return drawSpeed;
        }
    }

    @Override
    public Vector3f onReleaseBow(IModifierToolStack tool, int level, float drawPortion, float power, float accuracy, List<ArrowData> arrows, int arrowCount, World world, Vector3f currentDirection, LivingEntity shooter) {
        if(drawPortion > 0.75) {
            int effectLevel = Math.min(5 + (level - 1) * 6, TinkersArchery.groovyEffect.get().getLevel(shooter) + 1);
            TinkersArchery.groovyEffect.get().apply(shooter, 5 * 20, effectLevel, true);
        }
        return currentDirection;
    }

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey key, TooltipFlag flag) {
        float bonus = level * 0.2f * 6;
        if (player != null && key == TooltipKey.SHIFT) {
            bonus = 0;

            if(player.hasEffect(TinkersArchery.groovyEffect.get())) {
                int effectLevel = TinkersArchery.groovyEffect.get().getLevel(player) + 1;

                bonus = effectLevel * 0.2f;
            }
        }
        addStatTooltip(tool, BowAndArrowToolStats.DRAW_SPEED, TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE, bonus, tooltip);
    }
}
