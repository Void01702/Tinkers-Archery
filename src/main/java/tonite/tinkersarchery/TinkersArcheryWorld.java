package tonite.tinkersarchery;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TinkersArchery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TinkersArcheryWorld {

    @SubscribeEvent
    static void onBiomeLoad(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();

        Biome.Category category = event.getCategory();
        if (category != Biome.Category.NETHER && category != Biome.Category.THEEND) {
            if (TinkersArcheryConfig.COMMON.generateTantalum.get()) {
                generation.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, TinkersArchery.TANTALUM_ORE_FEATURE);
            }
        }
    }

}
