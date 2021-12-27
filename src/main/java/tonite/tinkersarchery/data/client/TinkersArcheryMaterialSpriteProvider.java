package tonite.tinkersarchery.data.client;

import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.data.TinkersArcheryMaterialIds;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.BowStringMaterialStats;

public class TinkersArcheryMaterialSpriteProvider extends AbstractMaterialSpriteProvider {
    @Override
    public String getName() {
        return "Tinkers' Archery Parts";
    }

    //TODO find good colorings for materials

    @Override
    protected void addAllMaterials() {
        // Melee Harvest
        buildMaterial(TinkersArcheryMaterialIds.tantalum)
                .meleeHarvest()
                .fallbacks("metal")
                .colorMapper(fromColor(0xFF9EB9D4));
        buildMaterial(TinkersArcheryMaterialIds.cobalt_tantalum)
                .meleeHarvest()
                .fallbacks("metal")
                .colorMapper(fromColor(0xFF5079FF));
        buildMaterial(TinkersArcheryMaterialIds.galaxy_alloy)
                .meleeHarvest()
                .fallbacks("metal")
                .colorMapper(fromColor(0xFF21007F));

        // Bowstring
        buildMaterial(TinkersArcheryMaterialIds.vine)
                .statType(BowStringMaterialStats.ID)
                .fallbacks("vine")
                .colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, 0xFF143306).addARGB(102, 0xFF183D08).addARGB(140, 0xFF1F4E0A).addARGB(178, 0xFF265F0D).addARGB(216, 0xFF2E730F).addARGB(255, 0xFF3A9313).build());
        buildMaterial(TinkersArcheryMaterialIds.twisting_vine)
                .statType(BowStringMaterialStats.ID)
                .fallbacks("vine")
                .colorMapper(fromColor(0xFF119B85));
        buildMaterial(TinkersArcheryMaterialIds.weeping_vine)
                .statType(BowStringMaterialStats.ID)
                .fallbacks("vine")
                .colorMapper(fromColor(0xFF7B0000));
        buildMaterial(TinkersArcheryMaterialIds.slime)
                .statType(BowStringMaterialStats.ID)
                .colorMapper(fromColor(0xFF5BD141));

        // Fletching
        buildMaterial(TinkersArcheryMaterialIds.feather)
                .statType(ArrowFletchingMaterialStats.ID)
                .colorMapper(fromColor(0xFFFFFFFF));
        buildMaterial(TinkersArcheryMaterialIds.leaf)
                .statType(ArrowFletchingMaterialStats.ID)
                .fallbacks("leaf")
                .colorMapper(fromColor(0xFF4AD718));
        buildMaterial(TinkersArcheryMaterialIds.slime_leaf)
                .statType(ArrowFletchingMaterialStats.ID)
                .fallbacks("leaf")
                .colorMapper(fromColor(0xFF36FFFC));
        buildMaterial(TinkersArcheryMaterialIds.silky_cloth)
                .statType(ArrowFletchingMaterialStats.ID)
                .colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, 0xFFAD685B).addARGB(102, 0xFFBF8070).addARGB(140, 0xFFBF8070).addARGB(178, 0xFFE8B3A0).addARGB(216, 0xFFF7CDBB).addARGB(255, 0xFFFFE7DB).build());
    }

    private GreyToColorMapping fromColor(int color) {
        return GreyToColorMapping.builderFromBlack().addARGB(63, GreyToColorMapping.scaleColor(0xFF393939, color, 63)).addARGB(102, GreyToColorMapping.scaleColor(0xFF666666, color, 102)).addARGB(140, GreyToColorMapping.scaleColor(0xFF8C8C8C, color, 140)).addARGB(178, GreyToColorMapping.scaleColor(0xFFB2B2B2, color, 178)).addARGB(216, GreyToColorMapping.scaleColor(0xFFD8D8D8, color, 178)).addARGB(255, GreyToColorMapping.scaleColor(0xFFFFFFFF, color, 255)).build();
    }
}
