package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.library.modifier.IBowModifier;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nullable;
import java.util.List;

public class Superslime extends SingleUseModifier implements IBowModifier {

    public static final float DRAW_SPEED_BOOST = 0.5f;
    public static final float POWER_BOOST = 0.5f;

    public Superslime() {
        super(0xFF5BD141);
    }

    @Override
    public void addVolatileData(ToolRebuildContext toolRebuildContext, int level, ModDataNBT volatileData) {
        TinkerModifiers.overslime.get().setFriend(volatileData);
    }

    @Override
    public float getDrawSpeed(IModifierToolStack tool, int level, float baseDrawSpeed, float drawSpeed, World world, LivingEntity shooter) {

        OverslimeModifier overslime = TinkerModifiers.overslime.get();
        int current = overslime.getOverslime(tool);
        int cap = overslime.getCapacity(tool);

        return tool.getModifier(BowAndArrowToolStats.DRAW_SPEED) * (((float)current)/ cap) * level * baseDrawSpeed * DRAW_SPEED_BOOST + drawSpeed;

    }

    @Override
    public float getPower(IModifierToolStack tool, int level, float drawPortion, float power, World world, LivingEntity shooter) {

        OverslimeModifier overslime = TinkerModifiers.overslime.get();
        int current = overslime.getOverslime(tool);
        int cap = overslime.getCapacity(tool);

        return tool.getModifier(BowAndArrowToolStats.ELASTICITY) * (((float)current)/ cap) * level * drawPortion * POWER_BOOST + power;

    }

    @Override
    public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey key, TooltipFlag flag) {
        float drawSpeedBonus = DRAW_SPEED_BOOST * level;
        float drawWeightBonus = POWER_BOOST * level;
        if (player != null && key == TooltipKey.SHIFT) {
            OverslimeModifier overslime = TinkerModifiers.overslime.get();
            int current = overslime.getOverslime(tool);
            int cap = overslime.getCapacity(tool);

            float portion = (((float)current)/ cap) * level;

            drawSpeedBonus = portion * DRAW_SPEED_BOOST * level;
            drawWeightBonus = portion * POWER_BOOST * level;
        }
        addStatTooltip(tool, BowAndArrowToolStats.DRAW_SPEED, TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE, drawSpeedBonus, tooltip);
        addStatTooltip(tool, BowAndArrowToolStats.ELASTICITY, TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE, drawWeightBonus, tooltip);
    }
}
