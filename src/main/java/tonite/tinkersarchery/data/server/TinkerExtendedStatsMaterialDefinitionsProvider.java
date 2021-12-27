package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.stats.*;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;

public class TinkerExtendedStatsMaterialDefinitionsProvider extends AbstractMaterialDataProvider {

    public TinkerExtendedStatsMaterialDefinitionsProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {
        // Melee Harvest
        add(MaterialIds.wood);
        add(MaterialIds.stone);
        add(MaterialIds.flint);
        add(MaterialIds.bone);
        add(MaterialIds.necroticBone);
        add(MaterialIds.iron);
        add(MaterialIds.copper);
        add(MaterialIds.searedStone);
        add(MaterialIds.scorchedStone);
        add(MaterialIds.slimewood);
        add(MaterialIds.bloodbone);
        add(MaterialIds.osmium);
        add(MaterialIds.tungsten);
        add(MaterialIds.platinum);
        add(MaterialIds.silver);
        add(MaterialIds.lead);
        add(MaterialIds.whitestone);
        add(MaterialIds.slimesteel);
        add(MaterialIds.tinkersBronze);
        add(MaterialIds.nahuatl);
        add(MaterialIds.pigIron);
        add(MaterialIds.roseGold);
        add(MaterialIds.cobalt);
        add(MaterialIds.steel);
        add(MaterialIds.bronze);
        add(MaterialIds.constantan);
        add(MaterialIds.invar);
        add(MaterialIds.necronium);
        add(MaterialIds.electrum);
        add(MaterialIds.platedSlimewood);
        add(MaterialIds.queensSlime);
        add(MaterialIds.hepatizon);
        add(MaterialIds.manyullyn);
        add(MaterialIds.blazingBone);

        // Bowstrings
        add(MaterialIds.string);
        add(MaterialIds.skyslimeVine);
        add(MaterialIds.enderslimeVine);

        // Fletchings
        add(MaterialIds.phantom);

    }

    private void add(MaterialId id) {
        addMaterial (id, 0, 0, true,  0xFFFFFFFF);
    }

    @Override
    public String getName() {
        return "Tinkers' Archery Extension of Tinker's Construct Materials For Stats";
    }

    public static class TinkerExtendedStatsMaterialStatsDataProvider extends AbstractMaterialStatsDataProvider {
        public TinkerExtendedStatsMaterialStatsDataProvider(DataGenerator gen, AbstractMaterialDataProvider materials) {
            super(gen, materials);
        }

        @Override
        protected void addMaterialStats() {
            // Melee Harvest
            addMaterialStats(MaterialIds.wood,
                    new BowMaterialStats(60, 1f, 1f),
                    new BowGuideMaterialStats(1.0f, 1.0f),
                    new ArrowHeadMaterialStats(60, 2f, WOOD, 0f, 1.0f, 0.8f, 0.9f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f));

            addMaterialStats(MaterialIds.stone,
                    new BowMaterialStats(130, 0.9f, 0.8f),
                    new BowGuideMaterialStats(1.0f, 1.0f),
                    new ArrowHeadMaterialStats(130, 4f, STONE, 1f, 1.0f, 1.3f, 0.8f),
                    new ArrowShaftMaterialStats(0.8f, 1.0f, 1.0f, 1.1f, 0.9f, 1.2f, 0.9f));

            addMaterialStats(MaterialIds.flint,
                    new BowMaterialStats(85, 1.1f, 0.8f),
                    new BowGuideMaterialStats(0.8f, 1.3f),
                    new ArrowHeadMaterialStats(85, 3.5f, STONE, 1.25f, 1.0f, 1.0f, 1.0f),
                    new ArrowShaftMaterialStats(0.8f, 1.1f, 1.0f, 1.0f, 0.9f, 1.1f, 0.7f));

            addMaterialStats(MaterialIds.bone,
                    new BowMaterialStats(100, 0.9f, 1.1f),
                    new BowGuideMaterialStats(0.9f, 1.1f),
                    new ArrowHeadMaterialStats(100, 2.5f, STONE, 1.25f, 0.75f, 0.9f, 1.0f),
                    new ArrowShaftMaterialStats(0.75f, 1.0f, 1.1f, 1.0f, 1.0f, 0.9f, 0.9f));

            addMaterialStats(MaterialIds.necroticBone,
                    new BowMaterialStats(125, 0.9f, 1.1f),
                    new BowGuideMaterialStats(0.9f, 1.2f),
                    new ArrowHeadMaterialStats(125, 2f, STONE, 1.5f, 0.75f, 0.9f, 1.0f),
                    new ArrowShaftMaterialStats(0.65f, 1.0f, 1.15f, 1.0f, 1.0f, 0.9f, 0.9f));

            addMaterialStats(MaterialIds.iron,
                    new BowMaterialStats(250, 1.2f, 0.85f),
                    new BowGuideMaterialStats(1.0f, 1.0f),
                    new ArrowHeadMaterialStats(250, 6f, IRON, 2f, 1.1f, 1.3f, 1.0f),
                    new ArrowShaftMaterialStats(1.1f, 1.0f, 1.0f, 1.0f, 1.0f, 1.2f, 1.0f));

            addMaterialStats(MaterialIds.copper,
                    new BowMaterialStats(210, 0.9f, 1.1f),
                    new BowGuideMaterialStats(0.9f, 1.1f),
                    new ArrowHeadMaterialStats(210, 6.5f, IRON, 1.5f, 0.85f, 1f, 1.3f),
                    new ArrowShaftMaterialStats(0.85f, 1.2f, 1.0f, 1.0f, 0.9f, 1.1f, 1.3f));

            addMaterialStats(MaterialIds.searedStone,
                    new BowMaterialStats(225, 0.8f, 0.9f),
                    new BowGuideMaterialStats(1.1f, 1.1f),
                    new ArrowHeadMaterialStats(225, 5f, IRON, 2.25f, 0.9f, 1.25f, 1.1f),
                    new ArrowShaftMaterialStats(0.85f, 1.0f, 1.0f, 1.15f, 0.9f, 1.1f, 1.1f));

            addMaterialStats(MaterialIds.scorchedStone,
                    new BowMaterialStats(120, 0.9f, 1.1f),
                    new BowGuideMaterialStats(1.1f, 1.1f),
                    new ArrowHeadMaterialStats(120, 4.5f, IRON, 2.5f, 1.1f, 1.25f, 0.9f),
                    new ArrowShaftMaterialStats(0.8f, 1.0f, 1.05f, 1.1f, 1.1f, 1.1f, 0.9f));

            addMaterialStats(MaterialIds.slimewood,
                    new BowMaterialStats(375, 1.3f, 1.2f),
                    new BowGuideMaterialStats(1.4f, 0.7f),
                    new ArrowHeadMaterialStats(375, 4f, IRON, 1f, 1.3f, 0.75f, 0.7f),
                    new ArrowShaftMaterialStats(1.3f, 0.85f, 1.0f, 0.85f, 1.2f, 0.9f, 0.8f));

            addMaterialStats(MaterialIds.bloodbone,
                    new BowMaterialStats(175, 1.1f, 1.0f),
                    new BowGuideMaterialStats(1.0f, 0.9f),
                    new ArrowHeadMaterialStats(175, 4.5f, IRON, 2.25f, 1f, 0.8f, 0.9f),
                    new ArrowShaftMaterialStats(0.9f, 1.0f, 1.1f, 1.05f, 1.1f, 0.85f, 0.9f));

            addMaterialStats(MaterialIds.osmium,
                    new BowMaterialStats(500, 1.4f, 0.65f),
                    new BowGuideMaterialStats(1.2f, 0.9f),
                    new ArrowHeadMaterialStats(500, 4.5f, IRON, 2.0f, 1.3f, 1.6f, 0.8f),
                    new ArrowShaftMaterialStats(1.2f, 0.9f, 0.9f, 1.0f, 1.2f, 1.7f, 0.8f));

            addMaterialStats(MaterialIds.tungsten,
                    new BowMaterialStats(350, 1.35f, 0.7f),
                    new BowGuideMaterialStats(1.1f, 1.0f),
                    new ArrowHeadMaterialStats(350, 6.5f, IRON, 1.75f, 1.0f, 1.55f, 1.0f),
                    new ArrowShaftMaterialStats(0.9f, 1.1f, 0.9f, 1.1f, 1.0f, 1.6f, 1.0f));

            addMaterialStats(MaterialIds.platinum,
                    new BowMaterialStats(400, 1.4f, 0.7f),
                    new BowGuideMaterialStats(1.2f, 1.0f),
                    new ArrowHeadMaterialStats(400, 7.0f, IRON, 1.5f, 1.1f, 1.4f, 1.1f),
                    new ArrowShaftMaterialStats(1.05f, 1.05f, 0.95f, 1.0f, 1.0f, 1.5f, 0.9f));

            addMaterialStats(MaterialIds.silver,
                    new BowMaterialStats(300, 1.4f, 0.8f),
                    new BowGuideMaterialStats(1.0f, 1.3f),
                    new ArrowHeadMaterialStats(300, 5.5f, IRON, 2.25f, 0.9f, 1.35f, 1.2f),
                    new ArrowShaftMaterialStats(0.9f, 1.05f, 1.1f, 1.0f, 0.9f, 1.15f, 1.1f));

            addMaterialStats(MaterialIds.lead,
                    new BowMaterialStats(200, 1.3f, 0.7f),
                    new BowGuideMaterialStats(1.1f, 1.0f),
                    new ArrowHeadMaterialStats(200, 5f, IRON, 2.5f, 0.9f, 1.5f, 1.1f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 0.8f, 1.2f, 0.8f, 1.6f, 1.2f));

            addMaterialStats(MaterialIds.whitestone,
                    new BowMaterialStats(275, 1.2f, 0.8f),
                    new BowGuideMaterialStats(1.1f, 1.2f),
                    new ArrowHeadMaterialStats(275, 6.0f, IRON, 1.25f, 1.1f, 1.35f, 0.85f),
                    new ArrowShaftMaterialStats(1.05f, 1.1f, 0.9f, 1.0f, 1f, 1.3f, 0.95f));

            addMaterialStats(MaterialIds.slimesteel,
                    new BowMaterialStats(1040, 1.4f, 1.3f),
                    new BowGuideMaterialStats(1.5f, 0.7f),
                    new ArrowHeadMaterialStats(1040, 6f, DIAMOND, 2.5f, 1.35f, 0.85f, 0.8f),
                    new ArrowShaftMaterialStats(1.2f, 1.0f, 0.95f, 1.0f, 1.3f, 0.9f, 0.9f));

            addMaterialStats(MaterialIds.tinkersBronze,
                    new BowMaterialStats(720, 1.0f, 1.1f),
                    new BowGuideMaterialStats(1.2f, 1.0f),
                    new ArrowHeadMaterialStats(720, 7f, DIAMOND, 2f, 1.25f, 1.0f, 0.9f),
                    new ArrowShaftMaterialStats(1.1f, 1.05f, 1.0f, 1.0f, 1.15f, 1.1f, 1.0f));

            addMaterialStats(MaterialIds.nahuatl,
                    new BowMaterialStats(350, 1.2f, 0.9f),
                    new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(350, 4.5f, DIAMOND, 3f, 1.1f, 0.9f, 1.1f),
                    new ArrowShaftMaterialStats(0.9f, 1.0f, 0.9f, 1.3f, 1.2f, 1.1f, 1.1f));

            addMaterialStats(MaterialIds.pigIron,
                    new BowMaterialStats(580, 1.3f, 0.75f),
                    new BowGuideMaterialStats(1.1f, 1.1f),
                    new ArrowHeadMaterialStats(580, 6f, DIAMOND, 2.5f, 1.2f, 1.35f, 1.2f),
                    new ArrowShaftMaterialStats(1.1f, 0.85f, 1.0f, 1.1f, 1.1f, 1.3f, 1.1f));

            addMaterialStats(MaterialIds.roseGold,
                    new BowMaterialStats(175, 0.8f, 1.3f),
                    new BowGuideMaterialStats(0.9f, 1.2f),
                    new ArrowHeadMaterialStats(175, 10f, IRON, 1f, 1.2f, 0.8f, 1.5f),
                    new ArrowShaftMaterialStats(0.6f, 1.25f, 1.25f, 1.0f, 1.3f, 0.7f, 1.6f));

            addMaterialStats(MaterialIds.cobalt,
                    new BowMaterialStats(800, 0.95f, 1.2f),
                    new BowGuideMaterialStats(1.0f, 1.1f),
                    new ArrowHeadMaterialStats(800, 7.5f, DIAMOND, 2.25f, 1.2f, 0.9f, 1.0f),
                    new ArrowShaftMaterialStats(1.05f, 1.05f, 1.05f, 1.1f, 1.1f, 0.85f, 1.0f));

            addMaterialStats(MaterialIds.steel,
                    new BowMaterialStats(775, 1.25f, 0.8f),
                    new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(775, 6f, DIAMOND, 2.75f, 1.2f, 1.4f, 1.1f),
                    new ArrowShaftMaterialStats(1.05f, 1.05f, 1.05f, 1.0f, 1.1f, 1.3f, 1.1f));

            addMaterialStats(MaterialIds.bronze,
                    new BowMaterialStats(760, 1.1f, 1.0f),
                    new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(760, 7f, DIAMOND, 2f, 0.9f, 0.95f, 1.25f),
                    new ArrowShaftMaterialStats(1.1f, 1.05f, 1.0f, 1.0f, 1.0f, 1.05f, 1.25f));

            addMaterialStats(MaterialIds.constantan,
                    new BowMaterialStats(675, 1.1f, 0.8f),
                    new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(675, 7.5f, DIAMOND, 1.75f, 1.0f, 0.9f, 1.15f),
                    new ArrowShaftMaterialStats(0.95f, 1.15f, 1.0f, 1.0f, 1.2f, 1.0f, 1.05f));

            addMaterialStats(MaterialIds.invar,
                    new BowMaterialStats(630, 1.4f, 0.75f),
                    new BowGuideMaterialStats(1.1f, 1f),
                    new ArrowHeadMaterialStats(630, 5.5f, DIAMOND, 2.5f, 1.1f, 1.4f, 0.9f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 1.0f, 1.2f, 1.0f, 1.3f, 0.8f));

            addMaterialStats(MaterialIds.necronium,
                    new BowMaterialStats(357, 1.0f, 1.0f),
                    new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(357, 4.0f, DIAMOND, 2.75f, 0.8f, 1.0f, 0.9f),
                    new ArrowShaftMaterialStats(0.8f, 1.0f, 1.15f, 1.1f, 1.1f, 0.9f, 0.8f));

            addMaterialStats(MaterialIds.electrum,
                    new BowMaterialStats(225, 1.2f, 0.7f),
                    new BowGuideMaterialStats(1.1f, 1.0f),
                    new ArrowHeadMaterialStats(225, 9f, IRON, 1.5f, 1.2f, 1.15f, 0.9f),
                    new ArrowShaftMaterialStats(0.8f, 1.15f, 1.15f, 1.0f, 1.2f, 1.2f, 0.9f));

            addMaterialStats(MaterialIds.platedSlimewood,
                    new BowMaterialStats(595, 1.4f, 1.1f),
                    new BowGuideMaterialStats(1.5f, 0.65f),
                    new ArrowHeadMaterialStats(595, 5.0f, DIAMOND, 2.0f, 1.4f, 0.85f, 0.8f),
                    new ArrowShaftMaterialStats(1.25f, 0.9f, 0.9f, 1.05f, 1.3f, 0.95f, 0.85f));

            addMaterialStats(MaterialIds.queensSlime,
                    new BowMaterialStats(1650, 1.6f, 1.1f),
                    new BowGuideMaterialStats(1.6f, 0.6f),
                    new ArrowHeadMaterialStats(1650, 6f, NETHERITE, 2f, 1.45f, 0.9f, 0.65f),
                    new ArrowShaftMaterialStats(1.35f, 0.9f, 0.95f, 0.95f, 1.4f, 1.0f, 0.8f));

            addMaterialStats(MaterialIds.hepatizon,
                    new BowMaterialStats(975, 0.95f, 0.8f),
                    new BowGuideMaterialStats(0.95f, 1.1f),
                    new ArrowHeadMaterialStats(975, 8f, NETHERITE, 2.5f, 1.3f, 0.8f, 1.0f),
                    new ArrowShaftMaterialStats(1.1f, 1.2f, 1.0f, 0.9f, 1.2f, 0.95f, 0.9f));

            addMaterialStats(MaterialIds.manyullyn,
                    new BowMaterialStats(1250, 1.2f, 1.1f),
                    new BowGuideMaterialStats(1.2f, 1.2f),
                    new ArrowHeadMaterialStats(1250, 6.5f, NETHERITE, 3.5f, 1.3f, 1.25f, 0.7f),
                    new ArrowShaftMaterialStats(1.1f, 0.9f, 0.95f, 1.25f, 1.2f, 1.2f, 0.8f));

            addMaterialStats(MaterialIds.blazingBone,
                    new BowMaterialStats(530, 0.9f, 1.3f),
                    new BowGuideMaterialStats(1.1f, 1.2f),
                    new ArrowHeadMaterialStats(530, 6f, IRON, 3f, 1.3f, 0.65f, 1.1f),
                    new ArrowShaftMaterialStats(0.85f, 1.05f, 1.2f, 1.0f, 1.4f, 0.65f, 1.0f));

            // Bowstrings

            addMaterialStats(MaterialIds.wood, new BowStringMaterialStats(1.3f, 0.8f, 0.8f, 1.2f));
            addMaterialStats(MaterialIds.string, new BowStringMaterialStats(1.0f, 1.0f, 1.0f, 1.0f));
            addMaterialStats(MaterialIds.skyslimeVine, new BowStringMaterialStats(1.1f, 1.0f, 0.9f, 1.2f));
            addMaterialStats(MaterialIds.enderslimeVine, new BowStringMaterialStats(1.1f, 1.2f, 1.1f, 0.7f));

            // Fletchings

            addMaterialStats(MaterialIds.phantom, ArrowFletchingMaterialStats.DEFAULT);

        }

        @Override
        public String getName() {
            return "Tinkers' Archery Extension of Tinker's Construct Material Stats";
        }
    }


}
