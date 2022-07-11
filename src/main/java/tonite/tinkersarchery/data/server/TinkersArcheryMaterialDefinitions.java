package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
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
        addMaterial(TinkersArcheryMaterialIds.tantalum, 2, ORDER_BOW, false,  0x9EB9D4);
        addMaterial(TinkersArcheryMaterialIds.tungstantalum, 3, ORDER_BOW, false,  0x9AA29B, false, new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS,  new NotCondition(new TagEmptyCondition("forge","ingots/tungsten"))));
        addMaterial(TinkersArcheryMaterialIds.luxtum, 3, ORDER_BOW + ORDER_NETHER, false,  0xF9D875);
        addMaterial(TinkersArcheryMaterialIds.cobalt_tantalum, 4, ORDER_BOW + ORDER_NETHER, false,  0x5079FF);
        addMaterial(TinkersArcheryMaterialIds.galaxy_alloy, 4, ORDER_BOW + ORDER_NETHER, false,  0x21007F);

        //addMaterial(TinkersArcheryMaterialIds.slime, 2, ORDER_BOW + ORDER_BINDING, true,  0x5BD141);
        addMaterial(TinkersArcheryMaterialIds.silky_cloth, 3, ORDER_BOW + ORDER_BINDING, true,  0xF7CDBB);
        addMaterial(TinkersArcheryMaterialIds.blazing_string, 4, ORDER_BOW + ORDER_BINDING + ORDER_NETHER, false,  0xFFC42E);

        addMaterial(TinkersArcheryMaterialIds.steel_wire, 3, ORDER_BOW + ORDER_COMPAT + ORDER_BINDING, true,  0x393D3D, false, new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS, new NotCondition(new TagEmptyCondition("forge:wires/steel")), new NotCondition(new TagEmptyCondition("forge:ingots/steel"))));

        addMaterial(TinkersArcheryMaterialIds.feather, 1, ORDER_BOW, true,  0xFFFFFF);
        addMaterial(TinkersArcheryMaterialIds.paper, 1, ORDER_BOW, true,  0xFFFFFF);
        addMaterial(TinkersArcheryMaterialIds.leaf, 1, ORDER_BOW, true,  0x4AD718);
        addMaterial(TinkersArcheryMaterialIds.slime_leaf, 1, ORDER_BOW, true,  0x36FFFC);
        addMaterial(TinkersArcheryMaterialIds.bamboo, 1, ORDER_BOW + 1, true,  0xFF568112);
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
            addDefaultTraits(TinkersArcheryMaterialIds.tantalum, TinkersArchery.FINISHING_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.tantalum, BowMaterialStats.ID, TinkersArchery.ACCURATE_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.tantalum, BowGuideMaterialStats.ID, TinkersArchery.ACCURATE_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.tungstantalum, TinkersArchery.SWIFTSTRIKE_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.tungstantalum, BowMaterialStats.ID, TinkersArchery.STABLE_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.tungstantalum, BowGuideMaterialStats.ID, TinkersArchery.STABLE_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.luxtum, TinkerModifiers.lightspeed.get());
            addTraits(TinkersArcheryMaterialIds.luxtum, BowMaterialStats.ID, TinkersArchery.ENLIGHTENING_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.luxtum, BowGuideMaterialStats.ID, TinkersArchery.ENLIGHTENING_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.cobalt_tantalum, TinkersArchery.SWIFTSTRIKE_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.cobalt_tantalum, BowMaterialStats.ID, TinkersArchery.STABLE_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.cobalt_tantalum, BowGuideMaterialStats.ID, TinkersArchery.STABLE_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.galaxy_alloy, TinkersArchery.CHAINING_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.galaxy_alloy, BowMaterialStats.ID, TinkersArchery.GROOVY_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.galaxy_alloy, BowGuideMaterialStats.ID, TinkersArchery.GROOVY_MODIFIER.get());

            //addDefaultTraits(TinkersArcheryMaterialIds.slime, TinkersArchery.SUPERSLIME_MODIFIER.get(), TinkerModifiers.overslime.get());
            addDefaultTraits(TinkersArcheryMaterialIds.silky_cloth, TinkersArchery.UPLIFTING_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.blazing_string, TinkersArchery.BLAZING_MODIFIER.get());

            addDefaultTraits(TinkersArcheryMaterialIds.steel_wire, TinkersArchery.SLICING_MODIFIER.get());

            addDefaultTraits(TinkersArcheryMaterialIds.feather, TinkersArchery.GRAVITY_TRAJECTORY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.paper, TinkersArchery.LOOPING_TRAJECTORY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.leaf, TinkersArchery.TWIRLING_TRAJECTORY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.slime_leaf, TinkersArchery.BOUNCING_TRAJECTORY_MODIFIER.get());
            addTraits(TinkersArcheryMaterialIds.silky_cloth, ArrowFletchingMaterialStats.ID, TinkersArchery.FLYING_TRAJECTORY_MODIFIER.get());
            addDefaultTraits(TinkersArcheryMaterialIds.bamboo, TinkersArchery.BOOMERANGING_TRAJECTORY_MODIFIER.get());
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
                    new HeadMaterialStats(230, 4.5f, 2, 2f),
                    new HandleMaterialStats(0.8f, 0.9f, 0.8f, 1.3f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(230, 4.0f, 0.9f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(0.9f, 1.4f),
                    new ArrowHeadMaterialStats(9, 4.5f, 2, 2.75f, 1.15f, 1.05f, 1.05f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 0.8f, 1.15f, 1.15f, 1.05f, 1.05f));
            addMaterialStats(TinkersArcheryMaterialIds.tungstantalum,
                    new HeadMaterialStats(680, 5.5f, 3, 2.5f),
                    new HandleMaterialStats(1.0f, 0.9f, 1.1f, 1.2f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(680, 4.75f, 0.85f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(23, 5.5f, 3, 3.5f, 1.0f, 1.3f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 1.1f, 1.2f, 1.0f, 1.2f));
            addMaterialStats(TinkersArcheryMaterialIds.luxtum,
                    new HeadMaterialStats(590, 5.5f, 3, 2.5f),
                    new HandleMaterialStats(1.0f, 0.9f, 1.1f, 1.2f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(590, 3.5f, 1.15f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(23, 5.5f, 3, 3.5f, 1.0f, 1.3f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 1.1f, 1.2f, 1.0f, 1.2f));
            addMaterialStats(TinkersArcheryMaterialIds.cobalt_tantalum,
                    new HeadMaterialStats(1040, 5.5f, 3, 2.5f),
                    new HandleMaterialStats(1.0f, 0.9f, 1.1f, 1.2f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(1040, 5.25f, 0.85f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.2f),
                    new ArrowHeadMaterialStats(32, 5.5f, 3, 3.75f, 1.0f, 0.85f, 1.05f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 1.1f, 1.15f, 1.0f, 0.9f, 1.05f));
            addMaterialStats(TinkersArcheryMaterialIds.galaxy_alloy,
                    new HeadMaterialStats(830, 6.5f, 4, 2.5f),
                    new HandleMaterialStats(1.1f, 0.9f, 1.1f, 1.1f),
                    ExtraMaterialStats.DEFAULT,
                    new BowMaterialStats(830, 4f, 1.15f),
                    new BowGuideMaterialStats(),//new BowGuideMaterialStats(1.0f, 1.1f),
                    new ArrowHeadMaterialStats(24, 6.5f, 4, 3.25f, 0.8f, 0.95f, 1.0f),
                    new ArrowShaftMaterialStats(1.0f, 0.9f, 1.1f, 0.9f, 0.8f, 1.0f, 1.0f));

            // Bowstring
            //addMaterialStats(TinkersArcheryMaterialIds.slime, new BowStringMaterialStats(1.2f, 0.8f, 1.2f, 0.5f));
            addMaterialStats(TinkersArcheryMaterialIds.silky_cloth, new BowStringMaterialStats(0.6f, 1.0f, 1.25f, 1.25f));
            addMaterialStats(TinkersArcheryMaterialIds.blazing_string, new BowStringMaterialStats(0.85f, 1.05f, 1.2f, 1.0f));
            addMaterialStats(TinkersArcheryMaterialIds.steel_wire, new BowStringMaterialStats(1.15f, 1.3f, 0.7f, 1.0f));

            // fletching
            addMaterialStats(TinkersArcheryMaterialIds.feather, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.paper, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.leaf, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.slime_leaf, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.silky_cloth, ArrowFletchingMaterialStats.DEFAULT);
            addMaterialStats(TinkersArcheryMaterialIds.bamboo, ArrowFletchingMaterialStats.DEFAULT);
        }

        @Override
        public String getName() {
            return "Tinkers' Archery Material Stats";
        }
    }
}
