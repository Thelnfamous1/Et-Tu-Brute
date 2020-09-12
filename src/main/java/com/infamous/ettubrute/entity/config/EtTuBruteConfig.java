package com.infamous.ettubrute.entity.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class EtTuBruteConfig {

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BRUTES_OUTSIDE_BASTIONS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ZIGLIN_BRUTES_SPAWN_NATURALLY;
        public final ForgeConfigSpec.ConfigValue<Integer> BRUTE_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> BRUTE_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> BRUTE_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BABY_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_RANDOM_BABY_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BASTION_BABY_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BABY_BRUTES_ARMED;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BRUTES_BECOME_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VICIOUS_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Integer> ZIGLIN_BRUTE_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> ZIGLIN_BRUTE_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> ZIGLIN_BRUTE_MAX_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BABY_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_RANDOM_BABY_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BABY_ZIGLIN_BRUTES_ARMED;

        public Common(ForgeConfigSpec.Builder builder){

            builder.comment("Piglin Brute Configuration").push("piglin_brute_configuration");

            ENABLE_BRUTES_OUTSIDE_BASTIONS = builder.comment("Enable the spawning of Piglin Brutes in Nether Wastes and Crimson Forests. [true / false]").define("enableBrutesOutsideBastions", false);
            BRUTE_SPAWN_WEIGHT = builder.comment("Spawn weight of Piglin Brutes spawning in the Nether Wastes and Crimson Forests [0-100, default: 1]").defineInRange("bruteSpawnWeight", 1, 0, 100);
            BRUTE_MIN_GROUP_SIZE = builder.comment("Minimum group size of Piglin Brutes spawning in the Nether Wastes and Crimson Forests [0-100, default: 1]").defineInRange("bruteMinGroupSize", 1, 0, 100);
            BRUTE_MAX_GROUP_SIZE = builder.comment("Maximum group size of Piglin Brutes spawning in the Nether Wastes and Crimson Forests [0-100, default: 1]").defineInRange("bruteMaxGroupSize", 1, 0, 100);

            builder.pop();

            builder.comment("Baby Piglin Brute Configuration").push("baby_piglin_brute_configuration");

            ENABLE_BABY_BRUTES = builder.comment("Enable the spawning of baby Piglin Brutes. [true / false]").define("enableBabyBrutes", false);
            ENABLE_RANDOM_BABY_BRUTES = builder.comment("Enable the random spawning of baby Piglin Brutes, when not spawned from structure generation. You also need enableBabyBrutes set to true. [true / false]").define("enableRandomBabyBrutes", false);
            ENABLE_BASTION_BABY_BRUTES = builder.comment("Enable the random spawning of baby Piglin Brutes during structure generation. You need to enable this before you create a world, and also need enableBabyBrutes set to true. [true / false]").define("enableBastionBabyBrutes", false);
            ENABLE_BABY_BRUTES_ARMED = builder.comment("Allow baby Piglin Brutes to spawn with Golden Axes. [true / false]").define("enableBabyBrutesArmed", false);

            builder.pop();

            builder.comment("Ziglin Brute Configuration").push("ziglin_brute_configuration");

            ENABLE_ZIGLIN_BRUTES = builder.comment("Enable the spawning of Ziglin Brutes - the Zombified version of Piglin Brutes. [true / false]").define("enableZiglinBrutes", true);
            ENABLE_VICIOUS_ZIGLIN_BRUTES = builder.comment("If enabled, Ziglin Brutes are hostile to all living creatures on sight - including you. [true / false]").define("enableViciousZiglinBrutes", false);
            ENABLE_BRUTES_BECOME_ZIGLIN_BRUTES = builder.comment("Enable the zombification of Piglin Brutes into Ziglin Brutes instead of vanilla Zombified Piglins. You also need enableZiglinBrutes set to true. [true / false]").define("enableBrutesBecomeZiglinBrutes", true);

            ENABLE_ZIGLIN_BRUTES_SPAWN_NATURALLY = builder.comment("Enable the spawning of Ziglin Brutes in Nether Wastes and Crimson Forests. You also need enableZiglinBrutes set to true. [true / false]").define("enableZiglinBrutesSpawnNaturally", false);
            ZIGLIN_BRUTE_SPAWN_WEIGHT = builder.comment("Spawn weight of Ziglin Brutes spawning in the Nether Wastes and Crimson Forests [0-100, default: 1]").defineInRange("ziglinBruteSpawnWeight", 1, 0, 100);
            ZIGLIN_BRUTE_MIN_GROUP_SIZE = builder.comment("Minimum group size of Ziglin Brutes spawning in the Nether Wastes and Crimson Forests [0-100, default: 1]").defineInRange("ziglinBruteMinGroupSize", 1, 0, 100);
            ZIGLIN_BRUTE_MAX_GROUP_SIZE = builder.comment("Maximum group size of Ziglin Brutes spawning in the Nether Wastes and Crimson Forests [0-100, default: 1]").defineInRange("ziglinBruteMaxGroupSize", 1, 0, 100);

            builder.pop();

            builder.comment("Baby Ziglin Brute Configuration").push("baby_ziglin_brute_configuration");

            ENABLE_BABY_ZIGLIN_BRUTES = builder.comment("Enable the spawning of baby Ziglin Brutes. You also need enableZiglinBrutes set to true. [true / false]").define("enableBabyZiglinBrutes", false);
            ENABLE_RANDOM_BABY_ZIGLIN_BRUTES = builder.comment("Enable the random spawning of baby Ziglin Brutes. You also need enableBabyZiglinBrutes set to true. [true / false]").define("enableRandomBabyZiglinBrutes", false);
            ENABLE_BABY_ZIGLIN_BRUTES_ARMED = builder.comment("Allow baby Ziglin Brutes to spawn with Golden Axes. [true / false]").define("enableBabyZiglinBrutesArmed", false);

            builder.pop();
        }

    }

    public static class Client{
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_CAMLIN_BRUTE;
        public Client(ForgeConfigSpec.Builder builder){

            builder.comment("Camlin Brute Configuration").push("camlin_brute_configuration");

            ENABLE_CAMLIN_BRUTE = builder.comment("Enable to make naming a Piglin Brute 'Cam', 'Cameron', and on August 24th, 'Birthday Boy' or 'Birthday Boi', turn it into a Camlin Brute. [true / false]").define("enableCamlinBrute", true);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }
}
