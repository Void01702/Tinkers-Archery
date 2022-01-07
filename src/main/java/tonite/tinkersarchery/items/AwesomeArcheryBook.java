package tonite.tinkersarchery.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.mantle.item.LecternBookItem;
import tonite.tinkersarchery.client.book.TinkersArcheryBook;

public class AwesomeArcheryBook extends LecternBookItem {

    public AwesomeArcheryBook(Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide) {
            TinkersArcheryBook.AWESOME_ARCHERY.openGui(hand, stack);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public void openLecternScreenClient(BlockPos pos, ItemStack stack) {
        TinkersArcheryBook.AWESOME_ARCHERY.openGui(pos, stack);
    }
}
