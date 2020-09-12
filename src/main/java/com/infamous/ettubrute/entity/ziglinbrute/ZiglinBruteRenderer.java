package com.infamous.ettubrute.entity.ziglinbrute;

import com.infamous.ettubrute.entity.piglinbrute.AbstractPiglinEntity;
import com.infamous.ettubrute.entity.piglinbrute.PiglinBruteEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.ettubrute.EtTuBrute.MODID;

@OnlyIn(Dist.CLIENT)
public class ZiglinBruteRenderer extends BipedRenderer<MobEntity, PiglinModel<MobEntity>> {
   private static final ResourceLocation ZIGLIN_BRUTE_TEXTURE = new ResourceLocation(MODID, "textures/entity/piglin/ziglin_brute.png");

   public ZiglinBruteRenderer(EntityRendererManager entityRendererManager, boolean b) {
      super(entityRendererManager, getModel(b), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
      this.addLayer(new BipedArmorLayer(this, new BipedModel(0.5F), new BipedModel(1.02F)));
   }

   private static PiglinModel<MobEntity> getModel(boolean b) {
      PiglinModel<MobEntity> piglinModel = new PiglinModel(0.0F, 64, 64);
      if (b) {
         piglinModel.field_239116_b_.showModel = false;
      }

      return piglinModel;
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(MobEntity entity) {
      ResourceLocation resourcelocation = ZIGLIN_BRUTE_TEXTURE;
      return resourcelocation;
   }
}