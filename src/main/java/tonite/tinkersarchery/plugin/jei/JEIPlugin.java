package tonite.tinkersarchery.plugin.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.util.ResourceLocation;
import tonite.tinkersarchery.TinkersArchery;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return TinkersArchery.getResource("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registry) {
        // tools
        ISubtypeInterpreter toolInterpreter = new slimeknights.tconstruct.plugin.jei.JEIPlugin.ToolSubtypeInterpreter(false);
        registry.registerSubtypeInterpreter(TinkersArchery.tinkers_arrow.get(), toolInterpreter);
    }
}
