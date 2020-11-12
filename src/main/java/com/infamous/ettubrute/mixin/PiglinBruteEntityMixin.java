package com.infamous.ettubrute.mixin;

import com.infamous.ettubrute.entity.config.EtTuBruteConfig;
import com.infamous.ettubrute.entity.ziglinbrute.ZiglinBruteEntity;
import com.infamous.ettubrute.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PiglinBruteEntity.class)
public abstract class PiglinBruteEntityMixin extends AbstractPiglinEntity {

    public PiglinBruteEntityMixin(EntityType<? extends AbstractPiglinEntity> p_i241915_1_, World p_i241915_2_) {
        super(p_i241915_1_, p_i241915_2_);
    }

    @Override
    public void func_234416_a_(ServerWorld serverWorld){
        if(EtTuBruteConfig.COMMON.ENABLE_BRUTES_BECOME_ZIGLIN_BRUTES.get()){
            ZiglinBruteEntity ziglinBruteEntity = this.func_233656_b_(ModEntityTypes.ZIGLIN_BRUTE.get(), true);
            if (ziglinBruteEntity != null) {
                ziglinBruteEntity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
            }
        }
        else{
            super.func_234416_a_(serverWorld);
        }
    }
}
