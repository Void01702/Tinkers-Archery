package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.stats.*;

public class TinkerExtendedTraitsMaterialDefinitionsProvider extends AbstractMaterialDataProvider {

    public TinkerExtendedTraitsMaterialDefinitionsProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {

        // Bow Materials
        add(MaterialIds.steel);
        add(MaterialIds.cobalt);

        // Bowstrings
        add(MaterialIds.string);
        add(MaterialIds.skyslimeVine);

        // Arrow Materials
        add(MaterialIds.pigIron);

        // Fletchings
        add(MaterialIds.phantom);

    }

    private void add(MaterialId id) {
        addMaterial (id, 0, 0, true,  0xFFFFFFFF);
    }

    @Override
    public String getName()  {
        return "Tinkers' Archery Extension of Tinker's Construct Materials For Traits";
    }

    public static class TinkerExtendedMaterialTraitDataProvider extends AbstractMaterialTraitDataProvider {

        public TinkerExtendedMaterialTraitDataProvider(DataGenerator gen, AbstractMaterialDataProvider materials) {
            super(gen, materials);
        }

        @Override
        protected void addMaterialTraits() {

            // Materials
            addTraits(MaterialIds.steel, BowMaterialStats.ID, TinkersArchery.DUCTILE_REPLACEMENT_MODIFIER.get());
            addTraits(MaterialIds.steel, BowGuideMaterialStats.ID, TinkersArchery.DUCTILE_REPLACEMENT_MODIFIER.get());
            addTraits(MaterialIds.cobalt, BowMaterialStats.ID, TinkersArchery.LIGHTWEIGHT_REPLACEMENT_MODIFIER.get());
            addTraits(MaterialIds.cobalt, BowGuideMaterialStats.ID, TinkersArchery.LIGHTWEIGHT_REPLACEMENT_MODIFIER.get());

            // Bowstrings
            addTraits(MaterialIds.string, BowStringMaterialStats.ID, TinkersArchery.CLEAN_MODIFIER.get());
            addTraits(MaterialIds.skyslimeVine, BowStringMaterialStats.ID, TinkersArchery.AIRBORNE_MODIFIER.get());

            // Arrow Materials
            addTraits(MaterialIds.pigIron, ArrowHeadMaterialStats.ID, TinkersArchery.TASTY_REPLACEMENT_MODIFIER.get());
            addTraits(MaterialIds.pigIron, ArrowShaftMaterialStats.ID, TinkersArchery.TASTY_REPLACEMENT_MODIFIER.get());

            // Fletchings
            addTraits(MaterialIds.phantom, ArrowFletchingMaterialStats.ID, TinkersArchery.ANTIGRAVITY_TRAJECTORY_MODIFIER.get());
        }

        @Override
        public String getName() {
            return "Tinkers' Archery Extension of Tinker's Construct Material Traits";
        }
    }

}
