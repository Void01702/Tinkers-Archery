package tonite.tinkersarchery.entities;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.library.modifier.IProjectileModifier;
import tonite.tinkersarchery.library.ProjectileTrajectory;
import tonite.tinkersarchery.library.TinkersArcheryRegistries;
import tonite.tinkersarchery.library.projectileinterfaces.ICriticalProjectile;
import tonite.tinkersarchery.library.projectileinterfaces.IDamageProjectile;
import tonite.tinkersarchery.library.projectileinterfaces.IPiercingProjectile;
import tonite.tinkersarchery.library.projectileinterfaces.IWaterInertiaProjectile;
import tonite.tinkersarchery.library.util.ProjectileAttackUtil;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TinkersArrowEntity extends ProjectileEntity implements IEntityAdditionalSpawnData, ICriticalProjectile, IDamageProjectile, IPiercingProjectile, IWaterInertiaProjectile {

    private ProjectileTrajectory trajectory;
    private Object trajectoryData;
    private Vector3d originalDirection;
    private int numTicks = 0;
    // As much as I didn't want to, I had to store the item itself. Stupid TConstruct and their stupid lack of IModDataReadOnly.toNBT.
    private ItemStack arrowStack;
    private ItemStack bowStack;

    private int pierceLevel = 0;
    private float bonusDamage = 0;
    private boolean critical = false;
    private float waterInertia = 0.6f;

    private ToolStack toolStack;
    private StatsNBT stats;
    private List<ModifierEntry> projectileModifierList;
    private ToolStack bowToolStack;

    protected boolean inGround;
    protected int inGroundTime;
    public int shakeTime = 0;

    private IntOpenHashSet piercingIgnoreEntityIds = null;
    private List<Entity> piercedAndKilledEntities = null;

    private SoundEvent soundEvent = this.getDefaultHitGroundSoundEvent();

    @Nullable
    private BlockState lastState;

    public TinkersArrowEntity(EntityType<? extends TinkersArrowEntity> entityType, World world) {
        super(entityType, world);

        arrowStack = null;
        toolStack = null;
        stats = StatsNBT.EMPTY;
        projectileModifierList = null;
    }

    public TinkersArrowEntity(double xPos, double yPos, double zPos, World world) {
        this(TinkersArchery.TINKERS_ARROW.get(), world);
        this.setPos(xPos, yPos, zPos);
    }

    public TinkersArrowEntity(World world, LivingEntity shooter) {
        this(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ(), world);
        this.setOwner(shooter);
    }

    @Override
    public void shoot(double xDirection, double yDirection, double zDirection, float power, float inaccuracy){

        super.shoot(xDirection, yDirection, zDirection, power, inaccuracy);

        originalDirection = getDeltaMovement();
        numTicks = 0;
    }

    public void changeDirection(Vector3d direction) {
        super.setDeltaMovement(direction);
        originalDirection = direction;
    }

    @Override
    public boolean isNoGravity() { return true; }

    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.put("OriginalDirection", this.newDoubleList(originalDirection.x, originalDirection.y, originalDirection.z));

        nbt.put("Tool", arrowStack.serializeNBT());
        nbt.put("Bow", bowStack.serializeNBT());

        if (trajectory != null && TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsValue(trajectory)) {
            nbt.putString("Trajectory", TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getKey(trajectory).toString());
        } else {
            nbt.putString("Trajectory", TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey().toString());
        }

        CompoundNBT trajectoryDataNBT = new CompoundNBT();
        trajectory.save(trajectoryDataNBT, originalDirection, toolStack.getStats().getFloat(BowAndArrowToolStats.WEIGHT), trajectoryData);
        nbt.put("TrajectoryData", trajectoryDataNBT);

        nbt.putInt("TickCount", numTicks);

        nbt.putInt("PierceLevel", pierceLevel);
        nbt.putFloat("BonusDamage", bonusDamage);
        nbt.putBoolean("Critical", critical);
        nbt.putFloat("WaterInertia", waterInertia);

    }

    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);

        ListNBT directionList = nbt.getList("OriginalDirection", 6);
        originalDirection = new Vector3d(directionList.getDouble(0), directionList.getDouble(1), directionList.getDouble(2));

        setTool(ItemStack.of(nbt.getCompound("Tool")));
        setBow(ItemStack.of(nbt.getCompound("Bow")));

        String trajectoryString = nbt.getString("Trajectory");

        ResourceLocation trajectoryId = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey();

        if (trajectoryString != null && ResourceLocation.isValidResourceLocation(trajectoryString)) {
            trajectoryId = ResourceLocation.tryParse(trajectoryString);

            if ( !TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsKey(trajectoryId)) {
                trajectoryId = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey();
            }
        }

        trajectory = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getValue(trajectoryId);

        trajectory.load(originalDirection, toolStack.getStats().getFloat(BowAndArrowToolStats.WEIGHT), trajectoryData, nbt.getCompound("TrajectoryData"));

        numTicks = nbt.getInt("TickCount");

        pierceLevel = nbt.getInt("PierceLevel");
        bonusDamage = nbt.getFloat("BonusDamage");
        critical = nbt.getBoolean("Critical");
        waterInertia = nbt.getFloat("WaterInertia");
    }

    @Override
    protected void defineSynchedData() {}

    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.setPos(p_180426_1_, p_180426_3_, p_180426_5_);
        this.setRot(p_180426_7_, p_180426_8_);
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpMotion(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
        super.lerpMotion(p_70016_1_, p_70016_3_, p_70016_5_);
    }

    @Override
    public void setWaterInertia(float waterInertia) {
        this.waterInertia = waterInertia;
    }

    public float getWaterInertia() {
        return waterInertia;
    }

    private Vector3d getArrowMotion() {
        float weight = stats.getFloat(BowAndArrowToolStats.WEIGHT);

        try {
            return trajectory.getMotionDirection(numTicks, originalDirection, weight, trajectoryData);
        } catch (Exception e) {
            trajectoryData = trajectory.onCreated(originalDirection, weight);
            return trajectory.getMotionDirection(numTicks, originalDirection, weight, trajectoryData);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.shakeTime > 0) {
            --this.shakeTime;
        }

        if (this.isInWaterOrRain()) {
            this.clearFire();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockpos);
        if (!blockstate.isAir(this.level, blockpos)) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
            if (!voxelshape.isEmpty()) {
                Vector3d vector3d1 = this.position();

                for(AxisAlignedBB axisalignedbb : voxelshape.toAabbs()) {
                    if (axisalignedbb.move(blockpos).contains(vector3d1)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (inGround) {

            if (this.lastState != blockstate && this.shouldFall()) {
                this.startFalling();
            } else if (!this.level.isClientSide) {
                this.tickDespawn();
            }

            ++this.inGroundTime;

            for (ModifierEntry m: projectileModifierList) {
                ((IProjectileModifier)m.getModifier()).onProjectileGroundTick(toolStack, m.getLevel(), this);
            }

        } else {

            numTicks++;

            if (trajectory != null && originalDirection != null) {
                Vector3d arrowMotion = getArrowMotion();

                if (this.isInWater()) {
                    for(int j = 0; j < 4; ++j) {
                        float f4 = 0.25F;
                        this.level.addParticle(ParticleTypes.BUBBLE, getX() - arrowMotion.x * 0.25D, getY() - arrowMotion.y * 0.25D, getZ() - arrowMotion.z * 0.25D, arrowMotion.x, arrowMotion.y, arrowMotion.z);
                    }

                    arrowMotion = arrowMotion.scale(waterInertia);
                }

                setDeltaMovement(arrowMotion);
            }

            Vector3d motion = this.getDeltaMovement();

            this.inGroundTime = 0;
            Vector3d position = this.position();
            Vector3d modifiedPosition = position.add(motion);
            RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(position, modifiedPosition, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                modifiedPosition = raytraceresult.getLocation();
            }

            while (!this.removed) {
                EntityRayTraceResult entityraytraceresult = this.findHitEntity(position, modifiedPosition);
                if (entityraytraceresult != null) {
                    raytraceresult = entityraytraceresult;
                }

                if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
                    Entity entity = ((EntityRayTraceResult) raytraceresult).getEntity();
                    Entity entity1 = this.getOwner();
                    if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity) entity1).canHarmPlayer((PlayerEntity) entity)) {
                        raytraceresult = null;
                        entityraytraceresult = null;
                    }
                }

                if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.hasImpulse = true;
                    if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
                        motion = getDeltaMovement();
                    }
                }

                if (entityraytraceresult == null || this.getPierceLevel() <= 0) {
                    break;
                }

                raytraceresult = null;
            }

            if (critical) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.CRIT, this.getX() + motion.x * (double)i / 4.0D, this.getY() + motion.y * (double)i / 4.0D, this.getZ() + motion.z * (double)i / 4.0D, -motion.x, -motion.y + 0.2D, -motion.z);
                }
            }

            float f1 = MathHelper.sqrt(getHorizontalDistanceSqr(motion));
            this.yRot = (float) (MathHelper.atan2(motion.x, motion.z) * (double) (180F / (float) Math.PI));
            this.xRot = (float) (MathHelper.atan2(motion.y, f1) * (double) (180F / (float) Math.PI));
            if (this.xRotO == 0f && this.yRotO == 0f) {
                this.xRotO = xRot;
                this.yRotO = yRot;
            } else {
                this.xRot = lerpRotation(this.xRotO, this.xRot);
                this.yRot = lerpRotation(this.yRotO, this.yRot);
            }

            setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);

            for (ModifierEntry m: projectileModifierList) {
                ((IProjectileModifier)m.getModifier()).onProjectileFlyTick(toolStack, m.getLevel(), this);
            }

        }

        for (ModifierEntry m: projectileModifierList) {
            ((IProjectileModifier)m.getModifier()).onProjectileTick(toolStack, m.getLevel(), this);
        }

    }

    private boolean shouldFall() {
        return this.inGround && this.level.noCollision((new AxisAlignedBB(this.position(), this.position())).inflate(0.06D));
    }

    private void startFalling() {
        this.inGround = false;
        Vector3d vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.multiply((double)(this.random.nextFloat() * 0.2F), (double)(this.random.nextFloat() * 0.2F), (double)(this.random.nextFloat() * 0.2F)));
        this.inGroundTime = 0;
        this.tickCount = 0;
    }

    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return ProjectileHelper.getEntityHitResult(this.level, this, p_213866_1_, p_213866_2_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityHit) {
        super.onHitEntity(entityHit);
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
        if (entity1 == null) {
            //damagesource = DamageSource.thrown(this, this);
        } else {
            //damagesource = DamageSource.thrown(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
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
        if (this.level.isClientSide || ProjectileAttackUtil.attackEntity( arrowStack.getItem(), this, toolStack, livingOwner, entity, false, bowToolStack)) {
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

            this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
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

    @Override
    protected void onHitBlock(BlockRayTraceResult BlockRayTraceResult) {
        this.lastState = this.level.getBlockState(BlockRayTraceResult.getBlockPos());
        super.onHitBlock(BlockRayTraceResult);
        Vector3d direction = getDeltaMovement();
        Vector3d positionDifference = BlockRayTraceResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(positionDifference);
        Vector3d vector3d1 = positionDifference.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vector3d1.x, this.getY() - vector3d1.y, this.getZ() - vector3d1.z);
        //this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
        this.setCritical(false);
        this.setPierceLevel(0);
        setTrajectory(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getValue(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey()));
        trajectoryData = trajectory.onCreated(originalDirection, stats.getFloat(BowAndArrowToolStats.WEIGHT));
        /*this.setSoundEvent(SoundEvents.ARROW_HIT);
        this.setShotFromCrossbow(false);*/
        this.resetPiercedEntities();
        for (ModifierEntry m: projectileModifierList) {
            ((IProjectileModifier)m.getModifier()).onProjectileHitBlock(toolStack, m.getLevel(), this, this.lastState, direction);
        }
    }

    private void resetPiercedEntities() {
        if (this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.clear();
        }

        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear();
        }

    }

    protected void tickDespawn() {
        if (this.inGroundTime >= 1200) {
            this.remove();
        }

    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(entity.getId()));
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {

        buffer.writeDouble(originalDirection.x);
        buffer.writeDouble(originalDirection.y);
        buffer.writeDouble(originalDirection.z);

        buffer.writeItem(arrowStack);
        buffer.writeItem(bowStack);

        if (trajectory != null && TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsValue(trajectory)) {
            buffer.writeUtf(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getKey(trajectory).toString());
        } else {
            buffer.writeUtf(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey().toString());
        }

        buffer.writeInt(numTicks);

        CompoundNBT trajectoryNBT = new CompoundNBT();
        trajectory.save(trajectoryNBT, originalDirection, stats.getFloat(BowAndArrowToolStats.WEIGHT), trajectoryData);
        buffer.writeNbt(trajectoryNBT);

        buffer.writeInt(pierceLevel);
        buffer.writeFloat(bonusDamage);
        buffer.writeBoolean(critical);
        buffer.writeFloat(waterInertia);

    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {

        originalDirection = new Vector3d(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());

        setTool(additionalData.readItem());
        setBow(additionalData.readItem());

        String trajectoryString = additionalData.readUtf();

        ResourceLocation trajectoryId = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey();

        if (trajectoryString != null && ResourceLocation.isValidResourceLocation(trajectoryString)) {
            trajectoryId = ResourceLocation.tryParse(trajectoryString);
            if ( !TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.containsKey(trajectoryId)) {
                trajectoryId = TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getDefaultKey();
            }
        }

        setTrajectory(TinkersArcheryRegistries.PROJECTILE_TRAJECTORIES.getValue(trajectoryId));

        numTicks = additionalData.readInt();

        trajectory.load(originalDirection, stats.getFloat(BowAndArrowToolStats.WEIGHT), trajectoryData, additionalData.readNbt());

        pierceLevel = additionalData.readInt();
        bonusDamage = additionalData.readFloat();
        critical = additionalData.readBoolean();
        waterInertia = additionalData.readFloat();

    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        double d0 = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getViewScale();
        return p_70112_1_ < d0 * d0;
    }

    public void setTrajectory(ProjectileTrajectory trajectory) {
        this.trajectory = trajectory;
        trajectoryData = trajectory.onCreated(originalDirection, stats.getFloat(BowAndArrowToolStats.WEIGHT));
    }

    public ProjectileTrajectory getTrajectory() {
        return this.trajectory;
    }

    public void setTool(ItemStack tool){

        projectileModifierList = new ArrayList<>();

        if (tool != null) {
            arrowStack = tool.copy();

            toolStack = ToolStack.from(tool);

            stats = toolStack.getStats();

            for (ModifierEntry m: toolStack.getModifierList()) {
                if (m.getModifier() instanceof IProjectileModifier) {
                    projectileModifierList.add(m);

                    ((IProjectileModifier)m.getModifier()).onArrowLoaded(toolStack, m.getLevel(), this);
                }
            }
        } else {
            arrowStack = null;

            toolStack = null;

            stats = StatsNBT.EMPTY;
        }


    }

    public void setBow(ItemStack bow){

        if (bow != null) {
            bowStack = bow.copy();

            toolStack = ToolStack.from(bow);
        } else {
            bowStack = null;

            toolStack = null;
        }


    }

    public Vector3d getOriginalDirection() {
        return originalDirection;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    @Override
    public void setPierceLevel(int pierceLevel) {
        this.pierceLevel = pierceLevel;
    }

    @Override
    public int getPierceLevel(){
        return pierceLevel;
    }

    @Override
    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    @Override
    public boolean getCritical() {
        return critical;
    }

    @Override
    public void setDamage(float damage) {
        bonusDamage = damage;
    }

    @Override
    public float getDamage() {

        float result = stats.getFloat(ToolStats.ATTACK_DAMAGE) + bonusDamage;

        float speed = (float)this.getDeltaMovement().length();
        result = (float)MathHelper.clamp((double)speed * result, 0.0D, 2.147483647E9D);

        if (critical) {
            float j = this.random.nextFloat() * (result / 2 + 2);
            result += j;
        }

        return result;
    }
}
