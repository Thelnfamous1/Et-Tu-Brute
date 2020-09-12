package com.infamous.ettubrute.entity.piglinbrute;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.world.GameRules;

import java.util.List;
import java.util.Optional;

public class ModPiglinTasks {

    private static final RangedInteger field_234445_b_ = TickRangeConverter.func_233037_a_(30, 120);


    public static void setAttackTarget(AbstractPiglinEntity abstractPiglinEntity, LivingEntity livingEntity) {
        if (!abstractPiglinEntity.getBrain().hasActivity(Activity.field_234624_n_)) {
            if (EntityPredicates.field_233583_f_.test(livingEntity)) {
                if (!BrainUtil.func_233861_a_(abstractPiglinEntity, livingEntity, 4.0D)) {
                    if (livingEntity.getType() == EntityType.PLAYER && abstractPiglinEntity.world.getGameRules().getBoolean(GameRules.field_234896_G_)) {
                        setPlayerAttackTarget(abstractPiglinEntity, livingEntity);
                        setAttackingPlayer(abstractPiglinEntity);
                    } else {
                        setLivingAttackTarget(abstractPiglinEntity, livingEntity);
                        huntHoglins(abstractPiglinEntity, livingEntity);
                    }

                }
            }
        }
    }

    private static void setPlayerAttackTarget(AbstractPiglinEntity abstractPiglinEntity, LivingEntity livingEntity) {
        Optional<PlayerEntity> optional = getNearestVisibleTargetablePlayer(abstractPiglinEntity);
        if (optional.isPresent()) {
            setLivingAttackTarget(abstractPiglinEntity, optional.get());
        } else {
            setLivingAttackTarget(abstractPiglinEntity, livingEntity);
        }

    }

    public static Optional<PlayerEntity> getNearestVisibleTargetablePlayer(AbstractPiglinEntity piglinEntity) {
        return piglinEntity.getBrain().hasMemory(MemoryModuleType.field_234102_l_) ? piglinEntity.getBrain().getMemory(MemoryModuleType.field_234102_l_) : Optional.empty();
    }

    protected static void setAttackingPlayer(AbstractPiglinEntity abstractPiglinEntity) {
        getNearestAdultPiglins(abstractPiglinEntity).forEach((piglinEntity) -> {
            PiglinTasks.func_241432_i_(piglinEntity).ifPresent((playerEntity) -> {
                func_234497_c_(piglinEntity, playerEntity);
            });
        });
    }

    private static List<PiglinEntity> getNearestAdultPiglins(AbstractPiglinEntity abstractPiglinEntity) {
        return abstractPiglinEntity.getBrain().getMemory(MemoryModuleType.field_234089_W_).orElse(ImmutableList.of());
    }

    protected static void setLivingAttackTarget(AbstractPiglinEntity abstractPiglinEntity, LivingEntity livingEntity) {
        if (isTargetableEntity(livingEntity)) {
            abstractPiglinEntity.getBrain().removeMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            abstractPiglinEntity.getBrain().func_233696_a_(MemoryModuleType.field_234078_L_, livingEntity.getUniqueID(), 600L);
            // hoglin
            if (livingEntity.getType() == EntityType.field_233588_G_) {
                setHunting(abstractPiglinEntity);
            }

            if (livingEntity.getType() == EntityType.PLAYER && abstractPiglinEntity.world.getGameRules().getBoolean(GameRules.field_234896_G_)) {
                abstractPiglinEntity.getBrain().func_233696_a_(MemoryModuleType.field_234079_M_, true, 600L);
            }

        }
    }

    private static boolean isTargetableEntity(LivingEntity livingEntity) {
        return EntityPredicates.field_233583_f_.test(livingEntity);
    }

    protected static void setHunting(AbstractPiglinEntity abstractPiglinEntity) {
        abstractPiglinEntity.getBrain().func_233696_a_(MemoryModuleType.field_234082_P_, true, (long)field_234445_b_.func_233018_a_(abstractPiglinEntity.world.rand));
    }


    protected static void huntHoglins(AbstractPiglinEntity abstractPiglinEntity, LivingEntity livingEntity) {
        getNearestAdultPiglins(abstractPiglinEntity).forEach((piglinEntity) -> {
            if (livingEntity.getType() != EntityType.field_233588_G_
                    //|| piglinEntity.func_234422_eK_()
                    &&
                    ((HoglinEntity)livingEntity).func_234365_eM_()) {
                func_234513_f_(piglinEntity, livingEntity);
            }
        });
    }

    private static void func_234513_f_(PiglinEntity piglinEntity, LivingEntity livingEntity) {
        Optional<LivingEntity> optional = func_234532_s_(piglinEntity);
        LivingEntity livingentity = BrainUtil.func_233867_a_(piglinEntity, optional, livingEntity);
        if (!optional.isPresent() || optional.get() != livingentity) {
            func_234497_c_(piglinEntity, livingentity);
        }
    }

    private static Optional<LivingEntity> func_234532_s_(PiglinEntity piglinEntity) {
        return BrainUtil.func_233864_a_(piglinEntity, MemoryModuleType.field_234078_L_);
    }

    protected static void func_234497_c_(PiglinEntity piglinEntity, LivingEntity livingEntity) {
        if (func_234506_e_(livingEntity)) {
            piglinEntity.getBrain().removeMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            piglinEntity.getBrain().func_233696_a_(MemoryModuleType.field_234078_L_, livingEntity.getUniqueID(), 600L);
            if (livingEntity.getType() == EntityType.field_233588_G_) {
                func_234518_h_(piglinEntity);
            }

            if (livingEntity.getType() == EntityType.PLAYER && piglinEntity.world.getGameRules().getBoolean(GameRules.field_234896_G_)) {
                piglinEntity.getBrain().func_233696_a_(MemoryModuleType.field_234079_M_, true, 600L);
            }

        }
    }

    private static boolean func_234506_e_(LivingEntity livingEntity) {
        return EntityPredicates.field_233583_f_.test(livingEntity);
    }

    protected static void func_234518_h_(PiglinEntity piglinEntity) {
        piglinEntity.getBrain().func_233696_a_(MemoryModuleType.field_234082_P_, true, (long)field_234445_b_.func_233018_a_(piglinEntity.world.rand));
    }
}
