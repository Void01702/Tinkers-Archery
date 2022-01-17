package tonite.tinkersarchery.modifiers.upgrades;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

public class Haste extends IncrementalModifier {
    public Haste() {
        super(0xFFAA0F01);
    }

    @Override
    public ITextComponent getDisplayName(int level) {
        // displays special names for levels of haste
        if (level <= 5) {
            return applyStyle(new TranslationTextComponent(getTranslationKey() + "." + level));
        }
        return super.getDisplayName(level);
    }

    @Override
    public void addToolStats(ToolRebuildContext context, int level, ModifierStatsBuilder builder) {
        float scaledLevel = getScaledLevel(context.getPersistentData(), level);
        BowAndArrowToolStats.DRAW_SPEED.add(builder, scaledLevel * 0.25f);
    }
}
