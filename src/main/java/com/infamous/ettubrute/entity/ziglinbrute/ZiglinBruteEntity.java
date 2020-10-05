package com.infamous.ettubrute.entity.ziglinbrute;

import com.infamous.ettubrute.mod.ModEntityTypes;
import com.infamous.ettubrute.entity.config.EtTuBruteConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ZiglinBruteEntity extends ZombifiedPiglinEntity {
    private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(ZombieEntity.class, DataSerializers.BOOLEAN);
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier ATTACK_SPEED_MODIFIER;
    private static final RangedInteger field_234350_d_;
    private int field_234345_bu_;
    private static final RangedInteger field_234346_bv_;
    private int field_234347_bw_;
    private UUID aggravatingEntityUUID;
    private static final RangedInteger field_241403_bz_;
    private int field_241401_bA_;

    public ZiglinBruteEntity(World p_i231568_2_) {
        super(ModEntityTypes.ZIGLIN_BRUTE.get(), p_i231568_2_);
    }

    public ZiglinBruteEntity(EntityType<? extends ZombifiedPiglinEntity> entityType, World world) {
        super(entityType, world);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
    }

    public void func_230259_a_(@Nullable UUID uuid) {
        this.aggravatingEntityUUID = uuid;
    }

    public double getYOffset() {
        return this.isChild() ? -0.16D : -0.45D;
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setCallsForHelp(new Class[0]));
        if(EtTuBruteConfig.COMMON.ENABLE_VICIOUS_ZIGLIN_BRUTES.get()){
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, LivingEntity::attackable));
        }
        else{
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        }
        this.targetSelector.addGoal(3, new ResetAngerGoal<>(this, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.func_233666_p_()
                // brute health
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
                // brute movement speed
                //.func_233815_a_(Attributes.field_233821_d_, 0.3499999940395355D)
                // brute attack damage
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)
                // zombie spawn reinforcements
                .createMutableAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.0D)
                // zombie movement speed
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                // zombie attack damage
                //.func_233815_a_(Attributes.field_233823_f_, 5.0D)
                ;
    }

    public boolean isChild() {
        if(EtTuBruteConfig.COMMON.ENABLE_BABY_ZIGLIN_BRUTES.get()){
            return this.getDataManager().get(IS_CHILD);
        }
        return false;
    }

    public void setChild(boolean childZombie) {
        if(EtTuBruteConfig.COMMON.ENABLE_BABY_ZIGLIN_BRUTES.get()){
            this.getDataManager().set(IS_CHILD, childZombie);
            if (this.world != null && !this.world.isRemote) {
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

    protected boolean shouldDrown() {
        return false;
    }

    protected void updateAITasks() {
        ModifiableAttributeInstance attackSpeed = this.getAttribute(Attributes.ATTACK_SPEED);
        if (this.func_233678_J__()) {
            if (attackSpeed != null && !this.isChild() && !attackSpeed.hasModifier(ATTACK_SPEED_MODIFIER)) {
                attackSpeed.applyPersistentModifier(ATTACK_SPEED_MODIFIER);
            }

            this.func_241409_eY_();
        } else if (attackSpeed != null && attackSpeed.hasModifier(ATTACK_SPEED_MODIFIER)) {
            attackSpeed.removeModifier(ATTACK_SPEED_MODIFIER);
        }

        this.func_241359_a_((ServerWorld)this.world, true);
        if (this.getAttackTarget() != null) {
            this.func_241410_eZ_();
        }

        if (this.func_233678_J__()) {
            this.recentlyHit = this.ticksExisted;
        }

        super.updateAITasks();
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        float f = difficultyIn.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);
        if (spawnDataIn == null) {
            spawnDataIn = new ZombieEntity.GroupData(func_241399_a_(worldIn.getRandom()), true);
        }

        if (spawnDataIn instanceof ZombieEntity.GroupData) {
            ZombieEntity.GroupData zombieentity$groupdata = (ZombieEntity.GroupData)spawnDataIn;
            if (zombieentity$groupdata.isChild && EtTuBruteConfig.COMMON.ENABLE_BABY_ZIGLIN_BRUTES.get() && EtTuBruteConfig.COMMON.ENABLE_RANDOM_BABY_ZIGLIN_BRUTES.get()) {
                this.setChild(true);
                if (zombieentity$groupdata.field_241400_b_) {
                    if ((double)worldIn.getRandom().nextFloat() < 0.05D) {
                        List<ChickenEntity> list = worldIn.getEntitiesWithinAABB(ChickenEntity.class, this.getBoundingBox().grow(5.0D, 3.0D, 5.0D), EntityPredicates.IS_STANDALONE);
                        if (!list.isEmpty()) {
                            ChickenEntity chickenentity = list.get(0);
                            chickenentity.setChickenJockey(true);
                            this.startRiding(chickenentity);
                        }
                    } else if ((double)worldIn.getRandom().nextFloat() < 0.05D) {
                        ChickenEntity chickenentity1 = EntityType.CHICKEN.create(this.world);
                        chickenentity1.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
                        chickenentity1.onInitialSpawn(worldIn, difficultyIn, SpawnReason.JOCKEY, (ILivingEntityData)null, (CompoundNBT)null);
                        chickenentity1.setChickenJockey(true);
                        this.startRiding(chickenentity1);
                        worldIn.addEntity(chickenentity1);
                    }
                }
            }

            this.setBreakDoorsAItask(this.canBreakDoors() && this.rand.nextFloat() < f * 0.1F);
            if(this.isChild() && EtTuBruteConfig.COMMON.ENABLE_BABY_ZIGLIN_BRUTES_ARMED.get()){
                this.setEquipmentBasedOnDifficulty(difficultyIn);
                this.setEnchantmentBasedOnDifficulty(difficultyIn);
            }
            else{
                this.setEquipmentBasedOnDifficulty(difficultyIn);
                this.setEnchantmentBasedOnDifficulty(difficultyIn);
            }
        }

        if (this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }

        this.applyAttributeBonuses(f);
        return spawnDataIn;
    }

    private void func_241409_eY_() {
        if (this.field_234345_bu_ > 0) {
            --this.field_234345_bu_;
            if (this.field_234345_bu_ == 0) {
                this.func_234353_eV_();
            }
        }

    }

    private void func_241410_eZ_() {
        if (this.field_241401_bA_ > 0) {
            --this.field_241401_bA_;
        } else {
            if (this.getEntitySenses().canSee(this.getAttackTarget())) {
                this.func_241411_fa_();
            }

            this.field_241401_bA_ = field_241403_bz_.func_233018_a_(this.rand);
        }
    }

    private void func_241411_fa_() {
        double followRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromVector(this.getPositionVec()).grow(followRange, 10.0D, followRange);
        this.world.getLoadedEntitiesWithinAABB(ZombifiedPiglinEntity.class, axisAlignedBB).stream().filter((zombifiedPiglinEntity) -> {
            return zombifiedPiglinEntity != this;
        }).filter((zombifiedPiglinEntity) -> {
            return zombifiedPiglinEntity.getAttackTarget() == null;
        }).filter((zombifiedPiglinEntity) -> {
            return !zombifiedPiglinEntity.isOnSameTeam(this.getAttackTarget());
        }).forEach((zombifiedPiglinEntity) -> {
            zombifiedPiglinEntity.setAttackTarget(this.getAttackTarget());
        });
    }

    private void func_234353_eV_() {
        this.playSound(SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, this.getSoundVolume() * 2.0F, this.getSoundPitch() * 1.8F);
    }

    public void setAttackTarget(@Nullable LivingEntity livingEntity) {
        if (this.getAttackTarget() == null && livingEntity != null) {
            this.field_234345_bu_ = field_234350_d_.func_233018_a_(this.rand);
            this.field_241401_bA_ = field_241403_bz_.func_233018_a_(this.rand);
        }

        if (livingEntity instanceof PlayerEntity) {
            this.func_230246_e_((PlayerEntity)livingEntity);
        }

        super.setAttackTarget(livingEntity);
    }

    public void func_230258_H__() {
        this.func_230260_a__(field_234346_bv_.func_233018_a_(this.rand));
    }

    public static boolean func_234351_b_(EntityType<ZombifiedPiglinEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && world.getBlockState(blockPos.down()).getBlock() != Blocks.NETHER_WART_BLOCK;
    }

    public boolean isNotColliding(IWorldReader worldReader) {
        return worldReader.checkNoEntityCollision(this) && !worldReader.containsAnyLiquid(this.getBoundingBox());
    }

    public void writeAdditional(CompoundNBT compoundNBT) {
        super.writeAdditional(compoundNBT);
        this.writeAngerNBT(compoundNBT);
    }

    public void readAdditional(CompoundNBT compoundNBT) {
        super.readAdditional(compoundNBT);
        this.readAngerNBT((ServerWorld)this.world, compoundNBT);
    }

    public void func_230260_a__(int p_230260_1_) {
        this.field_234347_bw_ = p_230260_1_;
    }

    public int func_230256_F__() {
        return this.field_234347_bw_;
    }

    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        return !this.isInvulnerableTo(damageSource) && super.attackEntityFrom(damageSource, amount);
    }

    protected SoundEvent getAmbientSound() {
        return this.func_233678_J__() ? SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_DEATH;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
    }

    protected ItemStack getSkullDrop() {
        return ItemStack.EMPTY;
    }

    protected void func_230291_eT_() {
        this.getAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.0D);
    }

    public UUID func_230257_G__() {
        return this.aggravatingEntityUUID;
    }

    public boolean func_230292_f_(PlayerEntity playerEntity) {
        return this.func_233680_b_(playerEntity);
    }

    static {
        ATTACK_SPEED_MODIFIER = new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Attacking speed boost", 0.05D, AttributeModifier.Operation.ADDITION);
        field_234350_d_ = TickRangeConverter.convertRange(0, 1);
        field_234346_bv_ = TickRangeConverter.convertRange(20, 39);
        field_241403_bz_ = TickRangeConverter.convertRange(4, 6);
    }
}
