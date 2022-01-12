package tonite.tinkersarchery.recipe;

import net.minecraft.data.IFinishedRecipe;
import slimeknights.tconstruct.library.data.recipe.IRecipeHelper;
import tonite.tinkersarchery.library.ITinkersConsumableItem;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ITinkersConsumableItemRecipeHelper extends IRecipeHelper {

    default void consumableItemBuilding(Consumer<IFinishedRecipe> consumer, ITinkersConsumableItem tool, String folder) {
        TinkersConsumableItemRecipeBuilder.toolBuildingRecipe(tool)
                .build(consumer, modResource(folder + Objects.requireNonNull(tool.asItem().getRegistryName()).getPath()));
    }

    default void consumableItemBuilding(Consumer<IFinishedRecipe> consumer, Supplier<? extends ITinkersConsumableItem> tool, String folder) {
        consumableItemBuilding(consumer, tool.get(), folder);
    }

}
