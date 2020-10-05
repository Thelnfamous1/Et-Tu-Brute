package com.infamous.ettubrute.entity.piglinbrute;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.infamous.ettubrute.mod.ModEntityTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

public class PiglinBruteBrain {

    protected static Brain<?> initializeBrain(PiglinBruteEntity piglinBruteEntity, Brain<PiglinBruteEntity> entityBrain) {
        setCoreTasks(entityBrain);
        setIdleTasks(entityBrain);
        setAttackTasks(piglinBruteEntity, entityBrain);
        entityBrain.setDefaultActivities(ImmutableSet.of(Activity.CORE));
        entityBrain.setFallbackActivity(Activity.IDLE);
        entityBrain.func_233714_e_();
        return entityBrain;
    }

    protected static void setHome(PiglinBruteEntity piglinBruteEntity) {
        GlobalPos globalPos = GlobalPos.getPosition(piglinBruteEntity.world.getDimensionKey(), piglinBruteEntity.getPosition());
        piglinBruteEntity.getBrain().setMemory(MemoryModuleType.HOME, globalPos);
    }

    private static void setCoreTasks(Brain<PiglinBruteEntity> entityBrain) {
        entityBrain.func_233698_a_(Activity.CORE, 0, ImmutableList.of(
                new LookTask(45, 90),
                new WalkToTargetTask(200),
                new InteractWithDoorTask(),
                new GetAngryTask<net.minecraft.entity.MobEntity>()));
    }

    private static void setIdleTasks(Brain<PiglinBruteEntity> entityBrain) {
        entityBrain.func_233698_a_(Activity.IDLE, 10, ImmutableList.<Task<? super PiglinBruteEntity>>of(
                new ForgetAttackTargetTask<>(PiglinBruteBrain::getAttackTarget),
                getLookTasks(),
                getWalkTasks(),
               new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)
        ));
    }

    /*
    private static void func_234485_b_(Brain<PiglinEntity> p_234485_0_) {
        p_234485_0_.func_233698_a_(Activity.IDLE, 10, ImmutableList.of(
                new LookAtEntityTask(PiglinTasks::func_234482_b_, 14.0F),
                new ForgetAttackTargetTask<>(PiglinEntity::func_234421_eJ_, PiglinTasks::func_234526_m_),
                new SupplementedTask<>(PiglinEntity::func_234422_eK_, new StartHuntTask<>()), func_234493_c_(), func_234505_e_(), func_234458_a_(), func_234481_b_(),
                new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)));
    }

     */


    private static void setAttackTasks(PiglinBruteEntity piglinBruteEntity, Brain<PiglinBruteEntity> entityBrain) {
        entityBrain.func_233699_a_(Activity.FIGHT, 10, ImmutableList.<Task<? super PiglinBruteEntity>>of(
                new FindNewAttackTargetTask((o) -> !getAttackableEntities(piglinBruteEntity, (LivingEntity) o)),
                new MoveToTargetTask(1.0F),
                new AttackTargetTask(20)
        ), MemoryModuleType.ATTACK_TARGET);
    }

    /*
    private static void func_234488_b_(PiglinEntity p_234488_0_, Brain<PiglinEntity> p_234488_1_) {
        p_234488_1_.func_233699_a_(Activity.field_234621_k_, 10, ImmutableList.<net.minecraft.entity.ai.brain.task.Task<? super PiglinEntity>>of(
                new FindNewAttackTargetTask<>((p_234523_1_) -> {
            return !func_234504_d_(p_234488_0_, p_234523_1_);
        }), new SupplementedTask<>(PiglinTasks::func_234494_c_,
                        new AttackStrafingTask<>(5, 0.75F)),
                new MoveToTargetTask(1.0F),
                new AttackTargetTask(20),
                new ShootTargetTask(),
                new FinishedHuntTask(),
                new PredicateTask<>(PiglinTasks::func_234525_l_, MemoryModuleType.field_234103_o_)
        ), MemoryModuleType.field_234103_o_);
    }

     */

    //@SuppressWarnings("unchecked")
    private static FirstShuffledTask getLookTasks() {
        return new FirstShuffledTask<>(ImmutableList.of(
                Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 1),
                Pair.of(new LookAtEntityTask(EntityType.PIGLIN, 8.0F), 1),
                Pair.of(new LookAtEntityTask(ModEntityTypes.PIGLIN_BRUTE.get(), 8.0F), 1),
                Pair.of(new LookAtEntityTask(8.0F), 1),
                Pair.of(new DummyTask(30, 60), 1)));
    }

    //@SuppressWarnings("unchecked")
    private static FirstShuffledTask getWalkTasks() {
        return new FirstShuffledTask<>(ImmutableList.of(
                Pair.of(new WalkRandomlyTask(0.6F), 2),
                // interact with other piglins
                Pair.of(InteractWithEntityTask.func_220445_a(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                // interact with other brutes
                Pair.of(InteractWithEntityTask.func_220445_a(ModEntityTypes.PIGLIN_BRUTE.get(), 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                // walk towards home
                //Pair.of(new WalkTowardsPosTask(MemoryModuleType.HOME, 0.6F, 2, 100), 2),
                Pair.of(new WalkTowardsPosTask(MemoryModuleType.HOME, 2, 100), 2),
                // "work" at home
                //Pair.of(new WorkTask(MemoryModuleType.HOME, 0.6F, 5), 2),
                Pair.of(new WorkTask(MemoryModuleType.HOME, 5), 2),
                // dummy task
                Pair.of(new DummyTask(30, 60), 1)));
    }

    protected static void setAggroed(PiglinBruteEntity piglinBruteEntity) {
        Brain<PiglinBruteEntity> entityBrain = piglinBruteEntity.getBrain();
        Activity activity = (Activity)entityBrain.func_233716_f_().orElse((Activity) null);
        entityBrain.func_233706_a_(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        Activity activity1 = (Activity)entityBrain.func_233716_f_().orElse((Activity) null);
        if (activity != activity1) {
            playFightSound(piglinBruteEntity);
        }

        piglinBruteEntity.setAggroed(entityBrain.hasMemory(MemoryModuleType.ATTACK_TARGET));
    }

    private static boolean getAttackableEntities(PiglinBruteEntity piglinEntity, LivingEntity livingEntity) {
        return getAttackTarget(piglinEntity).filter((livingEntityIterator) -> {
            return livingEntityIterator == livingEntity;
        }).isPresent();
    }

    private static Optional<? extends LivingEntity> getAttackTarget(PiglinBruteEntity PiglinBruteEntity) {
        Optional<LivingEntity> optionalLivingEntity = BrainUtil.func_233864_a_(PiglinBruteEntity, MemoryModuleType.ANGRY_AT);
        if (optionalLivingEntity.isPresent() && canAttackEntity((LivingEntity)optionalLivingEntity.get())) {
            return optionalLivingEntity;
        } else {
            Optional<? extends LivingEntity> optionalLivingEntity1 = getNearestVisibleTargetablePlayer(PiglinBruteEntity, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
            return optionalLivingEntity1.isPresent() ? optionalLivingEntity1 : PiglinBruteEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        }
    }

    private static boolean canAttackEntity(LivingEntity livingEntity) {
        return EntityPredicates.CAN_HOSTILE_AI_TARGET.test(livingEntity);
    }

    private static Optional<? extends LivingEntity> getNearestVisibleTargetablePlayer(PiglinBruteEntity piglinEntity, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
        return piglinEntity.getBrain().getMemory(memoryModuleType).filter((livingEntity) -> {
            return livingEntity.isEntityInRange(piglinEntity, 12.0D);
        });
    }

    protected static void makeAngryAt(PiglinBruteEntity piglinBruteEntity, LivingEntity livingEntity) {
        if (!(livingEntity instanceof PiglinBruteEntity || livingEntity instanceof PiglinEntity)) {
            PiglinBruteTasks.setAttackTarget(piglinBruteEntity, livingEntity);
            //PiglinTasks.func_234509_e_(piglinBruteEntity, livingEntity);
        }
    }



    protected static void activateFight(PiglinBruteEntity piglinBruteEntity) {
        if ((double)piglinBruteEntity.world.rand.nextFloat() < 0.0125D) {
            playFightSound(piglinBruteEntity);
        }

    }

    private static void playFightSound(PiglinBruteEntity piglinBruteEntity) {
        piglinBruteEntity.getBrain().func_233716_f_().ifPresent((activity) -> {
            if (activity == Activity.FIGHT) {
                piglinBruteEntity.playFightSound();
            }

        });
    }
}