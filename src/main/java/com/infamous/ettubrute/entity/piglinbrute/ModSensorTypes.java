package com.infamous.ettubrute.entity.piglinbrute;

import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.ettubrute.EtTuBrute.MODID;


public class ModSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, MODID);

    public static final String PIGLIN_BRUTE_SPECIFIC_SENSOR_NAME = "piglin_brute_specific_sensor";
    public static final RegistryObject<SensorType<?>> PIGLIN_BRUTE_SPECIFIC_SENSOR = SENSOR_TYPES.register(PIGLIN_BRUTE_SPECIFIC_SENSOR_NAME,
            () -> new SensorType<>(PiglinBruteSpecificSensor::new));
}
