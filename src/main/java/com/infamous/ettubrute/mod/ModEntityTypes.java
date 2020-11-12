package com.infamous.ettubrute.mod;

import com.infamous.ettubrute.entity.ziglinbrute.ZiglinBruteEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.ettubrute.EtTuBrute.MODID;


public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static final String ZIGLIN_BRUTE_NAME = "ziglin_brute";
    public static final RegistryObject<EntityType<ZiglinBruteEntity>> ZIGLIN_BRUTE = ENTITY_TYPES.register(ZIGLIN_BRUTE_NAME, () ->
            EntityType.Builder.<ZiglinBruteEntity>create(ZiglinBruteEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.6F, 1.95F)
                    .trackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ZiglinBruteEntity(world))
                    .build(new ResourceLocation(MODID, ZIGLIN_BRUTE_NAME).toString())
    );
}
