package tonite.tinkersarchery.data.client;

import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;
import tonite.tinkersarchery.data.TinkersArcheryMaterialIds;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.BowStringMaterialStats;

public class TinkerExtendedMaterialSpriteProvider extends TinkerMaterialSpriteProvider {

    @Override
    protected void addAllMaterials() {
        super.addAllMaterials();

        buildMaterial(MaterialIds.wood)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.string)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.skyslimeVine)
                .statType(BowStringMaterialStats.ID);

        buildMaterial(MaterialIds.enderslimeVine)
                .statType(BowStringMaterialStats.ID);


        buildMaterial(MaterialIds.phantom)
                .statType(ArrowFletchingMaterialStats.ID);

    }
}
