package tonite.tinkersarchery.data.client;

import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.BowGuideMaterialStats;
import tonite.tinkersarchery.stats.BowMaterialStats;
import tonite.tinkersarchery.stats.BowStringMaterialStats;

public class TinkerExtendedMaterialSpriteProvider extends TinkerMaterialSpriteProvider {

    @Override
    protected void addAllMaterials() {
        super.addAllMaterials();

        buildMaterial(MaterialIds.wood)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.stone)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.flint)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.bone)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.necroticBone)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.iron)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.copper)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.searedStone)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.bloodbone)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.slimewood)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.osmium)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.platinum)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.tungsten)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.lead)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.silver)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.whitestone)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.scorchedStone)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.slimesteel)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.tinkersBronze)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.nahuatl)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.pigIron)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.roseGold)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.steel)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.bronze)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.constantan)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.invar)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.necronium)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.electrum)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.platedSlimewood)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.queensSlime)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.hepatizon)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.manyullyn)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);
        buildMaterial(MaterialIds.blazingBone)
                .statType(BowMaterialStats.ID)
                .statType(BowGuideMaterialStats.ID);

        buildMaterial(MaterialIds.wood)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.string)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.vine)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.slimewood)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.skyslimeVine)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.enderslimeVine)
                .statType(BowStringMaterialStats.ID);


        buildMaterial(MaterialIds.phantom)
                .statType(ArrowFletchingMaterialStats.ID);

    }
}
