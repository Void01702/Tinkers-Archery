package tonite.tinkersarchery.data.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import slimeknights.mantle.registration.object.FluidObject;
import tonite.tinkersarchery.TinkersArchery;

public class TinkersArcheryBlockStates extends BlockStateProvider {

    public TinkersArcheryBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, TinkersArchery.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(TinkersArchery.tantalum_ore);

        blockWithItem(TinkersArchery.tantalum_block);
        blockWithItem(TinkersArchery.tungstantalum_block);
        blockWithItem(TinkersArchery.raw_luxtum_block);
        blockWithItem(TinkersArchery.luxtum_block);
        blockWithItem(TinkersArchery.cobalt_tantalum_block);
        blockWithItem(TinkersArchery.galaxy_alloy_block);

        fluid(TinkersArchery.molten_tantalum);
        fluid(TinkersArchery.molten_tungstantalum);
        fluid(TinkersArchery.molten_luxtum);
        fluid(TinkersArchery.molten_cobalt_tantalum);
        fluid(TinkersArchery.molten_galaxy_alloy);

    }

    public void blockWithItem(RegistryObject<? extends Block> registryObject) {
        //block model
        simpleBlock(registryObject.get());
        //itemblock model
        ResourceLocation id = registryObject.getId();
        ResourceLocation textureLocation = new ResourceLocation(id.getNamespace(), "block/" + id.getPath());
        itemModels().cubeAll(id.getPath(), textureLocation);
    }

    public void fluid(FluidObject fluid) {
        ResourceLocation name = fluid.get().getRegistryName();
        simpleBlock(fluid.getBlock(), models().getExistingFile(new ResourceLocation(name.getNamespace(), "block/fluid/" + name.getPath())));
    }
}
