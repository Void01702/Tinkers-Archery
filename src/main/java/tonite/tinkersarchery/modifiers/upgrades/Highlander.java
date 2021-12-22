package tonite.tinkersarchery.modifiers.upgrades;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.library.IBowModifier;

public class Highlander extends Modifier implements IBowModifier {
    public Highlander() {
        super(0xFFFAFF99);
    }

    public float getPower(IModifierToolStack tool, int level, float drawPortion, float power, World world, LivingEntity shooter) {

        int powerIncreaseHeight = world.getMaxBuildHeight() / 2;
        int powerIncreasePortion = world.getMaxBuildHeight() - powerIncreaseHeight;

        float multiplier;

        if (shooter.getY() <= powerIncreaseHeight) {
            multiplier = 0;
        } else {
            if (shooter.getY() < powerIncreaseHeight + powerIncreasePortion) {
                multiplier = ((float)shooter.getY() - powerIncreaseHeight) / powerIncreasePortion;
            } else {
                multiplier = 1;
            }
        }

        return power * (1 + level * multiplier);
    }
}
