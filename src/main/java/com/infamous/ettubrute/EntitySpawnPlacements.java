package com.infamous.ettubrute;

import com.infamous.ettubrute.entity.ziglinbrute.ZiglinBruteEntity;
import com.infamous.ettubrute.mod.ModEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

public class EntitySpawnPlacements {

    public static void initSpawnPlacements(){
        EntitySpawnPlacementRegistry.register(
                ModEntityTypes.ZIGLIN_BRUTE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                ZiglinBruteEntity::canZiglinBruteSpawn);
        EntitySpawnPlacementRegistry.register(
                EntityType.field_242287_aj,
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canPiglinBruteSpawn);

    }

    public static boolean canPiglinBruteSpawn(EntityType<PiglinBruteEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return !world.getBlockState(blockPos.down()).isIn(Blocks.NETHER_WART_BLOCK);
    }

}
