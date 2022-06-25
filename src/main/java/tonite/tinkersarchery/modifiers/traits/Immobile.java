package tonite.tinkersarchery.modifiers.traits;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.modifier.IBowModifier;

import java.util.UUID;

public class Immobile extends Modifier implements IBowModifier {

    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("68ee3026-1d50-4eb4-914e-a8b05fbfdb73");
    private static final String SPEED_MODIFIER_NAME = TinkersArchery.prefix("immobile_speeddown");

    public Immobile() {
        super(0x9EF7DE);
    }

    @Override
    public float getDrawSpeed(IModifierToolStack tool, int level, float baseDrawSpeed, float drawSpeed, World world, LivingEntity shooter) {
        try {
            ModifiableAttributeInstance instance = shooter.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
            if (instance != null) {
                instance.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_UUID, SPEED_MODIFIER_NAME, -0.5 * level, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        } catch (IllegalArgumentException e) {

        }

        return drawSpeed;
    }

    @Override
    public boolean onFinishUsing(IModifierToolStack tool, int level, World world, LivingEntity entity) {
        revertSpeed(entity);

        return false;
    }

    @Override
    public boolean onStoppedUsing(IModifierToolStack tool, int level, World world, LivingEntity entity, int timeLeft) {
        revertSpeed(entity);

        return false;
    }

    @Override
    public void onUnequip(IModifierToolStack tool, int level, EquipmentChangeContext context) {
        if (context.getReplacementTool() == null || context.getReplacementTool().getModifiers().getLevel(this) != level) {
            revertSpeed(context.getEntity());
        }
    }

    private void revertSpeed(LivingEntity entity){
        ModifiableAttributeInstance instance = entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (instance != null) {
            instance.removeModifier(SPEED_MODIFIER_UUID);
        }
    }
}
