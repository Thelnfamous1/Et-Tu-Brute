package com.infamous.ettubrute.entity.client;

import com.infamous.ettubrute.mod.ModEntityTypes;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.ettubrute.EtTuBrute.MODID;

@OnlyIn(Dist.CLIENT)
public class CustomPiglinRenderer extends BipedRenderer<MobEntity, CustomPiglinModel<MobEntity>> {
   private static final ResourceLocation PIGLIN_TEXTURE = new ResourceLocation("textures/entity/piglin/piglin.png");
   private static final ResourceLocation PIGLIN_BRUTE_TEXTURE = new ResourceLocation("textures/entity/piglin/piglin_brute.png");
   private static final ResourceLocation ZIGLIN_BRUTE_TEXTURE = new ResourceLocation(MODID, "textures/entity/piglin/ziglin_brute.png");

   public CustomPiglinRenderer(EntityRendererManager entityRendererManager, boolean b) {
      super(entityRendererManager, getModel(b), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
      this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.02F)));
   }

   private static CustomPiglinModel<MobEntity> getModel(boolean isZombified) {
      CustomPiglinModel<MobEntity> piglinmodel = new CustomPiglinModel<>(0.0F, 64, 64);
      if (isZombified) {
         piglinmodel.rightEar.showModel = false;
      }

      return piglinmodel;
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(MobEntity entity) {
      if(entity.getType() == ModEntityTypes.ZIGLIN_BRUTE.get()){
         return ZIGLIN_BRUTE_TEXTURE;
      }
      else if(entity.getType() == EntityType.field_242287_aj){
         return PIGLIN_BRUTE_TEXTURE;
      }
      return PIGLIN_TEXTURE;
   }

   // Shakes the Piglin Brute while it is zombifying in the Overworld
   protected boolean func_230495_a_(MobEntity mobEntity) {
      return mobEntity instanceof AbstractPiglinEntity && ((AbstractPiglinEntity)mobEntity).func_242336_eL();
   }
}