package com.infamous.ettubrute.mod;

import com.infamous.ettubrute.item.ModSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.ettubrute.EtTuBrute.MODID;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<ModSpawnEggItem> PIGLIN_BRUTE_SPAWN_EGG = ITEMS.register("piglin_brute_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.PIGLIN_BRUTE,
                    5843472,
                    16380836,
                    new Item.Properties().group(ItemGroup.MISC)));

    public static final RegistryObject<ModSpawnEggItem> ZIGLIN_BRUTE_SPAWN_EGG = ITEMS.register("ziglin_brute_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ZIGLIN_BRUTE,
                    5843472,
                    5009705,
                    new Item.Properties().group(ItemGroup.MISC)));
}
