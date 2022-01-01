package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import tonite.tinkersarchery.TinkersArchery;

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
                .addInputItem(TinkersArchery.bowshaft, 8, 44)
                .addInputItem(TinkersArchery.bowstring, 35, 44)
                .addInputItem(TinkersArchery.bowguide, 17, 26)
                .build();
        defineModifiable(TinkersArchery.crossbow)
                .sortIndex(SORT_BOW)
                .addInputItem(TinkersArchery.bowshaft, 17, 26)
                .addInputItem(TinkersArchery.bowstring, 35, 44)
                .addInputItem(TinkersArchery.crossbow_arm, 53, 62)
                .build();
        defineModifiable(TinkersArchery.longbow)
                .sortIndex(SORT_BOW)
                .addInputItem(TinkersArchery.large_bowshaft, 35, 17)
                .addInputItem(TinkersArchery.large_bowshaft, 8, 44)
                .addInputItem(TinkersArchery.bowstring, 35, 44)
                .addInputItem(TinkersArchery.bowguide, 17, 26)
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
