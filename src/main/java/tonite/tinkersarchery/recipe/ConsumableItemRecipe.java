package tonite.tinkersarchery.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import slimeknights.mantle.recipe.RecipeHelper;
import slimeknights.tconstruct.common.recipe.LoggingRecipeSerializer;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.recipe.tinkerstation.IMutableTinkerStationInventory;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationInventory;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.ITinkersConsumableItem;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsumableItemRecipe implements ITinkerStationRecipe {
    protected final ResourceLocation id;
    protected final String group;
    protected final ITinkersConsumableItem output;

    public IRecipeSerializer<?> getSerializer() {
        return TinkersArchery.tinkersConsumableItemSerializer.get();
    }

    @Override
    public boolean matches(ITinkerStationInventory inv, World worldIn) {

        if (!inv.getTinkerableStack().isEmpty()) {
            return false;
        } else {
            List<PartRequirement> parts = this.output.getToolDefinition().getData().getParts();
            if (parts.isEmpty()) {
                return false;
            } else {
                int i;
                for(i = 0; i < parts.size(); ++i) {
                    if (!(parts.get(i)).matches(inv.getInput(i).getItem())) {
                        return false;
                    }
                }

                while(i < inv.getInputCount()) {
                    if (!inv.getInput(i).isEmpty()) {
                        return false;
                    }

                    ++i;
                }

                return true;
            }
        }
    }

    @Override
    public ItemStack getCraftingResult(ITinkerStationInventory inv) {
        IntStream var10000 = IntStream.range(0, this.output.getToolDefinition().getData().getParts().size());
        inv.getClass();
        List<IMaterial> materials = var10000.mapToObj(inv::getInput).map(IMaterialItem::getMaterialFromStack).collect(Collectors.toList());

        int amount = inv.getInput(0).getCount();
        for (int i = 1; i < materials.size(); i++) {
            amount = Math.min(amount, inv.getInput(i).getCount());
        }

        ItemStack result = ITinkersConsumableItem.of(output, new MaterialNBT(materials));
        int countPerRecipe = output.getToolDefinition().buildStats(materials).getInt(BowAndArrowToolStats.COUNT);

        if (countPerRecipe > result.getMaxStackSize()) {

            result.setCount(result.getMaxStackSize());

        } else {

            result.setCount(countPerRecipe);

        }

        return result;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(this.output.asItem());
    }

    /*@Override
    public void updateInputs(ItemStack result, IMutableTinkerStationInventory inv, boolean isServer) {
        IntStream var10000 = IntStream.range(0, this.output.getToolDefinition().getData().getParts().size());
        List<IMaterial> materials = var10000.mapToObj(inv::getInput).map(IMaterialItem::getMaterialFromStack).collect(Collectors.toList());
        int countPerRecipe = output.getToolDefinition().buildStats(materials).getInt(BowAndArrowToolStats.COUNT);

        int amountToShrink;

        if (countPerRecipe > result.getMaxStackSize()) {
            amountToShrink = 1;
        } else {
            amountToShrink = result.getCount() / countPerRecipe;
        }

        for (int index = 0; index < inv.getInputCount(); index++) {
            inv.shrinkInput(index, amountToShrink);
        }
    }*/

    public ConsumableItemRecipe(ResourceLocation id, String group, ITinkersConsumableItem output) {
        this.id = id;
        this.group = group;
        this.output = output;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public String getGroup() {
        return this.group;
    }

    public static class TinkersConsumableItemRecipeSerializer extends LoggingRecipeSerializer<ConsumableItemRecipe> {

        @Override
        public ConsumableItemRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getAsString(json, "group", "");
            // output fetch as a modifiable item, its an error if it does not implement that interface or does not have parts
            ITinkersConsumableItem item = RecipeHelper.deserializeItem(JSONUtils.getAsString(json, "result"), "result", ITinkersConsumableItem.class);
            if (!item.getToolDefinition().isMultipart()) {
                throw new JsonSyntaxException("Modifiable item must have tool parts to get a tool building recipe");
            }
            return new ConsumableItemRecipe(recipeId, group, item);
        }

        @Nullable
        @Override
        protected ConsumableItemRecipe readSafe(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readUtf(Short.MAX_VALUE);
            ITinkersConsumableItem result = RecipeHelper.readItem(buffer, ITinkersConsumableItem.class);
            return new ConsumableItemRecipe(recipeId, group, result);
        }

        @Override
        protected void writeSafe(PacketBuffer buffer, ConsumableItemRecipe recipe) {
            buffer.writeUtf(recipe.group);
            RecipeHelper.writeItem(buffer, recipe.output::asItem);
        }
    }
}
