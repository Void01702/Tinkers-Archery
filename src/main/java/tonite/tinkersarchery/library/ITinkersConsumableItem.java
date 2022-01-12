package tonite.tinkersarchery.library;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public interface ITinkersConsumableItem {

    static ItemStack of(ITinkersConsumableItem originalItem, MaterialNBT materials) {
        return ToolStack.createTool(originalItem.asItem(), originalItem.getToolDefinition(), materials.getMaterials()).createStack();
    }

    static ItemStack of(ITinkersConsumableItem originalItem, List<MaterialId> materials) {
        ItemStack result = new ItemStack(originalItem.asItem());

        CompoundNBT tag = new CompoundNBT();
        ListNBT materialList = new ListNBT();
        for (int i = 0; i < materials.size(); i++) {
            materialList.add(StringNBT.valueOf(materials.get(i).toString()));
        }
        tag.put(ToolStack.TAG_MATERIALS, materialList);
        result.setTag(tag);

        return result;
    }

    static MaterialIdNBT getMaterialIds(ItemStack stack){
        if (stack.hasTag() && stack.getTag().contains(ToolStack.TAG_MATERIALS)) {
            return MaterialIdNBT.readFromNBT(stack.getTag().get(ToolStack.TAG_MATERIALS));
        } else {
            return new MaterialIdNBT(ImmutableList.of());
        }
    }

    static MaterialNBT getMaterials(ItemStack itemStack) {
        if (itemStack.hasTag() && itemStack.getTag().contains(ToolStack.TAG_MATERIALS)) {
            return MaterialNBT.readFromNBT(itemStack.getTag().get(ToolStack.TAG_MATERIALS));
        }
        return new MaterialNBT(ImmutableList.of());
    }

    static boolean isInitialized(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().contains(ToolStack.TAG_MATERIALS, Constants.NBT.TAG_LIST);
    }

    ToolDefinition getToolDefinition();

    default Item asItem(){
        return (Item)this;
    }

}
