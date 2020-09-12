package com.infamous.ettubrute.entity;


import com.infamous.ettubrute.EtTuBrute;
import com.infamous.ettubrute.entity.piglinbrute.PiglinBruteRenderer;
import com.infamous.ettubrute.entity.ziglinbrute.ZiglinBruteRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EtTuBrute.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void onRenderingRegistry(final FMLClientSetupEvent event){

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGLIN_BRUTE.get(), (EntityRendererManager entityRendererManager) -> new PiglinBruteRenderer(entityRendererManager, false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ZIGLIN_BRUTE.get(), (EntityRendererManager entityRendererManager) -> new ZiglinBruteRenderer(entityRendererManager, false));

    }
}
