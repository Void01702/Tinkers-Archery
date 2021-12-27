package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.tools.BowAndArrowDefinitions;

public class TinkersArcheryToolSlotLayouts extends AbstractStationSlotLayoutProvider {

    public static int SORT_BOW = SORT_WEAPON + 1;
    public static int SORT_PROJECTILE = SORT_WEAPON + 2;

    public TinkersArcheryToolSlotLayouts(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addLayouts() {
        defineModifiable(TinkersArchery.shortbow)
                .sortIndex(SORT_BOW)
                .addInputItem(TinkersArchery.bowshaft, 3, 44)
                .addInputItem(TinkersArchery.bowstring, 30, 44)
                .addInputItem(TinkersArchery.bowguide, 12, 26)
                .build();
        defineModifiable(TinkersArchery.crossbow)
                .sortIndex(SORT_BOW)
                .addInputItem(TinkersArchery.bowshaft, 12, 26)
                .addInputItem(TinkersArchery.bowstring, 30, 44)
                .addInputItem(TinkersArchery.crossbow_arm, 48, 62)
                .build();
        defineModifiable(TinkersArchery.longbow)
                .sortIndex(SORT_BOW)
                .addInputItem(TinkersArchery.large_bowshaft, 3, 44)
                .addInputItem(TinkersArchery.large_bowshaft, 30, 44)
                .addInputItem(TinkersArchery.bowstring, 30, 17)
                .addInputItem(TinkersArchery.bowguide, 12, 26)
                .build();
        defineModifiable(TinkersArchery.arrow)
                .sortIndex(SORT_PROJECTILE)
                .addInputItem(TinkersArchery.arrowhead, 48, 26)
                .addInputItem(TinkersArchery.arrow_shaft, 30, 44)
                .addInputItem(TinkersArchery.arrow_fletching, 12, 62)
                .build();
    }

    @Override
    public String getName() {
        return "Tinkers Archery Tinker Station Slot Layouts";
    }
}
