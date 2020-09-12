package com.infamous.ettubrute.entity.piglinbrute;

import com.google.common.collect.ImmutableList;
import com.infamous.ettubrute.entity.ModEntityTypes;
import com.infamous.ettubrute.entity.config.EtTuBruteConfig;
import com.infamous.ettubrute.entity.ziglinbrute.ZiglinBruteEntity;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public class PiglinBruteEntity extends AbstractPiglinEntity {
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(PiglinBruteEntity.class, DataSerializers.BOOLEAN);
    private static final UUID BABY_SPEED_BOOST_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_UUID, "Baby speed boost", (double)0.2F, AttributeModifier.Operation.MULTIPLY_BASE);


    protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinBruteEntity>>> SENSOR_TYPES;
    protected static final ImmutableList MEMORY_MODULES;

    public PiglinBruteEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 20;
    }

    public PiglinBruteEntity(World world) {
        super(ModEntityTypes.PIGLIN_BRUTE.get(), world);
        this.experienceValue = 20;
    }


    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_CHILD, false);
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (IS_CHILD.equals(key)) {
            this.recalculateSize();
        }

    }

    @Override
    protected void zombify(ServerWorld serverWorld) {
        if(EtTuBruteConfig.COMMON.ENABLE_ZIGLIN_BRUTES.get() && EtTuBruteConfig.COMMON.ENABLE_BRUTES_BECOME_ZIGLIN_BRUTES.get()){
            ZiglinBruteEntity ziglinBruteEntity = (ZiglinBruteEntity)this.func_233656_b_(ModEntityTypes.ZIGLIN_BRUTE.get());
            if (ziglinBruteEntity != null) {
                ziglinBruteEntity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
            }
        }
        else{
            ZombifiedPiglinEntity zombifiedPiglinEntity = (ZombifiedPiglinEntity)this.func_233656_b_(EntityType.field_233592_ba_);
            if (zombifiedPiglinEntity != null) {
                zombifiedPiglinEntity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
            }
        }
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                // health
                .func_233815_a_(Attributes.field_233818_a_, 50.0D)
                // movement speed
                .func_233815_a_(Attributes.field_233821_d_, 0.3499999940395355D)
                // attack damage
                .func_233815_a_(Attributes.field_233823_f_, 7.0D);
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
                ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.field_233821_d_);
                modifiableattributeinstance.removeModifier(BABY_SPEED_BOOST);
                if (childZombie) {
                    modifiableattributeinstance.func_233767_b_(BABY_SPEED_BOOST);
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

    public boolean func_234422_eK_() {
        return false;
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
        super.updateAITasks();
    }

    @OnlyIn(Dist.CLIENT)
    public PiglinEntity.Action getAction() {
        return this.isAggressive() && this.func_234434_eY_() ? PiglinEntity.Action.ATTACKING_WITH_MELEE_WEAPON : PiglinEntity.Action.DEFAULT;
    }

    private boolean func_234434_eY_() {
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

    protected SoundEvent getAmbientSound() {
        return SoundEvents.field_232788_kS_;
        //return SoundEvents.field_242132_lc;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.field_232793_kX_;
        //return SoundEvents.field_242135_lf;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.field_232791_kV_;
        //return SoundEvents.field_242134_le;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.field_232795_kZ_, 0.15F, 1.0F);
        //this.playSound(SoundEvents.field_242136_lg, 0.15F, 1.0F);
    }

    protected void playFightSound() {
        this.playSound(SoundEvents.field_232789_kT_, 1.0F, this.getSoundPitch());
        //this.playSound(SoundEvents.field_242133_ld, 1.0F, this.getSoundPitch());
    }

    protected void playZombificationSound() {
        this.playSound(SoundEvents.field_232799_la_, 1.0F, this.getSoundPitch());
        //this.playSound(SoundEvents.field_242137_lh, 1.0F, this.getSoundPitch());
    }

    static {
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.field_234129_b_, SensorType.HURT_BY, (SensorType)ModSensorTypes.PIGLIN_BRUTE_SPECIFIC_SENSOR.get());
        MEMORY_MODULES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.field_225462_q, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.field_234102_l_, MemoryModuleType.field_234090_X_, MemoryModuleType.field_234089_W_, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.field_234103_o_, MemoryModuleType.field_234104_p_, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.field_234078_L_, MemoryModuleType.field_234077_K_, MemoryModuleType.HOME);
    }
}
