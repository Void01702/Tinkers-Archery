package tonite.tinkersarchery.stats;

import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import tonite.tinkersarchery.TinkersArchery;

public class BowAndArrowToolStats {

    public static final FloatToolStat ELASTICITY = ToolStats.register(new FloatToolStat(name("elasticity"), 0xFF568112, 1, 0, 1024));

    public static final FloatToolStat DRAW_SPEED = ToolStats.register(new FloatToolStat(name("draw_speed"), 0xFFAA0F01, 1, 0.01f, 1024));

    public static final FloatToolStat ACCURACY = ToolStats.register(new FloatToolStat(name("accuracy"), 0xFFFFE858, 1, 0.01f, 1024));

    public static final FloatToolStat SPEED = ToolStats.register(new FloatToolStat(name("speed"), 0xFF0061FF, 1, 0, 1024));

    public static final FloatToolStat WEIGHT = ToolStats.register(new FloatToolStat(name("weight"), 0xFF474747, 1, 0.01f, 1024));

    private static ToolStatId name(String name) {
        return new ToolStatId(TinkersArchery.MOD_ID, name);
    }
}
