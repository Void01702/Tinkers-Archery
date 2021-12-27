package tonite.tinkersarchery.data;

import slimeknights.tconstruct.library.materials.definition.MaterialId;
import tonite.tinkersarchery.TinkersArchery;

public class TinkersArcheryMaterialIds {

    // Melee Harvest
    public static final MaterialId tantalum = id("tantalum");
    public static final MaterialId cobalt_tantalum = id("cobalt_tantalum");
    public static final MaterialId galaxy_alloy = id("galaxy_alloy");

    // Bowstring
    public static final MaterialId vine = id("vine");
    public static final MaterialId twisting_vine = id("twisting_vine");
    public static final MaterialId weeping_vine = id("weeping_vine");
    public static final MaterialId slime = id("slime");

    // Fletchings
    public static final MaterialId feather = id("feather");
    public static final MaterialId leaf = id("leaf");
    public static final MaterialId slime_leaf = id("slime_leaf");
    public static final MaterialId silky_cloth = id("silky_cloth");

    private static MaterialId id(String name) {
        return new MaterialId(TinkersArchery.MOD_ID, name);
    }

}
