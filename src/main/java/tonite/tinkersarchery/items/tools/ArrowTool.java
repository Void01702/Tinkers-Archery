package tonite.tinkersarchery.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.library.IProjectileItem;

public class ArrowTool extends ModifiableItem implements IProjectileItem {

    public static final ToolType TOOL_TYPE = ToolType.get("arrow");

    public ArrowTool(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public int consumeAmmo(ItemStack ammo, int amount, LivingEntity player) {
        CompoundNBT tag = ammo.getTag();

        if (tag.contains("infinity") && tag.getBoolean("infinity")) {
            return amount;
        }
        if (ammo.getTag().contains("count")) {
            return Math.min(ammo.getTag().getInt("count"), amount);
        } else {
            return 0;
        }
    }

    @Override
    public int getAmmo(ItemStack ammo, LivingEntity player) {
        return 0;
    }

    @Override
    public ProjectileEntity createProjectile(ItemStack ammo, World world, LivingEntity player, ItemStack bow) {
        TinkersArrowEntity arrow = new TinkersArrowEntity(world, player);

        return arrow;
    }

    @Override
    public void supplyInfoToProjectile(ProjectileEntity projectile, ItemStack ammo, World world, LivingEntity shooter, ItemStack bow) {
        ((TinkersArrowEntity)projectile).setTool(ammo);
    }
}
