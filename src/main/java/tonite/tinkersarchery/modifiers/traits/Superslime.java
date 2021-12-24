package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.IBowModifier;

public class Superslime extends Modifier implements IBowModifier {

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

        return (((float)current)/ cap) * baseDrawSpeed + drawSpeed;

    }

    @Override
    public float getPower(IModifierToolStack tool, int level, float drawPortion, float power, World world, LivingEntity shooter) {

        OverslimeModifier overslime = TinkerModifiers.overslime.get();
        int current = overslime.getOverslime(tool);
        int cap = overslime.getCapacity(tool);

        return (((float)current)/ cap) * drawPortion + power;

    }
}
