package tonite.tinkersarchery.data.client;

import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
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
                .addHead("bowshaft").addHead("bowshaft_pulling_1").addHead("bowshaft_pulling_2")
                .addBreakablePart("bowstring", BowStringMaterialStats.ID).addPart("bowstring_pulling_0", BowStringMaterialStats.ID).addPart("bowstring_pulling_1", BowStringMaterialStats.ID).addPart("bowstring_pulling_2", BowStringMaterialStats.ID)
                .addHandle("guide");

        buildTool("crossbow")
                .addHead("bowshaft")
                .addBreakablePart("bowstring", BowStringMaterialStats.ID).addPart("bowstring_pulling_0", BowStringMaterialStats.ID).addPart("bowstring_pulling_1", BowStringMaterialStats.ID).addPart("bowstring_pulling_2", BowStringMaterialStats.ID)
                .addHandle("crossbow_arm");

        buildTool("longbow").withLarge()
                .addHead("bowshaft_left")
                .addHead("bowshaft_right")
                .addBreakablePart("bowstring", BowStringMaterialStats.ID)//.addPart("bowstring_pulling_0", BowStringMaterialStats.ID).addPart("bowstring_pulling_1", BowStringMaterialStats.ID).addPart("bowstring_pulling_2", BowStringMaterialStats.ID)
                .addHandle("guide");

        buildTool("arrow").addHead("arrowhead").addHandle("arrow_shaft").addPart("arrow_fletching", ArrowFletchingMaterialStats.ID);
    }
}
