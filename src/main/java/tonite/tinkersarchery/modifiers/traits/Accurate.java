package tonite.tinkersarchery.modifiers.traits;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

public class Accurate extends Modifier {

    public Accurate() {
        super(0xFFC8FDFF);
    }

    @Override
    public void addToolStats(ToolRebuildContext context, int level, ModifierStatsBuilder builder) {
        BowAndArrowToolStats.ACCURACY.multiply(builder, 1 + level * 0.1f);
    }
}
