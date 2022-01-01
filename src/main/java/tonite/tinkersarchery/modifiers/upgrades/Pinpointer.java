package tonite.tinkersarchery.modifiers.upgrades;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

public class Pinpointer extends IncrementalModifier {
    public Pinpointer() {
        super(0xFFFFF6B9);
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
        // currently gives +5 speed per level
        // for comparison, vanilla gives +2, 5, 10, 17, 26 for efficiency I to V
        // 5 per level gives us          +5, 10, 15, 20, 25 for 5 levels
        BowAndArrowToolStats.ACCURACY.multiply(builder, 1 + scaledLevel * 0.4f);
    }
}
