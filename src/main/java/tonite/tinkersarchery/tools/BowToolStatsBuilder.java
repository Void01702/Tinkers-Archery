package tonite.tinkersarchery.tools;

import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionData;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.tools.stat.ToolStatsBuilder;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;
import tonite.tinkersarchery.stats.BowGuideMaterialStats;
import tonite.tinkersarchery.stats.BowMaterialStats;
import tonite.tinkersarchery.stats.BowStringMaterialStats;

import java.util.List;

public class BowToolStatsBuilder extends ToolStatsBuilder {
    private final List<BowMaterialStats> bowshafts;
    private final List<BowStringMaterialStats> bowstrings;
    private final List<BowGuideMaterialStats> bowguides;

    protected BowToolStatsBuilder(ToolDefinitionData baseStats, List<BowMaterialStats> bowshafts, List<BowStringMaterialStats> bowstrings, List<BowGuideMaterialStats> extras) {
        super(baseStats);
        this.bowshafts = bowshafts;
        this.bowstrings = bowstrings;
        this.bowguides = extras;
    }

    public static ToolStatsBuilder from(ToolDefinition toolDefinition, List<IMaterial> materials) {
        ToolDefinitionData data = toolDefinition.getData();
        List<PartRequirement> requiredComponents = data.getParts();
        // if the NBT is invalid, at least we can return the default stats builder, as an exception here could kill itemstacks
        if (materials.size() != requiredComponents.size()) {
            return ToolStatsBuilder.noParts(toolDefinition);
        }

        return new BowToolStatsBuilder(data, listOfCompatibleWith(BowMaterialStats.ID, materials, requiredComponents),
                listOfCompatibleWith(BowStringMaterialStats.ID, materials, requiredComponents),
                listOfCompatibleWith(BowGuideMaterialStats.ID, materials, requiredComponents)
        );
    }

    @Override
    protected void setStats(StatsNBT.Builder builder) {
        // add in specific stat types handled by our materials
        builder.set(ToolStats.DURABILITY, buildDurability());
        builder.set(BowAndArrowToolStats.ELASTICITY, buildElasticity());
        builder.set(BowAndArrowToolStats.DRAW_SPEED, buildDrawSpeed());
        builder.set(BowAndArrowToolStats.ACCURACY, buildAccuracy());
    }

    @Override
    protected boolean handles(IToolStat<?> stat) {
        return stat == ToolStats.DURABILITY || stat == BowAndArrowToolStats.ELASTICITY
                || stat == BowAndArrowToolStats.DRAW_SPEED || stat == BowAndArrowToolStats.ACCURACY;
    }

    public float buildDurability() {
        double averageBowshaftDurability = getAverageValue(bowshafts, BowMaterialStats::getDurability) + toolData.getBonus(ToolStats.DURABILITY);
        double averageBowstringModifier = getAverageValue(bowstrings, BowStringMaterialStats::getDurability, 1);
        // durability should never be below 1
        return Math.max(1, (int)(averageBowshaftDurability * averageBowstringModifier));
    }

    public float buildElasticity() {
        double averageBowshaftElasticity = getAverageValue(bowshafts, BowMaterialStats::getElasticity) + toolData.getBonus(BowAndArrowToolStats.ELASTICITY);
        double averageBowstringElasticity = getAverageValue(bowstrings, BowStringMaterialStats::getElasticity, 1);
        //double averageBowguideElasticity = getAverageValue(bowguides, BowGuideMaterialStats::getElasticity, 1);

        return (float)Math.max(0.1d, averageBowshaftElasticity * averageBowstringElasticity);
    }

    public float buildDrawSpeed() {
        double averageBowshaftDrawSpeed = getAverageValue(bowshafts, BowMaterialStats::getDrawSpeed) + toolData.getBonus(BowAndArrowToolStats.DRAW_SPEED);
        double averageBowstringDrawSpeed = getAverageValue(bowstrings, BowStringMaterialStats::getDrawSpeed, 1);

        return (float)Math.max(0.1d, averageBowshaftDrawSpeed * averageBowstringDrawSpeed);
    }

    public float buildAccuracy() {
        //double averageBowguideAccuracy = getAverageValue(bowguides, BowGuideMaterialStats::getAccuracy) + toolData.getBonus(BowAndArrowToolStats.ACCURACY);
        double averageBowstringAccuracy = getAverageValue(bowstrings, BowStringMaterialStats::getAccuracy, 1);

        return (float)Math.max(0.1d, averageBowstringAccuracy);
    }
}
