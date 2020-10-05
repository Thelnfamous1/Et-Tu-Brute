package com.infamous.ettubrute;

import com.infamous.ettubrute.entity.piglinbrute.PiglinBruteEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EtTuBrute.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onWitherSkeletonJoinWorld(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof WitherSkeletonEntity){
            WitherSkeletonEntity witherSkeletonEntity = (WitherSkeletonEntity)event.getEntity();
            witherSkeletonEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(witherSkeletonEntity, PiglinBruteEntity.class, true));
        }
    }
}
