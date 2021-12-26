package tonite.tinkersarchery.library.modifier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

import java.util.List;

public interface IBowModifier {

    /**
     * Called immediately before the arrow is shot out of the bow to determine power
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param drawPortion    The portion the bow was drawn
     * @param power          The current power
     * @param world          World containing tool
     * @param shooter        Entity who shot tool
     * @return               The new power
     */
    default float getPower(IModifierToolStack tool, int level, float drawPortion, float power, World world, LivingEntity shooter) { return power; }

    /**
     * Called immediately before the arrow is shot out of the bow to determine accuracy
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param drawPortion    The portion the bow was drawn
     * @param baseAccuracy   The base accuracy of the bow
     * @param accuracy       The current accuracy
     * @param world          World containing tool
     * @param shooter        Entity who shot tool
     * @return               The new accuracy
     */
    default float getAccuracy(IModifierToolStack tool, int level, float drawPortion, float baseAccuracy, float accuracy, World world, LivingEntity shooter) { return accuracy; }

    /**
     * Called when the bow is started to be drawn to determine draw speed. This can also be used to do other things at this time that is not necessarily drawSpeed related
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param baseDrawSpeed   The base drawSpeed of the bow
     * @param drawSpeed      The current drawSpeed
     * @param world          World containing tool
     * @param shooter        Entity who shot tool
     * @return               The new power
     */
    default float getDrawSpeed(IModifierToolStack tool, int level, float baseDrawSpeed, float drawSpeed, World world, LivingEntity shooter) { return drawSpeed; }

    /**
     * Called some point during drawing the bow to determine the number of additional arrows
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param drawPortion    The portion the bow was drawn
     * @param world          World containing tool
     * @param shooter        Entity who shot tool
     * @return               How many arrows to add
     */
    default int getArrowCount(IModifierToolStack tool, int level, float drawPortion, World world, LivingEntity shooter) { return 0; }


    /**
     * Called immediately before the arrow is shot out of the bow to determine the directions of additional arrows.
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param drawPortion    The portion the bow was drawn
     * @param power          The power the bow had when released
     * @param accuracy       The accuracy the bow had when released
     * @param arrows         The current set of arrows being shot from the bow. You can add items to this list to have the bow shoot more arrows. The number of items in this array must equal that from getNum.
     * @param numArrows      The number of arrows this modifier should add.
     * @param world          World containing tool
     * @param shooter        Entity who shot tool
     */
    default void onReleaseBow(IModifierToolStack tool, int level, float drawPortion, float power, float accuracy, List<ArrowData> arrows, int numArrows, World world, LivingEntity shooter) {}

    /**
     * Called for every arrow shot out of the bow. This is different from onBowReleased in that onBowReleased is only called once, but this can be called more than once.
     * @param tool           Current tool instance
     * @param level          Modifier level
     * @param arrow          Arrow shot from bow
     * @param drawPortion    The portion the bow was drawn
     * @param world          World containing tool
     * @param shooter        Entity who shot tool
     */
    default void onArrowShot(IModifierToolStack tool, int level, ProjectileEntity arrow, float drawPortion, float power, World world, LivingEntity shooter) {}

    class ArrowData {

        public Quaternion direction;
        public float power;
        public float accuracy;

        public ArrowData(Quaternion direction, float power, float accuracy) {
            this.direction = direction;
            this.power = power;
            this.accuracy = accuracy;
        }

    }
}
