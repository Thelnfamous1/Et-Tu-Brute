package com.infamous.ettubrute;

import com.infamous.ettubrute.entity.piglinbrute.PiglinBruteEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EtTuBrute.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onWitherSkeletonJoinWorld(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof WitherSkeletonEntity){
            WitherSkeletonEntity witherSkeletonEntity = (WitherSkeletonEntity)event.getEntity();
            witherSkeletonEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(witherSkeletonEntity, PiglinBruteEntity.class, true));
        }
    }

    /*
    @SubscribeEvent
    public static void onBlockPlaced(PlayerInteractEvent.RightClickBlock event){
        ItemStack useStack = event.getItemStack();
        BlockPos blockPos = event.getPos();
        World world = event.getWorld();
        if(useStack.getItem() == Blocks.field_235341_dI_.asItem()){
            event.setUseItem(Event.Result.DENY);
            world.setBlockState(blockPos, Blocks.field_235334_I_.getDefaultState())
        }
        else if(useStack.getItem() == Blocks.LANTERN.asItem()){
            event.setUseItem(Event.Result.DENY);

        }
        else if(useStack.getItem() == Blocks.field_235366_md_.asItem()){
            event.setUseItem(Event.Result.DENY);

        }
    }

     */
}
