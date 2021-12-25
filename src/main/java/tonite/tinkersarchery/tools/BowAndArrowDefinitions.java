package tonite.tinkersarchery.tools;

import slimeknights.tconstruct.library.tools.ToolDefinition;
import tonite.tinkersarchery.TinkersArchery;

import static tonite.tinkersarchery.TinkersArchery.*;

public class BowAndArrowDefinitions {
    public static final ToolDefinition SHORTBOW = ToolDefinition
            .builder(shortbow)
            .setStatsProvider(BowAndArrowToolStatsProviders.BOW)
            .build();

    public static final ToolDefinition LONGBOW = ToolDefinition
            .builder(longbow)
            .setStatsProvider(BowAndArrowToolStatsProviders.BOW)
            .build();

    public static final ToolDefinition CROSSBOW = ToolDefinition
            .builder(crossbow)
            .setStatsProvider(BowAndArrowToolStatsProviders.BOW)
            .build();

    public static final ToolDefinition ARROW = ToolDefinition
            .builder(arrow)
            .setStatsProvider(BowAndArrowToolStatsProviders.ARROW)
            .build();
}
