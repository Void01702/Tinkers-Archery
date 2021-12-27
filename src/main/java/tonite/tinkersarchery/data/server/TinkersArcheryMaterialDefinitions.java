package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.TinkersArcheryMaterialIds;
import tonite.tinkersarchery.stats.*;

public class TinkersArcheryMaterialDefinitions extends AbstractMaterialDataProvider {
    public static int ORDER_BOW = 4;

    public TinkersArcheryMaterialDefinitions(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {
        addMaterial(TinkersArcheryMaterialIds.tantalum, 2, ORDER_BOW, true,  0xFF9EB9D4);
        addMaterial(TinkersArcheryMaterialIds.cobalt_tantalum, 3, ORDER_BOW, true,  0xFF5079FF);
        addMaterial(TinkersArcheryMaterialIds.galaxy_alloy, 4, ORDER_BOW, true,  0xFF21007F);

        addMaterial(TinkersArcheryMaterialIds.vine, 1, ORDER_BOW, true,  0xFF48B518);
        addMaterial(TinkersArcheryMaterialIds.twisting_vine, 1, ORDER_BOW, true,  0xFF119B85);
        addMaterial(TinkersArcheryMaterialIds.weeping_vine, 1, ORDER_BOW, true,  0xFF7B0000);
        addMaterial(TinkersArcheryMaterialIds.slime, 1, ORDER_BOW, true,  0xFF5BD141);

        addMaterial(TinkersArcheryMaterialIds.feather, 1, ORDER_BOW, true,  0xFFFFFFFF);
        addMaterial(TinkersArcheryMaterialIds.leaf, 1, ORDER_BOW, true,  0xFF4AD718);
        addMaterial(TinkersArcheryMaterialIds.slime_leaf, 1, ORDER_BOW, true,  0xFF36FFFC);
        addMaterial(TinkersArcheryMaterialIds.silky_cloth, 1, ORDER_BOW, true,  0xFFF7CDBB);
    }

    @Override
    public String getName() {
        return "Tinkers' Archery Materials";
    }

    public static class TinkersArcheryMaterialTraitDataProvider extends AbstractMaterialTraitDataProvider {

        public TinkersArcheryMaterialTraitDataProvider(DataGenerator gen, AbstractMaterialDataProvider materials) {
            super(gen, materials);
        }

        @Override
        protected void addMaterialTraits() {
            addDefaultTraits(TinkersArcheryMaterialIds.tantalum, TinkersArchery.ACCURATE_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.cobalt_tantalum, TinkersArchery.WEIGHTY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.galaxy_alloy, TinkersArchery.GROOVY_MODIFIER.get());

            addDefaultTraits(TinkersArcheryMaterialIds.vine, TinkerModifiers.solarPowered.get());
            addDefaultTraits(TinkersArcheryMaterialIds.twisting_vine, TinkersArchery.UPLIFTING_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.weeping_vine, TinkersArchery.SAVAGE_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.slime, TinkersArchery.SUPERSLIME_MODIFIER.get(), TinkerModifiers.overslime.get());

            addDefaultTraits(TinkersArcheryMaterialIds.feather, TinkersArchery.GRAVITY_TRAJECTORY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.leaf, TinkersArchery.TWIRLING_TRAJECTORY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.slime_leaf, TinkersArchery.BOUNCING_TRAJECTORY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.silky_cloth, TinkersArchery.FLYING_TRAJECTORY_MODIFIER.get());
        }

        @Override
        public String getName() {
            return "Tinkers' Archery Material Traits";
        }
    }

    public static class TinkersArcheryMaterialStatsDataProvider extends AbstractMaterialStatsDataProvider {

        public TinkersArcheryMaterialStatsDataProvider(DataGenerator gen, AbstractMaterialDataProvider materials) {
            super(gen, materials);
        }

        @Override
        protected void addMaterialStats() {
            // Melee Harvest
            addMaterialStats(TinkersArcheryMaterialIds.tantalum,
                    new HeadMaterialStats(200, 4.5f, 2, 2f),
                    new HandleMaterialStats(0.8f, 0.9f, 0.8f, 1.3f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(300, 1.2f, 1.1f),
                    new BowGuideMaterialStats(0.9f, 1.3f),
                    new ArrowHeadMaterialStats(200, 4.5f, 2, 2.0f, 0.9f, 1.1f, 1.3f),
                    new ArrowShaftMaterialStats(0.8f, 0.9f, 0.8f, 1.3f, 1.0f, 1.1f, 1.2f));
            addMaterialStats(TinkersArcheryMaterialIds.cobalt_tantalum,
                    new HeadMaterialStats(900, 5.5f, 3, 2.5f),
                    new HandleMaterialStats(1.0f, 0.9f, 1.1f, 1.2f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(1200, 1.25f, 1.35f),
                    new BowGuideMaterialStats(1.0f, 1.5f),
                    new ArrowHeadMaterialStats(900, 5.5f, 3, 2.5f, 1.1f, 1.0f, 1.3f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 1.1f, 1.2f, 1.2f, 1.0f, 1.2f));
            addMaterialStats(TinkersArcheryMaterialIds.galaxy_alloy,
                    new HeadMaterialStats(1000, 6.5f, 4, 2.5f),
                    new HandleMaterialStats(1.1f, 0.9f, 1.1f, 1.1f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(1500, 1.5f, 1.3f),
                    new BowGuideMaterialStats(1.2f, 1.3f),
                    new ArrowHeadMaterialStats(1000, 6.5f, 4, 2.5f, 1.2f, 1.2f, 1.2f),
                    new ArrowShaftMaterialStats(1.1f, 0.9f, 1.1f, 1.1f, 1.2f, 1.2f, 1.2f));

            // Bowstring
            addMaterialStats(TinkersArcheryMaterialIds.vine, new BowStringMaterialStats(1.3f, 0.8f, 1.2f, 0.8f));
            addMaterialStats(TinkersArcheryMaterialIds.twisting_vine, new BowStringMaterialStats(0.7f, 1.2f, 1.1f, 1.1f));
            addMaterialStats(TinkersArcheryMaterialIds.weeping_vine, new BowStringMaterialStats(1.1f, 1.2f, 0.7f, 1.0f));
            addMaterialStats(TinkersArcheryMaterialIds.slime, new BowStringMaterialStats(1.2f, 1.0f, 1.3f, 0.5f));

            // fletching
            addMaterialStats(TinkersArcheryMaterialIds.feather, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.leaf, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.slime_leaf, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.silky_cloth, ArrowFletchingMaterialStats.DEFAULT);
        }

        @Override
        public String getName() {
            return "Tinkers' Archery Material Stats";
        }
    }
}
