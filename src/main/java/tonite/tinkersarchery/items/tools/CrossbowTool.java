package tonite.tinkersarchery.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import tonite.tinkersarchery.library.ShootableTool;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrossbowTool extends ShootableTool {

    public CrossbowTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public int getItemAmount(ItemStack itemStack, int desiredAmount, boolean isHand) {
        if (itemStack.getItem() == Items.FIREWORK_ROCKET && isHand) {
            return Math.min(itemStack.getCount(), desiredAmount);
        } else {
            return super.getItemAmount(itemStack, desiredAmount, isHand);
        }
    }

    @Override
    public ProjectileEntity createArrow(ItemStack bow, World world, Vector3f direction, float drawPortion, LivingEntity shooter, ItemStack arrowItem) {
        if (arrowItem.getItem() == Items.FIREWORK_ROCKET) {
            return new FireworkRocketEntity(world, arrowItem, shooter, shooter.getX(), shooter.getEyeY() - (double)0.15F, shooter.getZ(), true);
        } else {
            return super.createArrow(bow, world, direction, drawPortion, shooter, arrowItem);
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

        int[] itemCounts = itemStack.getTag().getIntArray("ArrowCounts");
        ListNBT listNBT = itemStack.getTag().getList("LoadedProjectiles", Constants.NBT.TAG_COMPOUND);
        List<ItemStack> items = listNBT.stream().map(nbt -> ItemStack.of((CompoundNBT)nbt)).collect(Collectors.toList());

        int totalCount = 1;

        for (int amount : itemCounts) {
            totalCount += amount;
        }

        while (totalCount > 0 && !items.isEmpty()) {
            if (items.get(0).getCount() <= totalCount) {
                totalCount -= items.get(0).getCount();
                items.remove(0);
            } else {
                items.get(0).shrink(totalCount);
                totalCount = 0;
            }
        }

        if (items.isEmpty()) {
            itemStack.addTagElement("Charged", ByteNBT.ZERO);
            itemStack.addTagElement("LoadedProjectiles", new ListNBT());
            itemStack.addTagElement("ArrowCounts", new ListNBT());
        } else {
            ListNBT newList = new ListNBT();
            for (ItemStack entry : items) {
                CompoundNBT nbt = entry.serializeNBT();
                newList.add(nbt);
            }
            itemStack.addTagElement("LoadedProjectiles", newList);
        }
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ToolStack toolStack = ToolStack.from(itemstack);
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
                    damageItem(itemstack, 1, player, (damager) -> {
                    });
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }

            uncharge(itemstack);

        }
        if (flag && !toolStack.isBroken()) {

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
            if (time >= (int)(20 / getDrawSpeed(bow))) {

                int[] arrowCounts = getArrowCounts(bow, world, shooter, 1.0f);

                List<AmmoListEntry> ammo = getProjectile((PlayerEntity)shooter, bow, compileArrowCounts(arrowCounts));

                if (!ammo.isEmpty()) {
                    charge(ammo, arrowCounts, bow);
                    consumeAmmo(ammo, (PlayerEntity) shooter);
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
