package tonite.tinkersarchery.modifiers.traits.replacements.bow;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

public class LightweightModifier extends Modifier {
  public LightweightModifier() {
    super(0x2882d4);
  }

  @Override
  public void addToolStats(ToolRebuildContext context, int level, ModifierStatsBuilder builder) {
    BowAndArrowToolStats.DRAW_SPEED.multiply(builder, 1 + (level * 0.07f));
  }
}
