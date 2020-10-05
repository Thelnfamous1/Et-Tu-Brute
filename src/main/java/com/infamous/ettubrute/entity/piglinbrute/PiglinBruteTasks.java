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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Optional;

public class PiglinBruteTasks {

    private static final RangedInteger huntingRangedInteger = TickRangeConverter.convertRange(30, 120);


    public static void setAttackTarget(PiglinBruteEntity bruteEntity, LivingEntity targetEntity) {
        if (!bruteEntity.getBrain().hasActivity(Activity.AVOID)) {
            if (canTargetThisEntity(targetEntity)) {
                if (!BrainUtil.func_233861_a_(bruteEntity, targetEntity, 4.0D)) {
                    if (targetEntity.getType() == EntityType.PLAYER && bruteEntity.world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                        setAttackingNearestPlayerOrEntity(bruteEntity, targetEntity);
                        makeNearbyPiglinsAngryAtPlayer(bruteEntity);
                    } else {
                        makePiglinAngryAtEntity(bruteEntity, targetEntity);
                        makeNearbyPiglinsAngryAtEntityIfNotHuntable(bruteEntity, targetEntity);
                    }

                }
            }
        }
    }

    private static void setAttackingNearestPlayerOrEntity(PiglinBruteEntity bruteEntity, LivingEntity livingEntity) {
        Optional<PlayerEntity> optional = getNearestVisibleTargetablePlayer(bruteEntity);
        if (optional.isPresent()) {
            makePiglinAngryAtAndPossiblyHunt(bruteEntity, optional.get());
        } else {
            makePiglinAngryAtAndPossiblyHunt(bruteEntity, livingEntity);
        }

    }

    public static Optional<PlayerEntity> getNearestVisibleTargetablePlayer(PiglinBruteEntity bruteEntity) {
        return bruteEntity.getBrain().hasMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) ?
                bruteEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) :
                Optional.empty();
    }

    protected static void makeNearbyPiglinsAngryAtPlayer(PiglinBruteEntity PiglinBruteEntity) {
        getNearestAdultPiglins(PiglinBruteEntity).forEach((piglinEntity) -> {
            PiglinTasks.func_241432_i_(piglinEntity).ifPresent((playerEntity) -> {
                makePiglinAngryAtEntity(piglinEntity, playerEntity);
            });
        });
    }

    private static List<PiglinEntity> getNearestAdultPiglins(PiglinBruteEntity bruteEntity) {
        return bruteEntity.getBrain().getMemory(MemoryModuleType.NEAREST_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    protected static void makePiglinAngryAtAndPossiblyHunt(PiglinBruteEntity bruteEntity, LivingEntity livingEntity) {
        if (canTargetThisEntity(livingEntity)) {
            bruteEntity.getBrain().removeMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            bruteEntity.getBrain().func_233696_a_(MemoryModuleType.ANGRY_AT, livingEntity.getUniqueID(), 600L);
            if (livingEntity.getType() == EntityType.HOGLIN) {
                setHunting(bruteEntity);
            }

            if (livingEntity.getType() == EntityType.PLAYER && bruteEntity.world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                bruteEntity.getBrain().func_233696_a_(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
            }

        }
    }

    protected static void setHunting(PiglinBruteEntity bruteEntity) {
        bruteEntity.getBrain().func_233696_a_(MemoryModuleType.HUNTED_RECENTLY, true, (long) huntingRangedInteger.func_233018_a_(bruteEntity.world.rand));
    }

    protected static void makeNearbyPiglinsAngryAtEntityIfNotHuntable(PiglinBruteEntity bruteEntity, LivingEntity livingEntity) {
        getNearestAdultPiglins(bruteEntity).forEach((piglinEntity) -> {

            Boolean cannotHuntFieldValue = ObfuscationReflectionHelper.getPrivateValue(PiglinEntity.class, piglinEntity, "field_234407_bB_");
            boolean cannotHunt = false;
            if (cannotHuntFieldValue != null) {
                cannotHunt = cannotHuntFieldValue;
            }

            boolean isNotAHoglin = livingEntity.getType() != EntityType.HOGLIN;
            boolean hoglinCannotBeHunted = livingEntity instanceof HoglinEntity && ((HoglinEntity) livingEntity).func_234365_eM_();
            boolean cannotHuntThisEntity = isNotAHoglin || !cannotHunt && hoglinCannotBeHunted;

            if (cannotHuntThisEntity) {
                makeAngryAtEntityIfNotAlreadyAngry(piglinEntity, livingEntity);
            }
        });
    }

    private static void makeAngryAtEntityIfNotAlreadyAngry(PiglinEntity piglinEntity, LivingEntity livingEntity) {
        Optional<LivingEntity> optional = getEntityAngryAt(piglinEntity);
        LivingEntity livingentity = BrainUtil.func_233867_a_(piglinEntity, optional, livingEntity);
        if (!optional.isPresent() || optional.get() != livingentity) {
            makePiglinAngryAtEntity(piglinEntity, livingentity);
        }
    }

    private static Optional<LivingEntity> getEntityAngryAt(PiglinEntity piglinEntity) {
        return BrainUtil.func_233864_a_(piglinEntity, MemoryModuleType.ANGRY_AT);
    }

    protected static void makePiglinAngryAtEntity(PiglinBruteEntity piglinEntity, LivingEntity livingEntity) {
        if (canTargetThisEntity(livingEntity)) {
            piglinEntity.getBrain().removeMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            piglinEntity.getBrain().func_233696_a_(MemoryModuleType.ANGRY_AT, livingEntity.getUniqueID(), 600L);
            if (livingEntity.getType() == EntityType.HOGLIN) {
                checkHasHuntedRecently(piglinEntity);
            }

            if (livingEntity.getType() == EntityType.PLAYER && piglinEntity.world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                piglinEntity.getBrain().func_233696_a_(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
            }

        }
    }

    protected static void makePiglinAngryAtEntity(PiglinEntity piglinEntity, LivingEntity livingEntity) {
        if (canTargetThisEntity(livingEntity)) {
            piglinEntity.getBrain().removeMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            piglinEntity.getBrain().func_233696_a_(MemoryModuleType.ANGRY_AT, livingEntity.getUniqueID(), 600L);
            if (livingEntity.getType() == EntityType.HOGLIN) {
                checkHasHuntedRecently(piglinEntity);
            }

            if (livingEntity.getType() == EntityType.PLAYER && piglinEntity.world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                piglinEntity.getBrain().func_233696_a_(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
            }

        }
    }

    private static boolean canTargetThisEntity(LivingEntity livingEntity) {
        return EntityPredicates.CAN_HOSTILE_AI_TARGET.test(livingEntity);
    }

    protected static void checkHasHuntedRecently(PiglinEntity piglinEntity) {
        piglinEntity.getBrain().func_233696_a_(MemoryModuleType.HUNTED_RECENTLY, true, (long) huntingRangedInteger.func_233018_a_(piglinEntity.world.rand));
    }

    protected static void checkHasHuntedRecently(PiglinBruteEntity piglinEntity) {
        piglinEntity.getBrain().func_233696_a_(MemoryModuleType.HUNTED_RECENTLY, true, (long) huntingRangedInteger.func_233018_a_(piglinEntity.world.rand));
    }
}
