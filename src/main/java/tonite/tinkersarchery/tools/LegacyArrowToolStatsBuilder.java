package tonite.tinkersarchery.tools;

import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionData;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.tools.stat.ToolStatsBuilder;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.ArrowHeadMaterialStats;
import tonite.tinkersarchery.stats.ArrowShaftMaterialStats;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import java.util.List;

public class LegacyArrowToolStatsBuilder extends ToolStatsBuilder {
    private final List<ArrowHeadMaterialStats> arrowheads;
    private final List<ArrowShaftMaterialStats> arrowshafts;
    private final List<ArrowFletchingMaterialStats> arrowfletchings;

    protected LegacyArrowToolStatsBuilder(ToolDefinitionData baseStats, List<ArrowHeadMaterialStats> arrowheads, List<ArrowShaftMaterialStats> arrowshafts, List<ArrowFletchingMaterialStats> arrowfletchings) {
        super(baseStats);
        this.arrowheads = arrowheads;
        this.arrowshafts = arrowshafts;
        this.arrowfletchings = arrowfletchings;
    }

    public static ToolStatsBuilder from(ToolDefinition toolDefinition, List<IMaterial> materials) {
        ToolDefinitionData data = toolDefinition.getData();
        List<PartRequirement> requiredComponents = data.getParts();
        // if the NBT is invalid, at least we can return the default stats builder, as an exception here could kill itemstacks
        if (materials.size() != requiredComponents.size()) {
            return ToolStatsBuilder.noParts(toolDefinition);
        }

        return new LegacyArrowToolStatsBuilder(data, listOfCompatibleWith(ArrowHeadMaterialStats.ID, materials, requiredComponents),
                listOfCompatibleWith(ArrowShaftMaterialStats.ID, materials, requiredComponents),
                listOfCompatibleWith(ArrowFletchingMaterialStats.ID, materials, requiredComponents)
        );
    }

    @Override
    protected void setStats(StatsNBT.Builder builder) {
        // add in specific stat types handled by our materials
        builder.set(ToolStats.DURABILITY, buildCount());
        builder.set(ToolStats.HARVEST_LEVEL, buildHarvestLevel());
        builder.set(ToolStats.ATTACK_DAMAGE, buildAttackDamage());
        builder.set(ToolStats.ATTACK_SPEED, buildAttackSpeed());
        builder.set(ToolStats.MINING_SPEED, buildMiningSpeed());
        builder.set(BowAndArrowToolStats.WEIGHT, buildWeight());
        builder.set(BowAndArrowToolStats.STABILITY, buildStability());
        builder.set(BowAndArrowToolStats.ACCURACY, buildAccuracy());
    }

    @Override
    protected boolean handles(IToolStat<?> stat) {
        return stat == ToolStats.DURABILITY || stat == ToolStats.HARVEST_LEVEL
                || stat == ToolStats.ATTACK_DAMAGE || stat == ToolStats.ATTACK_SPEED || stat == ToolStats.MINING_SPEED
                || stat == BowAndArrowToolStats.WEIGHT || stat == BowAndArrowToolStats.ELASTICITY;
    }


    /** Builds durability for the tool */
    public float buildCount() {
        double averageHeadCount = getAverageValue(arrowheads, ArrowHeadMaterialStats::getCount) + toolData.getBonus(BowAndArrowToolStats.COUNT);
        double averageHandleCount = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getCount, 1);
        // durability should never be below 1
        return Math.max(1, (int)(averageHeadCount * averageHandleCount));
    }

    /** Builds mining speed for the tool */
    public float buildMiningSpeed() {
        double averageHeadSpeed = getAverageValue(arrowheads, ArrowHeadMaterialStats::getMiningSpeed) + toolData.getBonus(ToolStats.MINING_SPEED);
        double averageHandleModifier = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getMiningSpeed, 1);

        return (float)Math.max(0.1d, averageHeadSpeed * averageHandleModifier);
    }

    /** Builds attack speed for the tool */
    public float buildAttackSpeed() {
        float baseSpeed = ToolStats.ATTACK_SPEED.getDefaultValue() + toolData.getBonus(ToolStats.ATTACK_SPEED);
        double averageHandleModifier = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getAttackSpeed, 1);
        return (float)Math.max(0, baseSpeed * averageHandleModifier);
    }

    /** Builds the harvest level for the tool */
    public int buildHarvestLevel() {
        return arrowheads.stream()
                .mapToInt(ArrowHeadMaterialStats::getHarvestLevel)
                .max()
                .orElse(0);
    }

    /** Builds attack damage for the tool */
    public float buildAttackDamage() {
        double averageHeadAttack = getAverageValue(arrowheads, ArrowHeadMaterialStats::getAttack) + toolData.getBonus(ToolStats.ATTACK_DAMAGE);
        double averageHandle = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getAttackDamage, 1.0f);
        return (float)Math.max(0.0d, averageHeadAttack * averageHandle);
    }

    public float buildWeight() {
        double averageArrowheadWeight = getAverageValue(arrowheads, ArrowHeadMaterialStats::getWeight) + toolData.getBonus(BowAndArrowToolStats.WEIGHT);
        double averageArrowShaftWeight = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getWeight, 1);

        return (float)Math.max(0.1d, averageArrowheadWeight * averageArrowShaftWeight);
    }

    public float buildStability() {
        double averageArrowheadStability = getAverageValue(arrowheads, ArrowHeadMaterialStats::getStability) + toolData.getBonus(BowAndArrowToolStats.STABILITY);
        double averageArrowShaftStability = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getStability, 1);

        return (float)Math.max(0.1d, averageArrowheadStability * averageArrowShaftStability);
    }

    public float buildAccuracy() {
        double averageArrowheadAccuracy = getAverageValue(arrowheads, ArrowHeadMaterialStats::getAccuracy) + toolData.getBonus(BowAndArrowToolStats.ACCURACY);
        double averageArrowShaftAccuracy = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getAccuracy, 1);

        return (float)Math.max(0.1d, averageArrowheadAccuracy * averageArrowShaftAccuracy);
    }
}

