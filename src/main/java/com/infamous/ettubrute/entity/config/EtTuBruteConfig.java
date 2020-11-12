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

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BRUTES_BECOME_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VICIOUS_ZIGLIN_BRUTES;
        public final ForgeConfigSpec.ConfigValue<Integer> ZIGLIN_BRUTE_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> ZIGLIN_BRUTE_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> ZIGLIN_BRUTE_MAX_GROUP_SIZE;

        public Common(ForgeConfigSpec.Builder builder){

            builder.comment("Piglin Brute Configuration").push("piglin_brute_configuration");

            ENABLE_BRUTES_OUTSIDE_BASTIONS = builder.comment("Enable the spawning of Piglin Brutes in all biomes that Piglins can spawn in. [true / false]").define("enableBrutesOutsideBastions", false);
            BRUTE_SPAWN_WEIGHT = builder.comment("Spawn weight of Piglin Brutes spawning in all biomes that Piglins can spawn in [0-100, default: 1]").defineInRange("bruteSpawnWeight", 1, 0, 100);
            BRUTE_MIN_GROUP_SIZE = builder.comment("Minimum group size of Piglin Brutes spawning in all biomes that Piglins can spawn in [0-100, default: 1]").defineInRange("bruteMinGroupSize", 1, 0, 100);
            BRUTE_MAX_GROUP_SIZE = builder.comment("Maximum group size of Piglin Brutes spawning in all biomes that Piglins can spawn in [0-100, default: 1]").defineInRange("bruteMaxGroupSize", 1, 0, 100);

            builder.pop();

            builder.comment("Ziglin Brute Configuration").push("ziglin_brute_configuration");

            ENABLE_ZIGLIN_BRUTES = builder.comment("Enable the spawning of Ziglin Brutes - the Zombified version of Piglin Brutes. [true / false]").define("enableZiglinBrutes", true);
            ENABLE_VICIOUS_ZIGLIN_BRUTES = builder.comment("If enabled, Ziglin Brutes are hostile to all living creatures on sight - including you. [true / false]").define("enableViciousZiglinBrutes", false);
            ENABLE_BRUTES_BECOME_ZIGLIN_BRUTES = builder.comment("Enable the zombification of Piglin Brutes into Ziglin Brutes instead of vanilla Zombified Piglins. You also need enableZiglinBrutes set to true. [true / false]").define("enableBrutesBecomeZiglinBrutes", true);

            ENABLE_ZIGLIN_BRUTES_SPAWN_NATURALLY = builder.comment("Enable the spawning of Ziglin Brutes in all biomes that Zombified Piglins can spawn in. You also need enableZiglinBrutes set to true. [true / false]").define("enableZiglinBrutesSpawnNaturally", false);
            ZIGLIN_BRUTE_SPAWN_WEIGHT = builder.comment("Spawn weight of Ziglin Brutes spawning in all biomes that Zombified Piglins can spawn in [0-100, default: 1]").defineInRange("ziglinBruteSpawnWeight", 1, 0, 100);
            ZIGLIN_BRUTE_MIN_GROUP_SIZE = builder.comment("Minimum group size of Ziglin Brutes spawning in all biomes that Zombified Piglins can spawn in [0-100, default: 1]").defineInRange("ziglinBruteMinGroupSize", 1, 0, 100);
            ZIGLIN_BRUTE_MAX_GROUP_SIZE = builder.comment("Maximum group size of Ziglin Brutes spawning in all biomes that Zombified Piglins can spawn in [0-100, default: 1]").defineInRange("ziglinBruteMaxGroupSize", 1, 0, 100);

            builder.pop();
        }

    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
