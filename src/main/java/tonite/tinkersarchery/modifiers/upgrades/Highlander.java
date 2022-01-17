package tonite.tinkersarchery.modifiers.upgrades;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nullable;
import java.util.List;

public class Highlander extends IncrementalModifier implements IBowModifier {

    public static float BONUS = 1.0f;

    public Highlander() {
        super(0xFFFAFF99);
    }

    @Override
    public ITextComponent getDisplayName(int level) {
        // displays special names for levels of haste
        if (level <= 5) {
            return applyStyle(new TranslationTextComponent(getTranslationKey() + "." + level));
        }
        return super.getDisplayName(level);
    }

    public float getPower(IModifierToolStack tool, int level, float drawPortion, float power, World world, LivingEntity shooter) {

        int powerIncreaseHeight = world.getMaxBuildHeight() / 2;
        int powerIncreasePortion = world.getMaxBuildHeight() - powerIncreaseHeight;

        float multiplier;

        if (shooter.getY() <= powerIncreaseHeight) {
            multiplier = 0;
        } else {
            if (shooter.getY() < powerIncreaseHeight + powerIncreasePortion) {
                multiplier = ((float)shooter.getY() - powerIncreaseHeight) / powerIncreasePortion;
            } else {
                multiplier = 1;
            }
        }

        return power + tool.getModifier(BowAndArrowToolStats.DRAW_SPEED) * getScaledLevel(tool, level) * multiplier * BONUS;
    }

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey key, TooltipFlag flag) {
        float bonus = level * BONUS;
        if (player != null && key == TooltipKey.SHIFT) {
            int powerIncreaseHeight = player.level.getMaxBuildHeight() / 2;
            int powerIncreasePortion = player.level.getMaxBuildHeight() - powerIncreaseHeight;

            float multiplier;

            if (player.getY() <= powerIncreaseHeight) {
                multiplier = 0;
            } else {
                if (player.getY() < powerIncreaseHeight + powerIncreasePortion) {
                    multiplier = ((float)player.getY() - powerIncreaseHeight) / powerIncreasePortion;
                } else {
                    multiplier = 1;
                }
            }

            bonus = getScaledLevel(tool, level) * multiplier * BONUS;
        }
        addStatTooltip(tool, BowAndArrowToolStats.ELASTICITY, TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE, bonus, tooltip);
    }
}
