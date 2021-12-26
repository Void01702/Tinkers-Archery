package tonite.tinkersarchery.items.tools;

import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.IProjectileItem;
import tonite.tinkersarchery.library.ShootableTool;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CrossbowTool extends ShootableTool {

    public CrossbowTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public int getItemAmount(ItemStack itemStack, int desiredAmount, boolean isHand) {
        if (itemStack.getItem() == Items.FIREWORK_ROCKET) {
            return ((IProjectileItem)itemStack.getItem()).getAmmo(itemStack, desiredAmount);
        } else {
            return super.getItemAmount(itemStack, desiredAmount, isHand);
        }
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

    public static boolean charge(List<AmmoListEntry> projectiles, int[] arrowCounts, ItemStack itemStack) {
        if (!projectiles.isEmpty()) {
            itemStack.addTagElement("Charged", ByteNBT.ONE);
            ListNBT listNBT = new ListNBT();
            for (AmmoListEntry entry : projectiles) {
                CompoundNBT nbt = entry.itemStack.serializeNBT();
                nbt.putInt("Count", entry.amount);
                listNBT.add(nbt);
            }
            itemStack.addTagElement("LoadedProjectiles", listNBT);
            itemStack.addTagElement("ArrowCounts", new IntArrayNBT(arrowCounts));
            return true;
        }
        return false;
    }

    public static void uncharge(ItemStack itemStack) {
        itemStack.addTagElement("Charged", ByteNBT.ZERO);
        itemStack.addTagElement("LoadedProjectiles", new ListNBT());
        itemStack.addTagElement("ArrowCounts", new ListNBT());
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !getFirstProjectile(player, itemstack).isEmpty();
        if (isCharged(itemstack)) {

            if (itemstack.getTag().contains("LoadedProjectiles") && itemstack.getTag().contains("ArrowCounts")) {
                ListNBT ammo = itemstack.getTag().getList("LoadedProjectiles", Constants.NBT.TAG_COMPOUND);
                int[] arrowCounts = itemstack.getTag().getIntArray("ArrowCounts");

                List<AmmoListEntry> arrows = new ArrayList<>();

                for (INBT nbt : ammo) {
                    ItemStack itemStack = ItemStack.of((CompoundNBT) nbt);

                    arrows.add(new AmmoListEntry(itemStack, itemStack.getCount()));
                }

                if (!ammo.isEmpty()) {
                    shootBow(itemstack, world, player, 1.0f, arrows, arrowCounts);
                    if (!world.isClientSide) {
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundCategory.PLAYERS, 0.5F, 1.0F);
                    }
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

                int[] arrowCounts = getArrowCounts(bow, world, shooter, 1.0f);

                List<AmmoListEntry> ammo = getProjectile((PlayerEntity)shooter, bow, compileArrowCounts(arrowCounts));

                if (!ammo.isEmpty()) {
                    charge(ammo, arrowCounts, bow);
                    if (!world.isClientSide) {
                        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundCategory.PLAYERS, 0.5F, 1.0F);
                    }
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

    public void onUseTick(World world, LivingEntity user, ItemStack itemStack, int ticks_held) {

        if (!world.isClientSide) {

            float drawSpeed = getDrawSpeed(itemStack);

            int time = getUseDuration(itemStack) - ticks_held;

            if (time == (int)(5 / drawSpeed)) {
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.CROSSBOW_LOADING_START, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (time == (int)(13 / drawSpeed)) {
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

        }

    }

}
