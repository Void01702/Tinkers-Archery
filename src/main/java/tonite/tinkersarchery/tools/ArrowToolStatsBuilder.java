package tonite.tinkersarchery.tools;

import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.tools.ToolBaseStatDefinition;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionData;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.tools.stat.ToolStatsBuilder;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import tonite.tinkersarchery.stats.*;

import java.util.List;

public class ArrowToolStatsBuilder extends ToolStatsBuilder {
    private final List<ArrowHeadMaterialStats> arrowheads;
    private final List<ArrowShaftMaterialStats> arrowshafts;
    private final List<ArrowFletchingMaterialStats> arrowfletchings;

    protected ArrowToolStatsBuilder(ToolDefinitionData baseStats, List<ArrowHeadMaterialStats> arrowheads, List<ArrowShaftMaterialStats> arrowshafts, List<ArrowFletchingMaterialStats> arrowfletchings) {
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

        return new ArrowToolStatsBuilder(data, listOfCompatibleWith(ArrowHeadMaterialStats.ID, materials, requiredComponents),
                listOfCompatibleWith(ArrowShaftMaterialStats.ID, materials, requiredComponents),
                listOfCompatibleWith(ArrowFletchingMaterialStats.ID, materials, requiredComponents)
        );
    }

    @Override
    protected void setStats(StatsNBT.Builder builder) {
        // add in specific stat types handled by our materials
        builder.set(ToolStats.DURABILITY, buildDurability());
        builder.set(ToolStats.HARVEST_LEVEL, buildHarvestLevel());
        builder.set(ToolStats.ATTACK_DAMAGE, buildAttackDamage());
        builder.set(ToolStats.ATTACK_SPEED, buildAttackSpeed());
        builder.set(ToolStats.MINING_SPEED, buildMiningSpeed());
        builder.set(BowAndArrowToolStats.SPEED, buildSpeed());
        builder.set(BowAndArrowToolStats.WEIGHT, buildWeight());
        builder.set(BowAndArrowToolStats.ACCURACY, buildAccuracy());
    }

    @Override
    protected boolean handles(IToolStat<?> stat) {
        return stat == ToolStats.DURABILITY || stat == ToolStats.HARVEST_LEVEL
                || stat == ToolStats.ATTACK_DAMAGE || stat == ToolStats.ATTACK_SPEED || stat == ToolStats.MINING_SPEED
                || stat == BowAndArrowToolStats.SPEED || stat == BowAndArrowToolStats.WEIGHT || stat == BowAndArrowToolStats.ELASTICITY;
    }


    /** Builds durability for the tool */
    public float buildDurability() {
        double averageHeadDurability = getAverageValue(arrowheads, HeadMaterialStats::getDurability) + toolData.getBonus(ToolStats.DURABILITY);
        double averageHandleModifier = getAverageValue(arrowshafts, HandleMaterialStats::getDurability, 1);
        // durability should never be below 1
        return Math.max(1, (int)(averageHeadDurability * averageHandleModifier));
    }

    /** Builds mining speed for the tool */
    public float buildMiningSpeed() {
        double averageHeadSpeed = getAverageValue(arrowheads, HeadMaterialStats::getMiningSpeed) + toolData.getBonus(ToolStats.MINING_SPEED);
        double averageHandleModifier = getAverageValue(arrowshafts, HandleMaterialStats::getMiningSpeed, 1);

        return (float)Math.max(0.1d, averageHeadSpeed * averageHandleModifier);
    }

    /** Builds attack speed for the tool */
    public float buildAttackSpeed() {
        float baseSpeed = ToolStats.ATTACK_SPEED.getDefaultValue() + toolData.getBonus(ToolStats.ATTACK_SPEED);
        double averageHandleModifier = getAverageValue(arrowshafts, HandleMaterialStats::getAttackSpeed, 1);
        return (float)Math.max(0, baseSpeed * averageHandleModifier);
    }

    /** Builds the harvest level for the tool */
    public int buildHarvestLevel() {
        return arrowheads.stream()
                .mapToInt(HeadMaterialStats::getHarvestLevel)
                .max()
                .orElse(0);
    }

    /** Builds attack damage for the tool */
    public float buildAttackDamage() {
        double averageHeadAttack = getAverageValue(arrowheads, HeadMaterialStats::getAttack) + toolData.getBonus(ToolStats.ATTACK_DAMAGE);
        double averageHandle = getAverageValue(arrowshafts, HandleMaterialStats::getAttackDamage, 1.0f);
        return (float)Math.max(0.0d, averageHeadAttack * averageHandle);
    }

    public float buildSpeed() {
        double averageArrowheadSpeed = getAverageValue(arrowheads, ArrowHeadMaterialStats::getSpeed) + toolData.getBonus(BowAndArrowToolStats.SPEED);
        double averageArrowShaftSpeed = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getSpeed, 1);

        return (float)Math.max(0.1d, averageArrowheadSpeed * averageArrowShaftSpeed);
    }

    public float buildWeight() {
        double averageArrowheadWeight = getAverageValue(arrowheads, ArrowHeadMaterialStats::getWeight) + toolData.getBonus(BowAndArrowToolStats.WEIGHT);
        double averageArrowShaftWeight = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getWeight, 1);

        return (float)Math.max(0.1d, averageArrowheadWeight * averageArrowShaftWeight);
    }

    public float buildAccuracy() {
        double averageArrowheadAccuracy = getAverageValue(arrowheads, ArrowHeadMaterialStats::getAccuracy) + toolData.getBonus(BowAndArrowToolStats.ACCURACY);
        double averageArrowShaftAccuracy = getAverageValue(arrowshafts, ArrowShaftMaterialStats::getAccuracy, 1);

        return (float)Math.max(0.1d, averageArrowheadAccuracy * averageArrowShaftAccuracy);
    }
}

