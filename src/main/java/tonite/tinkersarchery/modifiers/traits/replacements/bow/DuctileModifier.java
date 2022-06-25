package tonite.tinkersarchery.modifiers.traits.replacements.bow;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

public class DuctileModifier extends Modifier {
  public DuctileModifier() {
    super(0x959595);
  }

  @Override
  public void addToolStats(ToolRebuildContext context, int level, ModifierStatsBuilder builder) {
    ToolStats.DURABILITY.multiply(builder, 1 + (level * 0.04f));
    BowAndArrowToolStats.ELASTICITY.multiply(builder, 1 + (level * 0.04f));
  }
}
