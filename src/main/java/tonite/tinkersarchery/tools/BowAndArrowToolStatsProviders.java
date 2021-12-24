package tonite.tinkersarchery.tools;

import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.IToolStatProvider;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionData;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.tools.MeleeHarvestToolStatsBuilder;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import tonite.tinkersarchery.stats.*;

import java.util.List;

public class BowAndArrowToolStatsProviders {

    public static final IToolStatProvider BOW = new IToolStatProvider() {
        @Override
        public StatsNBT buildStats(ToolDefinition definition, List<IMaterial> materials) {
            return BowToolStatsBuilder.from(definition, materials).buildStats();
        }

        @Override
        public boolean isMultipart() {
            return true;
        }

        @Override
        public void validate(ToolDefinitionData data) {
            List<PartRequirement> requirements = data.getParts();
            if (requirements.isEmpty()) {
                throw new IllegalStateException("Must have at least one tool part for a melee/harvest tool");
            }
            boolean foundHead = false;
            for (PartRequirement req : requirements) {
                MaterialStatsId statType = req.getStatType();
                if (statType.equals(BowMaterialStats.ID)) {
                    foundHead = true;
                } else if (!statType.equals(BowStringMaterialStats.ID) && !statType.equals(BowGuideMaterialStats.ID)) {
                    throw new IllegalStateException("Invalid melee/harvest tool part type, only support head, handle, and extra part types");
                }
            }
            if (!foundHead) {
                throw new IllegalStateException("Melee/harvest tool must use at least one head part");
            }
        }
    };

    public static final IToolStatProvider ARROW = new IToolStatProvider() {
        @Override
        public StatsNBT buildStats(ToolDefinition definition, List<IMaterial> materials) {
            return ArrowToolStatsBuilder.from(definition, materials).buildStats();
        }

        @Override
        public boolean isMultipart() {
            return true;
        }

        @Override
        public void validate(ToolDefinitionData data) {
            List<PartRequirement> requirements = data.getParts();
            if (requirements.isEmpty()) {
                throw new IllegalStateException("Must have at least one tool part for a arrow tool");
            }
            boolean foundHead = false;
            boolean foundFletching = false;
            for (PartRequirement req : requirements) {
                MaterialStatsId statType = req.getStatType();
                if (statType.equals(ArrowHeadMaterialStats.ID)) {
                    foundHead = true;
                } else if (statType.equals(ArrowFletchingMaterialStats.ID)) {
                    foundFletching = true;
                } else if (!statType.equals(ArrowShaftMaterialStats.ID)) {
                    throw new IllegalStateException("Invalid arrow tool part type, only support arrowheads, arrowshafts, and arrow fletching part types");
                }
            }
            if (!foundHead || !foundFletching) {
                throw new IllegalStateException("Arrow tool must use at least one head and fletching part");
            }
        }
    };
}
