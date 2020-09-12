package com.infamous.ettubrute.entity.piglinbrute;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.TieredItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class AbstractPiglinEntity extends MonsterEntity {
    protected static final DataParameter<Boolean> IS_IMMUNNE_TO_ZOMBIFICATION;
    protected int TIME_IN_OVERWORLD = 0;

    public AbstractPiglinEntity(World world){
        super(EntityType.field_233591_ai_, world);
    }

    public AbstractPiglinEntity(EntityType<? extends AbstractPiglinEntity> p_i241915_1_, World p_i241915_2_) {
        super(p_i241915_1_, p_i241915_2_);
        this.setCanPickUpLoot(true);
        this.func_242339_eS();
        this.setPathPriority(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
    }

    private void func_242339_eS() {
        if (this.getNavigator() instanceof GroundPathNavigator) {
            ((GroundPathNavigator)this.getNavigator()).setBreakDoors(true);
        }

    }

    protected abstract boolean func_234422_eK_();

    public void setImmuneToZombification(boolean immuneToZombification) {
        this.getDataManager().set(IS_IMMUNNE_TO_ZOMBIFICATION, immuneToZombification);
    }

    protected boolean getImmuneToZombification() {
        return (Boolean)this.getDataManager().get(IS_IMMUNNE_TO_ZOMBIFICATION);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_IMMUNNE_TO_ZOMBIFICATION, false);
    }

    public void writeAdditional(CompoundNBT compoundNBT) {
        super.writeAdditional(compoundNBT);

        if (this.isChild()) {
            compoundNBT.putBoolean("IsBaby", true);
        }
        if (this.getImmuneToZombification()) {
            compoundNBT.putBoolean("IsImmuneToZombification", true);
        }

        compoundNBT.putInt("TimeInOverworld", this.TIME_IN_OVERWORLD);
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return this.isChild() ? 0.93F : 1.74F;
    }


    public double getYOffset() {
        return this.isChild() ? -0.1D : -0.45D;
    }

    public void readAdditional(CompoundNBT compoundNBT) {
        super.readAdditional(compoundNBT);
        this.setChild(compoundNBT.getBoolean("IsBaby"));
        this.setImmuneToZombification(compoundNBT.getBoolean("IsImmuneToZombification"));
        this.TIME_IN_OVERWORLD = compoundNBT.getInt("TimeInOverworld");
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.canZombify()) {
            ++this.TIME_IN_OVERWORLD;
        } else {
            this.TIME_IN_OVERWORLD = 0;
        }

        if (this.TIME_IN_OVERWORLD > 300) {
            this.playZombificationSound();
            this.zombify((ServerWorld)this.world);
        }

    }

    public boolean canZombify() {
        return !this.world.func_230315_m_().func_241509_i_() && !this.getImmuneToZombification() && !this.isAIDisabled();
    }

    protected void zombify(ServerWorld p_234416_1_) {
        ZombifiedPiglinEntity zombifiedPiglinEntity = (ZombifiedPiglinEntity)this.func_233656_b_(EntityType.field_233592_ba_);
        if (zombifiedPiglinEntity != null) {
            zombifiedPiglinEntity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
        }

    }

    public boolean isAdult() {
        return !this.isChild();
    }

    @OnlyIn(Dist.CLIENT)
    public abstract PiglinEntity.Action getAction();

    @Nullable
    public LivingEntity getAttackTarget() {
        return (LivingEntity)this.brain.getMemory(MemoryModuleType.field_234103_o_).orElse((LivingEntity) null);
    }

    protected boolean hasWeapon() {
        return this.getHeldItemMainhand().getItem() instanceof TieredItem;
    }

    public void playAmbientSound() {
        if (this.getBrain().hasActivity(Activity.IDLE)) {
            super.playAmbientSound();
        }

    }

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPacketSender.sendLivingEntity(this);
    }

    protected abstract void playZombificationSound();

    static {
        IS_IMMUNNE_TO_ZOMBIFICATION = EntityDataManager.createKey(AbstractPiglinEntity.class, DataSerializers.BOOLEAN);
    }
}