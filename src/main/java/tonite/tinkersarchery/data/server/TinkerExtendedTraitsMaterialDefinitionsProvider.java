package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.stats.ArrowFletchingMaterialStats;
import tonite.tinkersarchery.stats.BowStringMaterialStats;

public class TinkerExtendedTraitsMaterialDefinitionsProvider extends AbstractMaterialDataProvider {

    public TinkerExtendedTraitsMaterialDefinitionsProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {

        // Bowstrings
        add(MaterialIds.string);
        add(MaterialIds.skyslimeVine);

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
            // Bowstrings
            addTraits(MaterialIds.string, BowStringMaterialStats.ID, TinkersArchery.CLEAN_MODIFIER.get());
            addTraits(MaterialIds.skyslimeVine, BowStringMaterialStats.ID, TinkersArchery.AIRBORNE_MODIFIER.get());

            // Fletchings
            addTraits(MaterialIds.phantom, ArrowFletchingMaterialStats.ID, TinkersArchery.ANTIGRAVITY_TRAJECTORY_MODIFIER.get());
        }

        @Override
        public String getName() {
            return "Tinkers' Archery Extension of Tinker's Construct Material Traits";
        }
    }

}
