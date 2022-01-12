package tonite.tinkersarchery.data.client;

import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.BowGuideMaterialStats;
import tonite.tinkersarchery.stats.BowMaterialStats;
import tonite.tinkersarchery.stats.BowStringMaterialStats;

public class TinkersArcheryPartSpriteProvider extends AbstractPartSpriteProvider {
    public TinkersArcheryPartSpriteProvider() {
        super(TinkersArchery.MOD_ID);
    }

    @Override
    public String getName() {
        return "Tinkers' Archery Parts";
    }

    @Override
    protected void addAllSpites() {
        addPart("bowstring", BowStringMaterialStats.ID);
        addPart("arrow_fletching", ArrowFletchingMaterialStats.ID);

        buildTool("shortbow")
                .addPart("bowshaft", BowMaterialStats.ID).addPart("bowshaft_pulling_1", BowMaterialStats.ID).addPart("bowshaft_pulling_2", BowMaterialStats.ID)
                .addBreakablePart("bowstring", BowStringMaterialStats.ID).addPart("bowstring_pulling_0", BowStringMaterialStats.ID).addPart("bowstring_pulling_1", BowStringMaterialStats.ID).addPart("bowstring_pulling_2", BowStringMaterialStats.ID)
                .addPart("guide", BowGuideMaterialStats.ID);

        buildTool("crossbow")
                .addPart("bowshaft", BowMaterialStats.ID)
                .addBreakablePart("bowstring", BowStringMaterialStats.ID).addPart("bowstring_pulling_0", BowStringMaterialStats.ID).addPart("bowstring_pulling_1", BowStringMaterialStats.ID).addPart("bowstring_pulling_2", BowStringMaterialStats.ID)
                .addPart("crossbow_arm", BowGuideMaterialStats.ID);

        buildTool("longbow").withLarge()
                .addPart("bowshaft_left", BowMaterialStats.ID).addPart("bowshaft_left_pulling_0", BowMaterialStats.ID).addPart("bowshaft_left_pulling_1", BowMaterialStats.ID).addPart("bowshaft_left_pulling_2", BowMaterialStats.ID)
                .addPart("bowshaft_right", BowMaterialStats.ID).addPart("bowshaft_right_pulling_0", BowMaterialStats.ID).addPart("bowshaft_right_pulling_1", BowMaterialStats.ID).addPart("bowshaft_right_pulling_2", BowMaterialStats.ID)
                .addBreakablePart("bowstring", BowStringMaterialStats.ID).addPart("bowstring_pulling_0", BowStringMaterialStats.ID).addPart("bowstring_pulling_1", BowStringMaterialStats.ID).addPart("bowstring_pulling_2", BowStringMaterialStats.ID)
                .addPart("guide", BowGuideMaterialStats.ID);

        buildTool("arrow").addHead("arrowhead").addHandle("arrow_shaft").addPart("arrow_fletching", ArrowFletchingMaterialStats.ID);
    }
}
