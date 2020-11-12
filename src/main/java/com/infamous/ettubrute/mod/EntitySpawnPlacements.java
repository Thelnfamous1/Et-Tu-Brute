package com.infamous.ettubrute.mod;

import com.infamous.ettubrute.entity.piglinbrute.PiglinBruteEntity;
import com.infamous.ettubrute.entity.ziglinbrute.ZiglinBruteEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.world.gen.Heightmap;

public class EntitySpawnPlacements {

    public static void initSpawnPlacements(){
        EntitySpawnPlacementRegistry.register(
                ModEntityTypes.ZIGLIN_BRUTE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                ZiglinBruteEntity::canZiglinBruteSpawn);
        EntitySpawnPlacementRegistry.register(
                ModEntityTypes.PIGLIN_BRUTE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                PiglinBruteEntity::canPiglinBruteSpawn);

    }

}