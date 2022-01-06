package tonite.tinkersarchery.data;

import slimeknights.tconstruct.library.materials.definition.MaterialId;
import tonite.tinkersarchery.TinkersArchery;

public class TinkersArcheryMaterialIds {

    // Melee Harvest
    public static final MaterialId tantalum = id("tantalum");
    public static final MaterialId cobalt_tantalum = id("cobalt_tantalum");
    public static final MaterialId galaxy_alloy = id("galaxy_alloy");

    // Bowstring
    public static final MaterialId silky_cloth = id("silky_cloth");
    public static final MaterialId slime = id("slime");
    public static final MaterialId blazing_string = id("blazing_string");

    public static final MaterialId steel_wire = id("steel_wire");

    // Fletchings
    public static final MaterialId feather = id("feather");
    public static final MaterialId paper = id("paper");
    public static final MaterialId leaf = id("leaf");
    public static final MaterialId slime_leaf = id("slime_leaf");

    private static MaterialId id(String name) {
        return new MaterialId(TinkersArchery.MOD_ID, name);
    }

}
