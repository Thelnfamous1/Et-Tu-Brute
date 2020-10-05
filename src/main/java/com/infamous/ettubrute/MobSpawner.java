package com.infamous.ettubrute;

import com.infamous.ettubrute.entity.config.EtTuBruteConfig;
import com.infamous.ettubrute.mod.ModEntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

public class MobSpawner {

    public static void setupMobSpawn()
    {

        for(Biome biome : ForgeRegistries.BIOMES)
        {
            if(isPiglinBiome(biome) && EtTuBruteConfig.COMMON.ENABLE_BRUTES_OUTSIDE_BASTIONS.get()){
                // public SpawnListEntry(EntityType<?> entityTypeIn, int weight, int minGroupCountIn, int maxGroupCountIn) {
                //         super(weight);
                //         this.entityType = entityTypeIn;
                //         this.minGroupCount = minGroupCountIn;
                //         this.maxGroupCount = maxGroupCountIn;
                //      }
                biome.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(
                        ModEntityTypes.PIGLIN_BRUTE.get(),
                        EtTuBruteConfig.COMMON.BRUTE_SPAWN_WEIGHT.get(),
                        EtTuBruteConfig.COMMON.BRUTE_MIN_GROUP_SIZE.get(),
                        EtTuBruteConfig.COMMON.BRUTE_MAX_GROUP_SIZE.get()));

            }
            if(isPiglinBiome(biome) && EtTuBruteConfig.COMMON.ENABLE_ZIGLIN_BRUTES.get() && EtTuBruteConfig.COMMON.ENABLE_ZIGLIN_BRUTES_SPAWN_NATURALLY.get()){
                // public SpawnListEntry(EntityType<?> entityTypeIn, int weight, int minGroupCountIn, int maxGroupCountIn) {
                //         super(weight);
                //         this.entityType = entityTypeIn;
                //         this.minGroupCount = minGroupCountIn;
                //         this.maxGroupCount = maxGroupCountIn;
                //      }
                biome.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(
                        ModEntityTypes.ZIGLIN_BRUTE.get(),
                        EtTuBruteConfig.COMMON.ZIGLIN_BRUTE_SPAWN_WEIGHT.get(),
                        EtTuBruteConfig.COMMON.ZIGLIN_BRUTE_MIN_GROUP_SIZE.get(),
                        EtTuBruteConfig.COMMON.ZIGLIN_BRUTE_MAX_GROUP_SIZE.get()));

            }
        }
    }

    public static boolean isPiglinBiome(Biome biome){
        return biome == Biomes.NETHER_WASTES || biome == Biomes.CRIMSON_FOREST;
    }
}
