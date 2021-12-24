package tonite.tinkersarchery.modifiers.upgrades;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.modifier.IBowModifier;

import java.util.List;

public class Burst extends Modifier implements IBowModifier {
    public Burst() {
        super(0xFFFC921C);
    }

    @Override
    public float getDrawSpeed(IModifierToolStack tool, int level, float baseDrawSpeed, float drawSpeed, World world, LivingEntity shooter) {

        if(shooter.hasEffect(TinkersArchery.burstEffect.get())) {

            shooter.removeEffect(TinkersArchery.burstEffect.get());

            tool.getPersistentData().putInt(TinkersArchery.getResource("burst_count"), tool.getPersistentData().getInt(TinkersArchery.getResource("burst_count")) - 1);

            return drawSpeed * 3;
        } else {
            tool.getPersistentData().putInt(TinkersArchery.getResource("burst_count"), level);

            return drawSpeed;
        }
    }

    @Override
    public void onReleaseBow(IModifierToolStack tool, int level, float drawPortion, float power, float accuracy, List<ArrowData> arrows, World world, LivingEntity shooter) {
        if(tool.getPersistentData().getInt(TinkersArchery.getResource("burst_count")) > 0 && drawPortion > 0.75) {
            int duration = 20;
            TinkersArchery.burstEffect.get().apply(shooter, duration);
        }
    }
}
