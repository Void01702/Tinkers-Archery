package tonite.tinkersarchery.client;

import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.tconstruct.gadgets.client.FancyItemFrameRenderer;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.client.model.BowModel;
import tonite.tinkersarchery.client.renderer.TinkersArrowRenderer;
import tonite.tinkersarchery.items.tools.BowTool;

@Mod.EventBusSubscriber(modid = TinkersArchery.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TinkersArcheryClient {

    @SubscribeEvent
    static void registerModelLoaders(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(TinkersArchery.getResource("generic_bow"), BowModel.LOADER);
    }

    @SubscribeEvent
    static void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

        RenderingRegistry.registerEntityRenderingHandler(TinkersArchery.TINKERS_ARROW.get(), TinkersArrowRenderer::new);

        event.enqueueWork(() ->
        {

            ItemModelsProperties.register(TinkersArchery.shortbow.get(), new ResourceLocation("pull"), (itemStack, world, player) -> {
                if (player == null) {
                    return 0.0F;
                } else {
                    return player.getUseItem() != itemStack ? 0.0F : BowTool.getPowerForTimeOfTool(itemStack.getUseDuration() - player.getUseItemRemainingTicks(), itemStack);
                }
            });
            ItemModelsProperties.register(TinkersArchery.shortbow.get(), new ResourceLocation("pulling"), (itemStack, world, player) -> {
                return player != null && player.isUsingItem() && player.getUseItem() == itemStack ? 1.0F : 0.0F;
            });
        });
    }

}
