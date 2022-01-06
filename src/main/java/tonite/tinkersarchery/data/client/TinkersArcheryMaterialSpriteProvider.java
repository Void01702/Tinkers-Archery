package tonite.tinkersarchery.data.client;

import net.minecraft.util.ResourceLocation;
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
        ResourceLocation galaxyAlloyTexture = TinkersArchery.getResource("item/generator/galaxy_alloy");
        buildMaterial(TinkersArcheryMaterialIds.galaxy_alloy)
                .meleeHarvest()
                .fallbacks("galaxy", "metal")
                .transformer(GreyToSpriteTransformer.builderFromBlack()
                        .addARGB(63, 0xFF03001B)
                        .addARGB(102, 0xFF090035)
                        .addTexture(140, galaxyAlloyTexture)
                        .addTexture(178, galaxyAlloyTexture)
                        .addTexture(216, galaxyAlloyTexture)
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
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(255, 0xFF5BD141)
                        .build());
        buildMaterial(TinkersArcheryMaterialIds.blazing_string)
                .statType(BowStringMaterialStats.ID)
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(255, 0xFFFFC42E)
                        .build());

        buildMaterial(TinkersArcheryMaterialIds.steel_wire)
                .statType(BowStringMaterialStats.ID)
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFF222626)
                        .addARGB(102, 0xFF393D3D)
                        .addARGB(140, 0xFF515454)
                        .addARGB(178, 0xFF6A6D6D)
                        .addARGB(216, 0xFF898C8C)
                        .addARGB(255, 0xFFADAFAF)
                        .build());

        // Fletching
        buildMaterial(TinkersArcheryMaterialIds.feather)
                .statType(ArrowFletchingMaterialStats.ID)
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(178, 0xFFB2B2B2)
                        .addARGB(255, 0xFFFFFFFF)
                        .build());
        buildMaterial(TinkersArcheryMaterialIds.paper)
                .statType(ArrowFletchingMaterialStats.ID)
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(178, 0xFFF0F0F0)
                        .addARGB(255, 0xFFF0F0F0)
                        .build());
        buildMaterial(TinkersArcheryMaterialIds.leaf)
                .statType(ArrowFletchingMaterialStats.ID)
                .fallbacks("leaf")
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(178, 0xFF83EA5D)
                        .addARGB(255, 0xFF4AD718)
                        .build());
        buildMaterial(TinkersArcheryMaterialIds.slime_leaf)
                .statType(ArrowFletchingMaterialStats.ID)
                .fallbacks("leaf")
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(178, 0xFFA8FFFD)
                        .addARGB(255, 0xFF36FFFC)
                        .build());
    }
}
