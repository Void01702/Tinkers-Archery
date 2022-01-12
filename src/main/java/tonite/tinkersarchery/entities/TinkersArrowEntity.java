package tonite.tinkersarchery.entities;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.ITinkersConsumableItem;
import tonite.tinkersarchery.library.ProjectileTrajectory;
import tonite.tinkersarchery.library.TinkersArcheryRegistries;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.library.projectileinterfaces.*;
import tonite.tinkersarchery.library.util.ProjectileAttackUtil;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;
import tonite.tinkersarchery.tools.BowAndArrowDefinitions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TinkersArrowEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData, IDamageProjectile, ITrajectoryProjectile, IWeightProjectile, IStoreBowProjectile, IWaterInertiaProjectile {

    private float weightBonus = 0f;
    private float stabilityBonus = 0f;
    private float waterInertia = 0.6f;

    private ProjectileTrajectory trajectory;
    private Object trajectoryData;
    private Vector3d originalDirection;
    private int numTicks = 0;

    private ToolStack toolStack = null;
    private StatsNBT stats = StatsNBT.EMPTY;
    private List<ModifierEntry> projectileModifierList = new ArrayList<>();

    private IntOpenHashSet piercingIgnoreEntityIds = null;
    private List<Entity> piercedAndKilledEntities = null;

    private ItemStack bowStack = ItemStack.EMPTY;
    private ToolStack bowToolStack = null;

    public TinkersArrowEntity(EntityType<? extends TinkersArrowEntity> entityType, World world) {
        super(entityType, world);

        setBaseDamage(0);
    }

    public TinkersArrowEntity(World world, IPosition position, Item from, ToolDefinition toolDefinition, MaterialNBT materials) {
        super(TinkersArchery.TINKERS_ARROW.get(), position.x(), position.y(), position.z(), world);

        setToolStack(ToolStack.createTool(from, toolDefinition, materials.getMaterials()));

        setBaseDamage(0);
    }

    public TinkersArrowEntity(World world, LivingEntity shooter, Item from, ToolDefinition toolDefinition, MaterialNBT materials) {
        super(TinkersArchery.TINKERS_ARROW.get(), shooter, world);

        setToolStack(ToolStack.createTool(from, toolDefinition, materials.getMaterials()));

        setBaseDamage(0);
    }

    @Override
    public void setWaterInertia(float waterInertia) {
        this.waterInertia = waterInertia;
    }

    @Override
    public float getWaterInertiaMultiplier() {
        return waterInertia;
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public void shoot(double xDirection, double yDirection, double zDirection, float power, float inaccuracy){

        super.shoot(xDirection, yDirection, zDirection, power, inaccuracy / stats.getFloat(BowAndArrowToolStats.ACCURACY));

        originalDirection = getDeltaMovement();
        numTicks = 0;

        for(ModifierEntry m: toolStack.getModifierList()) {
            if (m.getModifier() instanceof IProjectileModifier) {
                ((IProjectileModifier) m.getModifier()).onArrowShot(toolStack, m.getLevel(), this, originalDirection, this.getOwner());
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        if (toolStack != null) {
            return toolStack.createStack();
        } else {
            return new ItemStack(Items.AIR);
        }

    }

    private Vector3d getArrowMotion() {
        float weight = calculateWeight();
        float stability = calculateStability();
        float resistance =  isInWater() ? getWaterInertiaMultiplier() : 0.99f;

        try {
            return trajectory.getMotionDirection(numTicks, originalDirection, weight, stability, resistance, trajectoryData);
        } catch (Exception e) {
            trajectoryData = trajectory.onCreated(originalDirection, weight, stability);
            return trajectory.getMotionDirection(numTicks, originalDirection, weight, stability, resistance, trajectoryData);
        }
    }

    @Override
    public void tick() {

        if (!inGround) {
            numTicks++;

            if (trajectory != null && originalDirection != null) {
                setDeltaMovement(getArrowMotion());
            }

            for (ModifierEntry m: projectileModifierList) {
                ((IProjectileModifier)m.getModifier()).onProjectileFlyTick(toolStack, m.getLevel(), this);
            }

        } else {
            for (ModifierEntry m: projectileModifierList) {
                ((IProjectileModifier)m.getModifier()).onProjectileGroundTick(toolStack, m.getLevel(), this);
            }
        }

        super.tick();

        for (ModifierEntry m: projectileModifierList) {
            ((IProjectileModifier)m.getModifier()).onProjectileTick(toolStack, m.getLevel(), this);
        }

    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityHit) {
        Entity entity = entityHit.getEntity();
        if (this.getPierceLevel() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        Entity entity1 = this.getOwner();
        //DamageSource damagesource;
        if (entity1 instanceof LivingEntity) {
            ((LivingEntity)entity1).setLastHurtMob(entity);
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getRemainingFireTicks();
        if (this.isOnFire() && !flag) {
            entity.setSecondsOnFire(5);
        }

        //if (entity instanceof LivingEntity && getOwner() instanceof LivingEntity)
        //    toolStack.getItem().hurtEnemy(arrowStack, (LivingEntity)entity, (LivingEntity)getOwner());

        // TinkersArchery.LOGGER.info(getOwner());

        // Entity owner = getOwner();

        LivingEntity livingOwner = getOwner() instanceof LivingEntity ? (LivingEntity) getOwner() : null;


        //if (entity.hurt(damagesource, (float)damage)) {
        if (this.level.isClientSide || ProjectileAttackUtil.attackEntity( toolStack.getItem(), this, toolStack, livingOwner, entity, false, bowToolStack)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                if (!this.level.isClientSide && this.getPierceLevel() <= 0) {
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                }

                /*if (this.knockback > 0) {
                    Vector3d vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockback * 0.6D);
                    if (vector3d.lengthSqr() > 0.0D) {
                        livingentity.push(vector3d.x, 0.1D, vector3d.z);
                    }
                }

                if (!this.level.isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)entity1).connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.ARROW_HIT_PLAYER, 0.0F));
                }*/

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                /*if (!this.level.isClientSide && entity1 instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entity1;
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, this.piercedAndKilledEntities);
                    } else if (!entity.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, Arrays.asList(entity));
                    }
                }*/
            }

            if (entity instanceof PlayerEntity) {
                this.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            } else {
                this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            }
            if (this.getPierceLevel() <= 0) {
                this.remove();
            }
        } else {
            entity.setRemainingFireTicks(k);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.yRot += 180.0F;
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                /*if (this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }*/

                this.remove();
            }
        }

    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        return super.canHitEntity(p_230298_1_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_230298_1_.getId()));
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult BlockRayTraceResult) {

        for (ModifierEntry m: projectileModifierList) {
            if (!((IProjectileModifier)m.getModifier()).onProjectileHitBlock(toolStack, m.getLevel(), this, lastState, BlockRayTraceResult.getBlockPos(), getDeltaMovement())) return;
        }

        this.lastState = this.level.getBlockState(BlockRayTraceResult.getBlockPos());

        setProjectileTrajectory(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getValue(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey()));
        trajectoryData = trajectory.onCreated(originalDirection, calculateWeight(), calculateStability());
        this.resetPiercedEntities();
        super.onHitBlock(BlockRayTraceResult);
    }

    private void resetPiercedEntities() {
        if (this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.clear();
        }

        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear();
        }

    }

    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.put("Bow", bowStack.serializeNBT());

        nbt.putString("Item", toolStack.getItem().getRegistryName().toString());
        nbt.put("Materials", toolStack.getMaterials().serializeToNBT());

        nbt.putFloat("Weight", weightBonus);
        nbt.putFloat("Stability", stabilityBonus);

        nbt.put("OriginalDirection", this.newDoubleList(originalDirection.x, originalDirection.y, originalDirection.z));

        if (trajectory != null && TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsValue(trajectory)) {
            nbt.putString("Trajectory", TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getKey(trajectory).toString());
        } else {
            nbt.putString("Trajectory", TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey().toString());
        }

        CompoundNBT trajectoryDataNBT = new CompoundNBT();
        trajectory.save(trajectoryDataNBT, originalDirection, calculateWeight(), calculateStability(), trajectoryData);
        nbt.put("TrajectoryData", trajectoryDataNBT);

        nbt.putInt("TickCount", numTicks);


    }

    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);

        setBow(ItemStack.of(nbt.getCompound("Bow")));

        Item originalItem = TinkersArchery.tinkers_arrow.get();

        try {
            originalItem = Registry.ITEM.get(new ResourceLocation(nbt.getString("Item")));
        } catch (RuntimeException runtimeexception) {

        }

        List<IMaterial> materials;

        try {
            materials = MaterialNBT.readFromNBT(nbt.get("Materials")).getMaterials();
        } catch (RuntimeException runtimeexception) {
            materials = new ArrayList<>();
        }

        ToolDefinition toolDefinition;

        if (originalItem instanceof ITinkersConsumableItem) {
            toolDefinition = ((ITinkersConsumableItem)originalItem).getToolDefinition();
        } else {
            toolDefinition = BowAndArrowDefinitions.ARROW;
        }

        setToolStack(ToolStack.createTool(originalItem, toolDefinition, materials));

        weightBonus = nbt.getFloat("Weight");
        stabilityBonus = nbt.getFloat("Stability");

        ListNBT directionList = nbt.getList("OriginalDirection", 6);
        originalDirection = new Vector3d(directionList.getDouble(0), directionList.getDouble(1), directionList.getDouble(2));

        String trajectoryString = nbt.getString("Trajectory");

        ResourceLocation trajectoryId = ResourceLocation.tryParse(trajectoryString);

        if (trajectoryId != null) {
            trajectoryId = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey();
        }

        if ( !TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsKey(trajectoryId)) {
            trajectoryId = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey();
        }

        setProjectileTrajectory(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getValue(trajectoryId));

        trajectory.load(originalDirection, calculateWeight(), calculateStability(), trajectoryData, nbt.getCompound("TrajectoryData"));

        numTicks = nbt.getInt("TickCount");
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {

        buffer.writeItem(bowStack);

        if (toolStack != null) {
            buffer.writeBoolean(true);
            buffer.writeVarInt(Item.getId(toolStack.getItem()));

            buffer.writeInt(toolStack.getMaterialsList().size());

            for (IMaterial material : toolStack.getMaterialsList()) {
                buffer.writeResourceLocation(material.getIdentifier());
            }
        } else {
            buffer.writeBoolean(false);

            buffer.writeInt(0);
        }

        buffer.writeFloat(weightBonus);
        buffer.writeFloat(stabilityBonus);

        buffer.writeDouble(originalDirection.x);
        buffer.writeDouble(originalDirection.y);
        buffer.writeDouble(originalDirection.z);

        if (trajectory != null && TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsValue(trajectory)) {
            buffer.writeUtf(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getKey(trajectory).toString());
        } else {
            buffer.writeUtf(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey().toString());
        }

        buffer.writeInt(numTicks);

        CompoundNBT trajectoryNBT = new CompoundNBT();
        trajectory.save(trajectoryNBT, originalDirection, calculateWeight(), calculateStability(), trajectoryData);
        buffer.writeNbt(trajectoryNBT);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {

        setBow(additionalData.readItem());

        Item originalItem = TinkersArchery.tinkers_arrow.get();

        if (additionalData.readBoolean()) {
            originalItem = Item.byId(additionalData.readVarInt());
        }

        List<IMaterial> materials = new ArrayList<>();

        int size = additionalData.readInt();

        for (int i = 0; i < size; i++) {
            materials.add(MaterialRegistry.getMaterial(new MaterialId(additionalData.readResourceLocation())));
        }

        ToolDefinition toolDefinition;

        if (originalItem instanceof ITinkersConsumableItem) {
            toolDefinition = ((ITinkersConsumableItem)originalItem).getToolDefinition();
        } else {
            toolDefinition = BowAndArrowDefinitions.ARROW;
        }

        setToolStack(ToolStack.createTool(originalItem, toolDefinition, materials));

        weightBonus = additionalData.readFloat();
        stabilityBonus = additionalData.readFloat();

        originalDirection = new Vector3d(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());

        String trajectoryString = additionalData.readUtf();

        ResourceLocation trajectoryId = ResourceLocation.tryParse(trajectoryString);
        if ( trajectoryId != null && !TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsKey(trajectoryId)) {
            trajectoryId = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey();
        }

        setProjectileTrajectory(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getValue(trajectoryId));

        numTicks = additionalData.readInt();

        trajectory.load(originalDirection, calculateWeight(), calculateStability(), trajectoryData, additionalData.readNbt());
    }

    public void setToolStack(ToolStack toolStack) {
        projectileModifierList = new ArrayList<>();

        if (toolStack != null) {
            this.toolStack = toolStack.copy();

            stats = toolStack.getStats();

            for (ModifierEntry m: toolStack.getModifierList()) {
                if (m.getModifier() instanceof IProjectileModifier) {
                    projectileModifierList.add(m);

                    ((IProjectileModifier)m.getModifier()).onArrowLoaded(toolStack, m.getLevel(), this);
                }
            }
        } else {
            this.toolStack = null;

            stats = StatsNBT.EMPTY;
        }
    }

    public IMaterial getMaterial(int index) {
        return toolStack.getMaterial(index);
    }

    public int getNumMaterials() {
        return toolStack.getMaterialsList().size();
    }

    public boolean hasTool() {
        return toolStack != null;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ProjectileTrajectory getProjectileTrajectory() {
        return this.trajectory;
    }

    @Override
    public void setProjectileTrajectory(ProjectileTrajectory trajectory) {
        this.trajectory = trajectory;
        trajectoryData = trajectory.onCreated(originalDirection, calculateWeight(), calculateStability());
    }

    @Override
    public Vector3d getOriginalDirection() {
        return originalDirection;
    }

    @Override
    public void setOriginalDirection(Vector3d originalDirection) {
        this.originalDirection = originalDirection;
    }

    @Override
    public void changeDirection(Vector3d direction) {
        setDeltaMovement(direction);

        originalDirection = direction;

        trajectoryData = trajectory.onCreated(originalDirection, calculateWeight(), calculateStability());
    }

    @Override
    public int getTrajectoryTime() {
        return numTicks;
    }

    @Override
    public void setTrajectoryTime(int ticks) {
        numTicks = ticks;
    }

    @Override
    public void setWeight(float weight) {
        weightBonus = weight;
    }

    @Override
    public float getWeight() {
        return weightBonus;
    }

    @Override
    public float calculateWeight() {
        return stats.getFloat(BowAndArrowToolStats.WEIGHT) + weightBonus;
    }

    @Override
    public void setStability(float stability) {
        stabilityBonus = stability;
    }

    @Override
    public float getStability() {
        return stabilityBonus;
    }

    @Override
    public float calculateStability() {
        return stats.getFloat(BowAndArrowToolStats.STABILITY) + stabilityBonus;
    }

    @Override
    public void setDamage(float damage) {
        setBaseDamage(damage);
    }

    @Override
    public float getDamage() {
        return (float)getBaseDamage();
    }

    @Override
    public float calculateDamage() {

        float result = stats.getFloat(ToolStats.ATTACK_DAMAGE) + getDamage();

        float speed = (float)this.getDeltaMovement().length();
        result = (float) MathHelper.clamp((double)speed * result, 0.0D, 2.147483647E9D);

        if (isCritArrow()) {
            result *= 1.2;
        }

        return result;
    }

    @Override
    public void setBow(ItemStack bow) {
        bowStack = bow;
        bowToolStack = ToolStack.from(bow);
    }
}
