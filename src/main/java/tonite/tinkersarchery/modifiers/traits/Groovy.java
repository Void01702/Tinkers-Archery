package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.IBowModifier;

import java.util.List;

public class Groovy extends Modifier implements IBowModifier {
    public Groovy() {
        super(0xFF21007F);
    }

    @Override
    public float getDrawSpeed(IModifierToolStack tool, int level, float baseDrawSpeed, float drawSpeed, World world, LivingEntity shooter) {

        if(shooter.hasEffect(TinkersArchery.groovyEffect.get())) {

            int effectLevel = TinkersArchery.groovyEffect.get().getLevel(shooter) + 1;

            return drawSpeed * (1 + effectLevel * level * 0.1f);

        } else {

            return drawSpeed;
        }
    }

    @Override
    public void onReleaseBow(IModifierToolStack tool, int level, float drawPortion, float power, float accuracy, List<ArrowData> arrows, World world, LivingEntity shooter) {
        if(drawPortion > 0.75) {
            int effectLevel = Math.min(5 + (level - 1) * 3, TinkersArchery.groovyEffect.get().getLevel(shooter) + 1);
            TinkersArchery.groovyEffect.get().apply(shooter, 5 * 20, effectLevel);
        }
    }
}
