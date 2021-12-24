package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.modifier.IBowModifier;

public class Clean extends SingleUseModifier implements IBowModifier {

    public Clean() {
        super(0xFFD9D9D9);
    }

    @Override
    public float getAccuracy(IModifierToolStack tool, int level, float drawPortion, float baseAccuracy, float accuracy, World world, LivingEntity shooter) {
        return accuracy * (1 + drawPortion * level);
    }
}
