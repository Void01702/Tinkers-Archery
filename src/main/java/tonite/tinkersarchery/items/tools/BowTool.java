package tonite.tinkersarchery.items.tools;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.library.IProjectileItem;
import tonite.tinkersarchery.library.ShootableTool;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import java.util.function.Predicate;

public class BowTool extends ShootableTool {
    public static final ToolType TOOL_TYPE = ToolType.get("shortbow");

    public static final Predicate<ItemStack> TINKERS_ARROW_ONLY = (itemStack) -> {
        return itemStack.getItem() instanceof IProjectileItem;
    };

    public BowTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public void releaseUsing(ItemStack bow, World world, LivingEntity shooter, int held_ticks) {
        if (shooter instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)shooter;
            boolean flag = playerentity.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
            ItemStack itemstack = ShootableTool.getProjectile(playerentity, bow);

            int time = this.getUseDuration(bow) - held_ticks;
            time = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bow, world, playerentity, time, !itemstack.isEmpty() || flag);
            if (time < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float drawPortion = getPowerForTime(time, getDrawSpeed(bow));
                if (!((double)drawPortion < 0.1D)) {
                    boolean flag1 = playerentity.abilities.instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, bow, playerentity));
                    if (!world.isClientSide) {

                        shootBow(bow, world, shooter, drawPortion, itemstack);

                        damageItem(bow, 1, playerentity, (damager) -> {
                        });
                    }

                    world.playSound((PlayerEntity)null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + drawPortion * 0.5F);
                    if (!flag1 && !playerentity.abilities.instabuild) {
                        //itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.removeItem(itemstack);
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static float getPowerForTime(int time, float drawspeedModifier) {

        float power = (float)time * drawspeedModifier / 20.0F;
        power = (power * power + power * 2.0F) / 3.0F;
        if (power > 1.0F) {
            power = 1.0F;
        }

        return power;
    }

    public static float getPowerForTimeOfTool(int time, ItemStack tool) {

        float power = (float)time * getDrawSpeed(tool) / 20.0F;
        power = (power * power + power * 2.0F) / 3.0F;
        if (power > 1.0F) {
            power = 1.0F;
        }

        return power;
    }

    public int getUseDuration(ItemStack bow) {

        return (int)(72000 / getDrawSpeed(bow));
    }

    public static float getDrawSpeed(ItemStack bow) {
        float drawspeedModifier;

        if(bow.hasTag() && bow.getTag().contains("drawspeed")) {
            drawspeedModifier = bow.getTag().getFloat("drawspeed");
        } else {
            drawspeedModifier = 1f;
        }

        return drawspeedModifier;
    }

    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !player.getProjectile(itemstack).isEmpty();

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

        itemstack.addTagElement("drawspeed", FloatNBT.valueOf(drawspeedModifier));

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, world, player, hand, flag);
        if (ret != null) return ret;

        if (!player.abilities.instabuild && !flag) {
            return ActionResult.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return ActionResult.consume(itemstack);
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return TINKERS_ARROW_ONLY.or(ShootableItem.ARROW_ONLY);
    }


}
