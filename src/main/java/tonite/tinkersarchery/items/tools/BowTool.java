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

import java.util.List;
import java.util.function.Predicate;

public class BowTool extends ShootableTool {
    public static final ToolType TOOL_TYPE = ToolType.get("shortbow");

    public BowTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public void releaseUsing(ItemStack bow, World world, LivingEntity shooter, int held_ticks) {
        if (shooter instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)shooter;

            int time = this.getUseDuration(bow) - held_ticks;
            time = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bow, world, playerentity, time, !getFirstProjectile(playerentity, bow).isEmpty());
            if (time < 0) return;

            float drawPortion = getPowerForTime(time, getDrawSpeed(bow));

            int[] arrowCounts = getArrowCounts(bow, world, shooter, drawPortion);

            List<AmmoListEntry> ammoList = ShootableTool.getProjectile(playerentity, bow, compileArrowCounts(arrowCounts));

            if (!ammoList.isEmpty()) {

                if (!((double)drawPortion < 0.1D)) {
                    if (!world.isClientSide) {

                        shootBow(bow, world, shooter, drawPortion, ammoList, arrowCounts);

                        damageItem(bow, 1, playerentity, (damager) -> {
                        });
                    }

                    world.playSound((PlayerEntity)null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + drawPortion * 0.5F);

                    consumeAmmo(ammoList, playerentity);

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

        if(bow.hasTag() && bow.getTag().contains("Drawspeed")) {
            drawspeedModifier = bow.getTag().getFloat("Drawspeed");
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
        ToolStack tool = ToolStack.from(itemstack);

        if (!tool.isBroken()) {
            boolean flag = !getFirstProjectile(player, itemstack).isEmpty();

            float drawspeedModifier;

            if (!itemstack.hasTag()) {
                drawspeedModifier = 1f;
            } else {

                drawspeedModifier = tool.getStats().getFloat(BowAndArrowToolStats.DRAW_SPEED);

                float drawspeedBase = drawspeedModifier;

                for (ModifierEntry m : tool.getModifierList()) {
                    if (m.getModifier() instanceof IBowModifier) {
                        drawspeedModifier = ((IBowModifier) m.getModifier()).getDrawSpeed(tool, m.getLevel(), drawspeedBase, drawspeedModifier, world, player);
                    }
                }
            }

            itemstack.addTagElement("Drawspeed", FloatNBT.valueOf(drawspeedModifier));

            ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, world, player, hand, flag);
            if (ret != null) return ret;

            if (player.abilities.instabuild || flag) {
                player.startUsingItem(hand);
                return ActionResult.consume(itemstack);
            } else {
                return ActionResult.fail(itemstack);
            }
        }
        return ActionResult.fail(itemstack);
    }

}
