package tonite.tinkersarchery.items.tools;

import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.ShootableTool;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import java.util.function.Predicate;

public class CrossbowTool extends ShootableTool {

    public CrossbowTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return BowTool.TINKERS_ARROW_ONLY.or(ShootableItem.ARROW_OR_FIREWORK);
    }

    public boolean useOnRelease(ItemStack p_219970_1_) {
        return true;
    }

    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.CROSSBOW;
    }

    public static boolean isCharged(ItemStack itemStack) {
        if(itemStack.hasTag() && itemStack.getTag().contains("Charged")) {
            return itemStack.getTag().getBoolean("Charged");
        }
        return false;
    }

    public static boolean charge(ItemStack projectile, ItemStack itemStack) {
        if (projectile != ItemStack.EMPTY) {
            itemStack.addTagElement("Charged", ByteNBT.ONE);
            itemStack.addTagElement("LoadedProjectiles", projectile.serializeNBT());
            return true;
        }
        return false;
    }

    public static void uncharge(ItemStack itemStack) {
        itemStack.addTagElement("Charged", ByteNBT.ZERO);
        itemStack.addTagElement("LoadedProjectiles", new CompoundNBT());
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !getProjectile(player, itemstack).isEmpty();
        if (isCharged(itemstack)) {

            if (itemstack.getTag().contains("LoadedProjectiles")) {
                ItemStack ammo = ItemStack.of(itemstack.getTag().getCompound("LoadedProjectiles"));
                if (ammo != ItemStack.EMPTY) {
                    shootBow(itemstack, world, player, 1.0f, ammo);
                }
            }

            uncharge(itemstack);

        }
        if (flag) {

            float drawspeedModifier;

            if(!itemstack.hasTag()) {
                drawspeedModifier = 1f;
            } else {
                ToolStack tool = ToolStack.from(itemstack);
                drawspeedModifier = tool.getStats().getFloat(BowAndArrowToolStats.DRAW_SPEED);

                float drawspeedBase = drawspeedModifier;

                for (ModifierEntry m : tool.getModifierList()) {
                    if (m.getModifier() instanceof IBowModifier){
                        drawspeedModifier = ((IBowModifier)m.getModifier()).getDrawSpeed(tool, m.getLevel(), drawspeedBase, drawspeedModifier, world, player);
                    }
                }
            }

            itemstack.addTagElement("Drawspeed", FloatNBT.valueOf(drawspeedModifier));

            if (!player.isUsingItem()) {
                player.startUsingItem(hand);
            }
            return ActionResult.consume(itemstack);
        } else {
            return ActionResult.fail(itemstack);
        }
    }

    public void releaseUsing(ItemStack bow, World world, LivingEntity shooter, int held_ticks) {
        if (shooter instanceof PlayerEntity) {
            int time = getUseDuration(bow) - held_ticks;
            TinkersArchery.LOGGER.info("time held: " + time);
            if (time > 25 / getDrawSpeed(bow)) {

                ItemStack ammo = getProjectile((PlayerEntity)shooter, bow);

                if (ammo != ItemStack.EMPTY) {
                    charge(ammo, bow);
                }
            }
        }
    }

    public int getUseDuration(ItemStack bow) {
        return 3 + (int)(25 / getDrawSpeed(bow));
    }

    public static float getDrawSpeed(ItemStack bow) {
        float drawspeedModifier;

        if(bow.hasTag() && bow.getTag().contains("Drawspeed")) {
            drawspeedModifier = bow.getTag().getFloat("Drawspeed");
        } else {
            drawspeedModifier = 1f;
        }

        return drawspeedModifier;
    }

    public void onUseTick(World p_219972_1_, LivingEntity p_219972_2_, ItemStack p_219972_3_, int p_219972_4_) {

    }

}
