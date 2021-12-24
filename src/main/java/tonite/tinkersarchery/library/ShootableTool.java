package tonite.tinkersarchery.library;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import tonite.tinkersarchery.library.projectileinterfaces.ICriticalProjectile;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ShootableTool extends ModifiableItem {
    public ShootableTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return this.getAllSupportedProjectiles();
    }

    public abstract Predicate<ItemStack> getAllSupportedProjectiles();

    public static ItemStack getProjectile(PlayerEntity player, ItemStack bowItemStack) {
        if (!(bowItemStack.getItem() instanceof ShootableTool)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableTool) bowItemStack.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(player, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                predicate = ((ShootableTool) bowItemStack.getItem()).getAllSupportedProjectiles();

                for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
                    ItemStack itemstack1 = player.inventory.getItem(i);
                    if (predicate.test(itemstack1)) {
                        return itemstack1;
                    }
                }

                return player.abilities.instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }

    public void shootBow(ItemStack bow, World world, LivingEntity shooter, float drawPortion, ItemStack arrowItem){

        float powerModifier = 1f;
        float accuracyModifier = 1f;

        ToolStack tool = null;

        List<ModifierEntry> modifierList = new ArrayList<>();

        if(bow.hasTag()) {
            tool = ToolStack.from(bow);
            powerModifier = tool.getStats().getFloat(BowAndArrowToolStats.ELASTICITY);
            accuracyModifier = tool.getStats().getFloat(BowAndArrowToolStats.ACCURACY);

            for (ModifierEntry m : tool.getModifierList()) {
                if (m.getModifier() instanceof IBowModifier){
                    modifierList.add(m);
                }
            }
        }

        float power = drawPortion * powerModifier;
        float accuracy = accuracyModifier;

        List<IBowModifier.ArrowData> arrows = new ArrayList<>();

        for (ModifierEntry entry : modifierList) {
            power = ((IBowModifier) entry.getModifier()).getPower(tool, entry.getLevel(), drawPortion, power, world, shooter);
            accuracy = ((IBowModifier) entry.getModifier()).getAccuracy(tool, entry.getLevel(), drawPortion, accuracyModifier, accuracy, world, shooter);
        }

        arrows.add(new IBowModifier.ArrowData(Quaternion.ONE, power, accuracy));

        for (ModifierEntry entry : modifierList) {
            ((IBowModifier) entry.getModifier()).onReleaseBow(tool, entry.getLevel(), drawPortion, power, accuracy, arrows, world, shooter);
        }

        Vector3f motion = new Vector3f( shooter.getDeltaMovement() );
        motion = new Vector3f(motion.x(), shooter.isOnGround() ? 0.0f : motion.y(), motion.z());

        Vector3f lookingDirection = new Vector3f( shooter.getViewVector(1.0F) );

        ArrowItem arrowitem = (ArrowItem) (arrowItem.getItem() instanceof ArrowItem ? arrowItem.getItem() : Items.ARROW);
        for (IBowModifier.ArrowData data: arrows) {

            Vector3f arrowDirection = lookingDirection.copy();
            arrowDirection.transform(data.direction);
            arrowDirection.add(motion);

            ProjectileEntity projectile = createArrow(bow, world, arrowDirection, drawPortion, shooter, arrowItem);

            projectile.shoot(arrowDirection.x(), arrowDirection.y(), arrowDirection.z(), power * 3.0F, 1.0F / accuracy);

            supplyInfoToArrow(projectile, bow, world, new Vector3f(projectile.getDeltaMovement()), drawPortion, shooter, arrowItem);

            for (ModifierEntry entry : modifierList) {
                ((IBowModifier) entry.getModifier()).onArrowShot(tool, entry.getLevel(), projectile, drawPortion, power, world, shooter);
            }

            world.addFreshEntity(projectile);
        }
    }

    public ProjectileEntity createArrow(ItemStack bow, World world, Vector3f direction, float drawPortion, LivingEntity shooter, ItemStack arrowItem) {

        if (arrowItem.getItem() instanceof IProjectileItem) {
            return ((IProjectileItem)arrowItem.getItem()).createProjectile(arrowItem, world, shooter, bow);
        }

        ArrowItem arrowItemClass;

        if (arrowItem.getItem() instanceof ArrowItem) {
            arrowItemClass = (ArrowItem)arrowItem.getItem();
        } else {
            arrowItemClass = (ArrowItem)Items.ARROW;
        }

        AbstractArrowEntity arrow = arrowItemClass.createArrow(world, arrowItem, shooter);

        if (drawPortion == 1.0F) {
            arrow.setCritArrow(true);
        }

        if (shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.instabuild) {
            arrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
        }

        return arrow;
    }

    public void supplyInfoToArrow(ProjectileEntity projectile, ItemStack bow, World world, Vector3f direction, float drawPortion, LivingEntity shooter, ItemStack arrowItem) {

        if (projectile instanceof ICriticalProjectile && drawPortion == 1.0F) {
            ((ICriticalProjectile)projectile).setCritical(true);
        }

        if (arrowItem.getItem() instanceof IProjectileItem) {
            ((IProjectileItem)arrowItem.getItem()).supplyInfoToProjectile(projectile, arrowItem, world, shooter, bow);
        }

        if (projectile instanceof AbstractArrowEntity) {
            AbstractArrowEntity arrow = (AbstractArrowEntity)projectile;

            if (drawPortion == 1.0F) {
                arrow.setCritArrow(true);
            }

            if (shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.instabuild) {
                arrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
            }
        }
    }
}
