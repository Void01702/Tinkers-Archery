package tonite.tinkersarchery.data.client;

import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToSpriteTransformer;
import tonite.tinkersarchery.TinkersArchery;
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
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFF323232)
                        .addARGB(102, 0xFF535353)
                        .addARGB(140, 0xFF676D6D)
                        .addARGB(178, 0xFF7F8E8E)
                        .addARGB(216, 0xFF95ABAB)
                        .addARGB(255, 0xFFC7E0E0)
                        .build());
        buildMaterial(TinkersArcheryMaterialIds.cobalt_tantalum)
                .meleeHarvest()
                .fallbacks("metal")
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFF575757)
                        .addARGB(102, 0xFF727272)
                        .addARGB(140, 0xFF7B808B)
                        .addARGB(178, 0xFF8491AB)
                        .addARGB(216, 0xFF8CA0C7)
                        .addARGB(255, 0xFF90B1F3)
                        .build());
        ResourceLocation galaxyALloyTexture = TinkersArchery.getResource("item/generator/galaxy_alloy");
        buildMaterial(TinkersArcheryMaterialIds.galaxy_alloy)
                .meleeHarvest()
                .fallbacks("galaxy", "metal")
                .transformer(GreyToSpriteTransformer.builderFromBlack()
                        .addARGB(63, 0xFF03001B)
                        .addARGB(102, 0xFF090035)
                        .addTexture(140, galaxyALloyTexture)
                        .addTexture(178, galaxyALloyTexture)
                        .addTexture(216, galaxyALloyTexture)
                        .addARGB(255, 0xFF290097)
                        .build());

        // Bowstring
        buildMaterial(TinkersArcheryMaterialIds.silky_cloth)
                .statType(BowStringMaterialStats.ID)
                .statType(ArrowFletchingMaterialStats.ID)
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFFAD685B)
                        .addARGB(102, 0xFFBF8070)
                        .addARGB(140, 0xFFBF8070)
                        .addARGB(178, 0xFFE8B3A0)
                        .addARGB(216, 0xFFF7CDBB)
                        .addARGB(255, 0xFFFFE7DB)
                        .build());
        buildMaterial(TinkersArcheryMaterialIds.slime)
                .statType(BowStringMaterialStats.ID)
                .colorMapper(fromColor(0xFF5BD141));
        buildMaterial(TinkersArcheryMaterialIds.blazing_string)
                .statType(BowStringMaterialStats.ID)
                .colorMapper(fromColor(0xFFFFC42E));

        buildMaterial(TinkersArcheryMaterialIds.steel_wire)
                .statType(BowStringMaterialStats.ID)
                .colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, 0xFF222626).addARGB(102, 0xFF393D3D).addARGB(140, 0xFF515454).addARGB(178, 0xFF6A6D6D).addARGB(216, 0xFF898C8C).addARGB(255, 0xFFADAFAF).build());

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
    }

    private GreyToColorMapping fromColor(int color) {
        return GreyToColorMapping.builderFromBlack().addARGB(63, GreyToColorMapping.scaleColor(0xFF393939, color, 63)).addARGB(102, GreyToColorMapping.scaleColor(0xFF666666, color, 102)).addARGB(140, GreyToColorMapping.scaleColor(0xFF8C8C8C, color, 140)).addARGB(178, GreyToColorMapping.scaleColor(0xFFB2B2B2, color, 178)).addARGB(216, GreyToColorMapping.scaleColor(0xFFD8D8D8, color, 178)).addARGB(255, GreyToColorMapping.scaleColor(0xFFFFFFFF, color, 255)).build();
    }
}
