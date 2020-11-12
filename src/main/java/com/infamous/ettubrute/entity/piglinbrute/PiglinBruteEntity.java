package com.infamous.ettubrute.entity.piglinbrute;

import com.google.common.collect.ImmutableList;
import com.infamous.ettubrute.mod.ModSensorTypes;
import com.infamous.ettubrute.mod.ModEntityTypes;
import com.infamous.ettubrute.entity.config.EtTuBruteConfig;
import com.infamous.ettubrute.entity.ziglinbrute.ZiglinBruteEntity;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TieredItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class PiglinBruteEntity extends MonsterEntity {
    protected static final DataParameter<Boolean> IS_IMMUNNE_TO_ZOMBIFICATION = EntityDataManager.createKey(PiglinBruteEntity.class, DataSerializers.BOOLEAN);
    protected int timeInOverworld = 0;
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(PiglinBruteEntity.class, DataSerializers.BOOLEAN);
    private static final UUID BABY_SPEED_BOOST_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_UUID, "Baby speed boost", (double)0.2F, AttributeModifier.Operation.MULTIPLY_BASE);


    protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinBruteEntity>>> SENSOR_TYPES;
    protected static final ImmutableList MEMORY_MODULES;

    public PiglinBruteEntity(EntityType<? extends PiglinBruteEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
        this.func_242339_eS();
        this.setPathPriority(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.experienceValue = 20;
    }

    public static boolean canPiglinBruteSpawn(EntityType<PiglinBruteEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && world.getBlockState(blockPos.down()).getBlock() != Blocks.NETHER_WART_BLOCK;
    }

    private void func_242339_eS() {
        if (this.getNavigator() instanceof GroundPathNavigator) {
            ((GroundPathNavigator)this.getNavigator()).setBreakDoors(true);
        }

    }

    public PiglinBruteEntity(World world) {
        super(ModEntityTypes.PIGLIN_BRUTE.get(), world);
        this.experienceValue = 20;
    }


    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_CHILD, false);
        this.dataManager.register(IS_IMMUNNE_TO_ZOMBIFICATION, false);
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (IS_CHILD.equals(key)) {
            this.recalculateSize();
        }

    }
    public void setImmuneToZombification(boolean immuneToZombification) {
        this.getDataManager().set(IS_IMMUNNE_TO_ZOMBIFICATION, immuneToZombification);
    }

    protected boolean getImmuneToZombification() {
        return this.getDataManager().get(IS_IMMUNNE_TO_ZOMBIFICATION);
    }

    public boolean canZombify() {
        return !this.world.func_230315_m_().func_241509_i_() && !this.getImmuneToZombification() && !this.isAIDisabled();
    }

    protected void zombify(ServerWorld serverWorld) {
        if(EtTuBruteConfig.COMMON.ENABLE_ZIGLIN_BRUTES.get() && EtTuBruteConfig.COMMON.ENABLE_BRUTES_BECOME_ZIGLIN_BRUTES.get()){
            ZiglinBruteEntity ziglinBruteEntity = (ZiglinBruteEntity)this.func_233656_b_(ModEntityTypes.ZIGLIN_BRUTE.get());
            if (ziglinBruteEntity != null) {
                ziglinBruteEntity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
            }
        }
        else{
            ZombifiedPiglinEntity zombifiedPiglinEntity = (ZombifiedPiglinEntity)this.func_233656_b_(EntityType.ZOMBIFIED_PIGLIN);
            if (zombifiedPiglinEntity != null) {
                zombifiedPiglinEntity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
            }
        }
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                // health
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
                // movement speed
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
                // attack damage
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT compoundNBT) {
        PiglinBruteBrain.setHome(this);


        if (spawnReason != SpawnReason.STRUCTURE) {
            if (world.getRandom().nextFloat() < 0.2F
                    && (EtTuBruteConfig.COMMON.ENABLE_BABY_BRUTES.get() && EtTuBruteConfig.COMMON.ENABLE_RANDOM_BABY_BRUTES.get())) {
                this.setChild(true);
            }
            else if (!this.isChild() || EtTuBruteConfig.COMMON.ENABLE_BABY_BRUTES_ARMED.get()) {
                this.setEquipmentBasedOnDifficulty(difficultyInstance);
            }
        }
        else{
            if (world.getRandom().nextFloat() < 0.2F
                    && (EtTuBruteConfig.COMMON.ENABLE_BABY_BRUTES.get() && EtTuBruteConfig.COMMON.ENABLE_BASTION_BABY_BRUTES.get())) {
                this.setChild(true);
            }
            else if (!this.isChild() || EtTuBruteConfig.COMMON.ENABLE_BABY_BRUTES_ARMED.get()) {
                this.setEquipmentBasedOnDifficulty(difficultyInstance);
            }
        }

        return super.onInitialSpawn(world, difficultyInstance, spawnReason, livingEntityData, compoundNBT);
    }

    public void setChild(boolean childZombie) {
        if(EtTuBruteConfig.COMMON.ENABLE_BABY_BRUTES.get()){
            this.getDataManager().set(IS_CHILD, childZombie);
            if (!this.world.isRemote) {
                ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.removeModifier(BABY_SPEED_BOOST);
                }
                if (childZombie) {
                    if (modifiableattributeinstance != null) {
                        modifiableattributeinstance.applyPersistentModifier(BABY_SPEED_BOOST);
                    }
                }
            }
        }

    }

    public boolean isChild() {
        if(EtTuBruteConfig.COMMON.ENABLE_BABY_BRUTES.get()){
            return this.getDataManager().get(IS_CHILD);
        }
        return false;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
    }

    protected Brain.BrainCodec<PiglinBruteEntity> func_230289_cH_() {
        return Brain.func_233705_a_(MEMORY_MODULES,
                SENSOR_TYPES);
    }

    protected Brain<?> createBrain(Dynamic<?> dynamic) {
        return PiglinBruteBrain.initializeBrain(this, this.func_230289_cH_().func_233748_a_(dynamic));
    }

    public Brain<PiglinBruteEntity> getBrain() {
        return (Brain<PiglinBruteEntity>) super.getBrain();
    }

    public boolean func_230293_i_(ItemStack stack) {
        return stack.getItem() == Items.GOLDEN_AXE && super.func_230293_i_(stack);
    }

    protected void updateAITasks() {
        this.world.getProfiler().startSection("piglinBruteBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().endSection();
        PiglinBruteBrain.setAggroed(this);
        PiglinBruteBrain.activateFight(this);


        if (this.canZombify()) {
            ++this.timeInOverworld;
        } else {
            this.timeInOverworld = 0;
        }

        if (this.timeInOverworld > 300) {
            this.playZombificationSound();
            this.zombify((ServerWorld)this.world);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public PiglinEntity.Action getAction() {
        return this.isAggressive() && this.hasMeleeWeapon() ? PiglinEntity.Action.ATTACKING_WITH_MELEE_WEAPON : PiglinEntity.Action.DEFAULT;
    }

    private boolean hasMeleeWeapon() {
        return this.getHeldItemMainhand().getItem() instanceof TieredItem;
    }

    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        boolean canAttackEntityFrom = super.attackEntityFrom(damageSource, amount);
        if (this.world.isRemote) {
            return false;
        } else {
            if (canAttackEntityFrom && damageSource.getTrueSource() instanceof LivingEntity) {
                PiglinBruteBrain.makeAngryAt(this, (LivingEntity)damageSource.getTrueSource());
            }

            return canAttackEntityFrom;
        }
    }


    @Override
    public void writeAdditional(CompoundNBT compoundNBT) {
        super.writeAdditional(compoundNBT);

        if (this.isChild()) {
            compoundNBT.putBoolean("IsBaby", true);
        }
        if (this.getImmuneToZombification()) {
            compoundNBT.putBoolean("IsImmuneToZombification", true);
        }

        compoundNBT.putInt("TimeInOverworld", this.timeInOverworld);
    }

    @Override
    public void readAdditional(CompoundNBT compoundNBT) {
        super.readAdditional(compoundNBT);
        this.setChild(compoundNBT.getBoolean("IsBaby"));
        this.setImmuneToZombification(compoundNBT.getBoolean("IsImmuneToZombification"));
        this.timeInOverworld = compoundNBT.getInt("TimeInOverworld");
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PIGLIN_AMBIENT;
        //return SoundEvents.field_242132_lc;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_PIGLIN_HURT;
        //return SoundEvents.field_242135_lf;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PIGLIN_DEATH;
        //return SoundEvents.field_242134_le;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.ENTITY_PIGLIN_STEP, 0.15F, 1.0F);
        //this.playSound(SoundEvents.field_242136_lg, 0.15F, 1.0F);
    }

    protected void playFightSound() {
        this.playSound(SoundEvents.ENTITY_PIGLIN_ANGRY, 1.0F, this.getSoundPitch());
        //this.playSound(SoundEvents.field_242133_ld, 1.0F, this.getSoundPitch());
    }

    protected void playZombificationSound() {
        this.playSound(SoundEvents.ENTITY_PIGLIN_CONVERTED_TO_ZOMBIFIED, 1.0F, this.getSoundPitch());
        //this.playSound(SoundEvents.field_242137_lh, 1.0F, this.getSoundPitch());
    }

    static {
        SENSOR_TYPES = ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.field_234129_b_,
                SensorType.HURT_BY,
                (SensorType) ModSensorTypes.PIGLIN_BRUTE_SPECIFIC_SENSOR.get()); // Allowed, since it is registered as a sensor type
        MEMORY_MODULES = ImmutableList.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.OPENED_DOORS,
                MemoryModuleType.MOBS,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
                MemoryModuleType.NEAREST_ADULT_PIGLINS,
                MemoryModuleType.HURT_BY,
                MemoryModuleType.HURT_BY_ENTITY,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.PATH,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                MemoryModuleType.HOME);
    }
}
