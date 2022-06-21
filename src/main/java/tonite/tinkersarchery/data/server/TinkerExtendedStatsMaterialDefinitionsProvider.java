package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
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
        add(MaterialIds.vine);
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
                    new BowMaterialStats(150, 3f, 1f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.0f),
                    new ArrowHeadMaterialStats(16, 2f, WOOD, 0f, 1f, 1.0f 1.2f), //count, rand*2, dmg, weight, stability, accuracy
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f)); //count, rand*2, atk, weigh, stability, accuracy

            addMaterialStats(MaterialIds.stone,
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.0f),
                    new ArrowHeadMaterialStats(10, 4f, STONE, 0.75f, 1.05f, 1.1f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.0f, 0.95f, 1.1f, 1.1f, 1.0f));

            addMaterialStats(MaterialIds.flint,
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.8f, 1.3f),
                    new ArrowHeadMaterialStats(8, 3.5f, STONE, 1f, 1.0f, 1.0f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 1.1f, 1.0f, 1.0f, 1.1f, 1.0f, 1.05f));

            addMaterialStats(MaterialIds.bone,
                    //new BowMaterialStats(100, 3f, 1f),
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.9f, 1.1f),
                    new ArrowHeadMaterialStats(6, 2.5f, STONE, 1.25f, 0.95f, 1.0f, 0.9f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.1f, 1.05f, 0.95f, 1.0f, 0.85f));

            addMaterialStats(MaterialIds.necroticBone,
                    //new BowMaterialStats(125, 3f, 1f),
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.9f, 1.2f),
                    new ArrowHeadMaterialStats(4, 2f, STONE, 1.25f, 0.85f, 1.0f, 0.85f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.15f, 1.15f, 0.95f, 1.0f, 0.75f));

            addMaterialStats(MaterialIds.iron,
                    new BowMaterialStats(250, 3.75f, 0.95f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.0f, 1.0f, 1.2f, 1.2f, 1.0f));
                    new ArrowHeadMaterialStats(15, 6f, IRON, 1.5f, 1.1f, 1f, 1.05f),

            addMaterialStats(MaterialIds.copper,
                    new BowMaterialStats(210, 3.5f, 1.0f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.9f, 1.1f),
                    new ArrowHeadMaterialStats(12, 6.5f, IRON, 1.25f, 1.1f, 1.15f, 1.05f),
                    new ArrowShaftMaterialStats(1.0f, 1.2f, 1.0f, 1.0f, 1.1f, 1.1f, 1.3f));

            addMaterialStats(MaterialIds.searedStone,
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.9f, 1.1f),
                    new ArrowHeadMaterialStats(12, 5f, IRON, 1.75f, 1f, 1f, 0.9f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.0f, 1.15f, 1.1f, 1.1f, 1.1f));

            addMaterialStats(MaterialIds.scorchedStone,
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.1f),
                    new ArrowHeadMaterialStats(9, 4.5f, IRON, 1.5f, 1.0f, 0.95f, 1.1f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.05f, 1.1f, 1.1f, 1.1f, 0.9f));

            addMaterialStats(MaterialIds.slimewood,
                    new BowMaterialStats(375, 3f, 1.1f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.2f, 0.7f),
                    new ArrowShaftMaterialStats(1.0f, 0.85f, 1.0f, 0.85f, 0.9f, 0.9f 0.8f));
                    new ArrowHeadMaterialStats(15, 4f, IRON, 1.25f, 0.9f, 0.95f, 1.0f),

            addMaterialStats(MaterialIds.bloodbone,
                    //new BowMaterialStats(175, 3.5f, 1.0f),
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 0.9f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.1f, 1.05f, 0.85f, 1.15f, 0.9f));
                    new ArrowHeadMaterialStats(9, 4.5f, IRON, 1.75f, 0.95f, 1.05f, 0.85f),

            addMaterialStats(MaterialIds.osmium,
                    new BowMaterialStats(500, 3.5f, 0.9f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.15f, 0.9f),
                    new ArrowHeadMaterialStats(15, 4.5f, IRON, 1.5f, 1.25f, 1.2f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 0.9f, 1.0f, 1.7f, 1.7f, 0.8f));

            addMaterialStats(MaterialIds.tungsten,
                    new BowMaterialStats(350, 3.75f, 0.95f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.1f, 1.0f),
                    new ArrowHeadMaterialStats(12, 6.5f, IRON, 1.25f, 1.2f, 1.2f, 1.1f),
                    new ArrowShaftMaterialStats(1.0f, 1.1f, 0.9f, 1.1f, 1.6f, 1.6f, 1.0f));

            addMaterialStats(MaterialIds.platinum,
                    new BowMaterialStats(400, 3.5f, 0.95f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.15f, 1.0f),
                    new ArrowHeadMaterialStats(15, 7.0f, IRON, 1.25f, 1.2f, 1.1f, 1.15f),
                    new ArrowShaftMaterialStats(1.0f, 1.05f, 0.95f, 1.0f, 1.5f, 1.5f, 0.9f));

            addMaterialStats(MaterialIds.silver,
                    new BowMaterialStats(300, 3.25f, 1.05f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.3f),
                    new ArrowHeadMaterialStats(12, 5.5f, IRON, 1.5f, 1.1f, 1.1f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 1.05f, 1.1f, 1.0f, 1.15f, 1.15f, 1.1f));

            addMaterialStats(MaterialIds.lead,
                    new BowMaterialStats(200, 4f, 0.85f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.1f, 1.0f),
                    new ArrowHeadMaterialStats(12, 5f, IRON, 1.75f, 1.15f, 1.0f, 1.05f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 0.8f, 1.2f, 1.6f, 1.6f, 1.2f));

            addMaterialStats(MaterialIds.whitestone,
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.1f, 1.2f),
                    new ArrowShaftMaterialStats(1.0f, 1.1f, 0.9f, 1.0f, 1.3f, 1.3f, 0.95f));
                    new ArrowHeadMaterialStats(15, 6.0f, IRON, 1.25f, 1.05f, 1.1f, 0.9f),

            addMaterialStats(MaterialIds.slimesteel,
                    new BowMaterialStats(1040, 4f, 1f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.25f, 0.5f),
                    new ArrowHeadMaterialStats(25, 6f, DIAMOND, 2.0f, 1.0f, 0.95f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 0.95f, 1.0f, 0.9f, 0.9f, 0.9f));

            addMaterialStats(MaterialIds.tinkersBronze,
                    new BowMaterialStats(720, 4.25f, 0.95f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.15f, 1.0f),
                    new ArrowHeadMaterialStats(20, 7f, DIAMOND, 1.75f, 1.1f, 1.15f, 1.05f),
                    new ArrowShaftMaterialStats(1.0f, 1.05f, 1.0f, 1.0f, 1.1f, 1.1f, 1.0f));

            addMaterialStats(MaterialIds.nahuatl,
                    new BowMaterialStats(350, 4.25f, 1.0f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(15, 4.5f, DIAMOND, 2.25f, 0.9f, 0.9f, 0.95f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 0.9f, 1.3f, 1.1f, 0.8f, 1.1f));

            addMaterialStats(MaterialIds.pigIron,
                    new BowMaterialStats(580, 4.5f, 0.9f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.1f, 1.1f),
                    new ArrowHeadMaterialStats(21, 6f, DIAMOND, 2.0f, 0.95f, 1.0f, 0.95f),
                    new ArrowShaftMaterialStats(1.0f, 0.85f, 1.0f, 1.1f, 1.3f, 1.3f, 1.1f));

            addMaterialStats(MaterialIds.roseGold,
                    new BowMaterialStats(175, 3f, 1.3f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.9f, 1.2f),
                    new ArrowHeadMaterialStats(10, 10f, IRON, 1.5f, 1.15f, 1.2f, 1.2f),
                    new ArrowShaftMaterialStats(1.0f, 1.25f, 1.25f, 1.0f, 0.7f, 0.7f, 1.6f));

            addMaterialStats(MaterialIds.cobalt,
                    new BowMaterialStats(800, 3.75f, 1.05f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.1f),
                    new ArrowHeadMaterialStats(20, 7.5f, DIAMOND, 2.0f, 1.0f, 0.9f, 1.1f),
                    new ArrowShaftMaterialStats(1.0f, 1.05f, 1.05f, 1.1f, 0.85f, 0.85f, 1.0f));

            addMaterialStats(MaterialIds.steel,
                    new BowMaterialStats(775, 4.25f, 0.95f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(25, 6f, DIAMOND, 2.0f, 1.1f, 1.0f, 1.05f),
                    new ArrowShaftMaterialStats(1.0f, 1.05f, 1.05f, 1.0f, 1.3f, 1.3f, 1.1f));

            addMaterialStats(MaterialIds.bronze,
                    new BowMaterialStats(760, 4.25f, 0.95f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(20, 7f, DIAMOND, 1.75f, 1.1f, 1.15f, 1.05f)),
                    new ArrowShaftMaterialStats(1.0f, 1.05f, 1.0f, 1.0f, 1.05f, 1.05f, 1.25f));

            addMaterialStats(MaterialIds.constantan,
                    new BowMaterialStats(675, 3.75f, 1f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(20, 7.5f, DIAMOND, 1.75f, 1.1f, 1.2f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 1.15f, 1.0f, 1.0f, 1.0f, 1.0f, 1.05f));

            addMaterialStats(MaterialIds.invar,
                    new BowMaterialStats(630, 4f, 1f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.1f, 1f),
                    new ArrowHeadMaterialStats(25, 5.5f, DIAMOND, 2.0f, 1.1f, 1.05f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 1.0f, 1.2f, 1.3f, 1.3f, 0.8f));

            addMaterialStats(MaterialIds.necronium,
                    //new BowMaterialStats(357, 4.0f, 1.0f),
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(15, 4.0f, DIAMOND, 2.25f, 0.85f, 1.05f, 0.8f),
                    new ArrowShaftMaterialStats(1.0f, 1.0f, 1.15f, 1.1f, 0.9f, 1.2f, 0.8f));

            addMaterialStats(MaterialIds.electrum,
                    new BowMaterialStats(225, 3.25f, 1.2f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.1f, 1.0f),
                    new ArrowHeadMaterialStats(15, 9f, IRON, 1.75f, 1.15f, 1.15f, 1.15f),
                    new ArrowShaftMaterialStats(1.0f, 1.15f, 1.15f, 1.0f, 1.2f, 1.2f, 0.9f));

            addMaterialStats(MaterialIds.platedSlimewood,
                    new BowMaterialStats(595, 3.5f, 1.1f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.3f, 0.4f),
                    new ArrowHeadMaterialStats(20, 5.0f, DIAMOND, 1.75f, 0.85f, 0.9f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 0.9f, 1.05f, 0.95f, 0.95f, 0.85f));

            addMaterialStats(MaterialIds.queensSlime,
                    new BowMaterialStats(1650, 4f, 1.1f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.35f, 0.3f),
                    new ArrowHeadMaterialStats(40, 6f, NETHERITE, 2.5f, 1.1f, 0.95f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 0.95f, 0.95f, 1.0f, 1.0f, 0.8f));

            addMaterialStats(MaterialIds.hepatizon,
                    new BowMaterialStats(975, 5f, 0.9f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.95f, 1.1f),
                    new ArrowHeadMaterialStats(40, 8f, NETHERITE, 2.25f, 1.0f, 1.1f, 0.95f),
                    new ArrowShaftMaterialStats(1.0f, 1.2f, 1.0f, 0.9f, 0.95f, 0.95f, 0.9f));

            addMaterialStats(MaterialIds.manyullyn,
                    new BowMaterialStats(1250, 4.5f, 0.95f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.2f, 0.9f),
                    new ArrowHeadMaterialStats(32, 6.5f, NETHERITE, 2.75f, 1.2f, 1.1f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 0.95f, 1.25f, 1.2f, 1.2f, 0.8f));

            addMaterialStats(MaterialIds.blazingBone,
                    //new BowMaterialStats(530, 4.5f, 1f),
                    //new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.1f, 1.2f),
                    new ArrowHeadMaterialStats(18, 6f, IRON, 2.75f, 0.8f, 1.05f, 0.75f),
                    new ArrowShaftMaterialStats(1.0f, 1.05f, 1.2f, 1.0f, 0.65f, 1.35f, 1.0f));

            // Bowstrings

            addMaterialStats(MaterialIds.wood, new BowStringMaterialStats(1.1f, 0.8f, 0.9f, 1.0f));
            addMaterialStats(MaterialIds.string, new BowStringMaterialStats(1.0f, 1.0f, 1.0f, 1.0f));
            addMaterialStats(MaterialIds.vine, new BowStringMaterialStats(0.95f, 0.9f, 1.15f, 1.0f));
            addMaterialStats(MaterialIds.slimewood, new BowStringMaterialStats(1.3f, 0.9f, 1f, 0.85f));
            addMaterialStats(MaterialIds.skyslimeVine, new BowStringMaterialStats(0.95f, 1.2f, 0.9f, 1.0f));
            addMaterialStats(MaterialIds.enderslimeVine, new BowStringMaterialStats(1.1f, 0.9f, 0.9f, 1.3f));

            // Fletchings

            addMaterialStats(MaterialIds.phantom, ArrowFletchingMaterialStats.DEFAULT);

        }

        @Override
        public String getName() {
            return "Tinkers' Archery Extension of Tinker's Construct Material Stats";
        }
    }


}
