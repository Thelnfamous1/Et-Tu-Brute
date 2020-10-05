package com.infamous.ettubrute.entity.piglinbrute;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PiglinBruteSpecificSensor extends Sensor<LivingEntity> {
    public PiglinBruteSpecificSensor() {
    }

    public Set<MemoryModuleType<?>> getUsedMemories() {
        return ImmutableSet.of(MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_ADULT_PIGLINS);
    }

    protected void update(ServerWorld serverWorld, LivingEntity livingEntity) {
        Brain<?> entityBrain = livingEntity.getBrain();
        Optional<MobEntity> optionalMobEntity = Optional.empty();
        List<PiglinEntity> piglinEntityList = Lists.newArrayList();
        List livingEntityList = (List)entityBrain.getMemory(MemoryModuleType.VISIBLE_MOBS).orElse(ImmutableList.of());
        Iterator var7 = livingEntityList.iterator();

        while(var7.hasNext()) {
            LivingEntity possibleEnemy = (LivingEntity)var7.next();
            if (possibleEnemy instanceof WitherSkeletonEntity || possibleEnemy instanceof WitherEntity) {
                optionalMobEntity = Optional.of((MobEntity)possibleEnemy);
                break;
            }
        }

        List livingEntityList1 = (List)entityBrain.getMemory(MemoryModuleType.MOBS).orElse(ImmutableList.of());
        Iterator var11 = livingEntityList1.iterator();

        while(var11.hasNext()) {
            LivingEntity livingEntity1 = (LivingEntity)var11.next();
            if (livingEntity1 instanceof PiglinEntity && !((PiglinEntity)livingEntity1).isChild()) {
                piglinEntityList.add((PiglinEntity)livingEntity1);
            }

            //else if (livingEntity1 instanceof AbstractPiglinEntity && !((AbstractPiglinEntity)livingEntity1).isChild()) {
            //    piglinEntityList.add((AbstractPiglinEntity)livingEntity1);
            //}
        }

        entityBrain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optionalMobEntity);
        entityBrain.setMemory(MemoryModuleType.NEAREST_ADULT_PIGLINS, piglinEntityList);
    }
}
