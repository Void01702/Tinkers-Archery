package tonite.tinkersarchery.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.recipe.data.AbstractRecipeBuilder;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.ITinkersConsumableItem;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class TinkersConsumableItemRecipeBuilder extends AbstractRecipeBuilder<TinkersConsumableItemRecipeBuilder> {
    private final ITinkersConsumableItem output;

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, Objects.requireNonNull(this.output.asItem().getRegistryName()));
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        ResourceLocation advancementId = this.buildOptionalAdvancement(id, "parts");
        consumerIn.accept(new TinkersConsumableItemRecipeBuilder.Result(id, advancementId));
    }

    private TinkersConsumableItemRecipeBuilder(ITinkersConsumableItem output) {
        this.output = output;
    }

    public static TinkersConsumableItemRecipeBuilder toolBuildingRecipe(ITinkersConsumableItem output) {
        return new TinkersConsumableItemRecipeBuilder(output);
    }

    private class Result extends AbstractRecipeBuilder<TinkersConsumableItemRecipeBuilder>.AbstractFinishedRecipe {
        public Result(ResourceLocation ID, @Nullable ResourceLocation advancementID) {
            super(ID, advancementID);
        }

        public void serializeRecipeData(JsonObject json) {
            if (!TinkersConsumableItemRecipeBuilder.this.group.isEmpty()) {
                json.addProperty("group", TinkersConsumableItemRecipeBuilder.this.group);
            }

            json.addProperty("result", Objects.requireNonNull(TinkersConsumableItemRecipeBuilder.this.output.asItem().getRegistryName()).toString());
        }

        public IRecipeSerializer<?> getType() {
            return TinkersArchery.tinkersConsumableItemSerializer.get();
        }
    }
}