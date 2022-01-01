package tonite.tinkersarchery.modifiers.upgrades;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IBowModifier;

public class Highlander extends IncrementalModifier implements IBowModifier {
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

        return power * (1 + getScaledLevel(tool, level) * multiplier);
    }
}
