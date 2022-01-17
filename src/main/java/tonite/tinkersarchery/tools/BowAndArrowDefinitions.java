package tonite.tinkersarchery.tools;

import slimeknights.tconstruct.library.tools.ToolDefinition;

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
            .builder(tinkers_arrow)
            .setStatsProvider(BowAndArrowToolStatsProviders.ARROW)
            .build();

    public static final ToolDefinition LEGACY_ARROW = ToolDefinition
            .builder(legacy_arrow)
            .setStatsProvider(BowAndArrowToolStatsProviders.LEGACY_ARROW)
            .build();
}
