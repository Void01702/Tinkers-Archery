package tonite.tinkersarchery.data.client;

import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import tonite.tinkersarchery.TinkersArchery;

public class TinkersArcheryItemModels extends ItemModelProvider {
    public TinkersArcheryItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TinkersArchery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        itemWithModel(TinkersArchery.tantalum_ingot, "item/generated");
        itemWithModel(TinkersArchery.tantalum_nugget, "item/generated");

        itemWithModel(TinkersArchery.cobalt_tantalum_ingot, "item/generated");
        itemWithModel(TinkersArchery.cobalt_tantalum_nugget, "item/generated");

        itemWithModel(TinkersArchery.galaxy_alloy_ingot, "item/generated");
        itemWithModel(TinkersArchery.galaxy_alloy_nugget, "item/generated");

        bucketModel(TinkersArchery.molten_tantalum);
        bucketModel(TinkersArchery.molten_cobalt_tantalum);
        bucketModel(TinkersArchery.molten_galaxy_alloy);

        castModels(TinkersArchery.bowshaft_cast);
        castModels(TinkersArchery.bowguide_cast);
        castModels(TinkersArchery.crossbow_arm_cast);
        castModels(TinkersArchery.large_bowshaft_cast);
        castModels(TinkersArchery.arrowhead_cast);
        castModels(TinkersArchery.arrow_shaft_cast);
    }

    public void itemWithModel(RegistryObject<? extends Item> registryObject, String parent) {
        ResourceLocation id = registryObject.getId();
        ResourceLocation textureLocation = new ResourceLocation(id.getNamespace(), "item/" + id.getPath());
        singleTexture(id.getPath(), new ResourceLocation(parent), "layer0", textureLocation);
    }

    public void castModels(CastItemObject cast) {
        ResourceLocation idGold = cast.get().getRegistryName();
        String path = idGold.getPath().replace("_cast", "");
        ResourceLocation textureLocationGold = new ResourceLocation(idGold.getNamespace(), "item/cast/gold/" + path);
        singleTexture(idGold.getPath(), new ResourceLocation("item/generated"), "layer0", textureLocationGold);
        ResourceLocation idSand = cast.getSand().getRegistryName();
        ResourceLocation textureLocationSand = new ResourceLocation(idSand.getNamespace(), "item/cast/sand/" + path);
        singleTexture(idSand.getPath(), new ResourceLocation("item/generated"), "layer0", textureLocationSand);
        ResourceLocation idSandRed = cast.getRedSand().getRegistryName();
        ResourceLocation textureLocationSandRed = new ResourceLocation(idSandRed.getNamespace(), "item/cast/red_sand/" + path);
        singleTexture(idSandRed.getPath(), new ResourceLocation("item/generated"), "layer0", textureLocationSandRed);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void bucketModel(FluidObject registryObject) {
        ModelBuilder builder = getBuilder(registryObject.getId().getPath() + "_bucket").parent(getExistingFile(new ResourceLocation("forge", "item/bucket_drip")));

        //I'm not sure how this works but it works
        builder.customLoader((t, u) ->  new CustomLoaderBuilder(((ModelBuilder) t).getLocation(), (ModelBuilder) t, (ExistingFileHelper) u) {
            public JsonObject toJson(JsonObject json) {
                json.addProperty("loader", "forge:bucket");
                json.addProperty("fluid", registryObject.get().getFluid().getRegistryName().toString());
                return json;
            }
        });
    }

}
