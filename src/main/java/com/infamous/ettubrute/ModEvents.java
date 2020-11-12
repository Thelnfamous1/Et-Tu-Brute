package com.infamous.ettubrute;

import com.infamous.ettubrute.entity.config.EtTuBruteConfig;
import com.infamous.ettubrute.mod.ModEntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.ListIterator;

@Mod.EventBusSubscriber(modid = EtTuBrute.MODID)
public class ModEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(BiomeLoadingEvent event){
        MobSpawnInfoBuilder mobSpawnInfoBuilder = event.getSpawns();
        List<MobSpawnInfo.Spawners> monsterSpawnersList = mobSpawnInfoBuilder.getSpawner(EntityClassification.MONSTER);
        ListIterator<MobSpawnInfo.Spawners> litr = monsterSpawnersList.listIterator();
        while(litr.hasNext()){
            MobSpawnInfo.Spawners spawners = litr.next();
            if(spawners.type == EntityType.PIGLIN && EtTuBruteConfig.COMMON.ENABLE_BRUTES_OUTSIDE_BASTIONS.get()){
                litr.add(new MobSpawnInfo.Spawners(
                        EntityType.field_242287_aj,
                        EtTuBruteConfig.COMMON.BRUTE_SPAWN_WEIGHT.get(),
                        EtTuBruteConfig.COMMON.BRUTE_MIN_GROUP_SIZE.get(),
                        EtTuBruteConfig.COMMON.BRUTE_MAX_GROUP_SIZE.get()));
            }
            if(spawners.type == EntityType.ZOMBIFIED_PIGLIN
                    && EtTuBruteConfig.COMMON.ENABLE_ZIGLIN_BRUTES.get()
                    && EtTuBruteConfig.COMMON.ENABLE_ZIGLIN_BRUTES_SPAWN_NATURALLY.get()){
                litr.add(new MobSpawnInfo.Spawners(
                        ModEntityTypes.ZIGLIN_BRUTE.get(),
                        EtTuBruteConfig.COMMON.ZIGLIN_BRUTE_SPAWN_WEIGHT.get(),
                        EtTuBruteConfig.COMMON.ZIGLIN_BRUTE_MIN_GROUP_SIZE.get(),
                        EtTuBruteConfig.COMMON.ZIGLIN_BRUTE_MAX_GROUP_SIZE.get()));
            }
        }
    }
}
