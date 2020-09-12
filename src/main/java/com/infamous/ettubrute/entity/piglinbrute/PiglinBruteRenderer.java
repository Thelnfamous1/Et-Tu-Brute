package com.infamous.ettubrute.entity.piglinbrute;

import com.infamous.ettubrute.entity.config.EtTuBruteConfig;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import static com.infamous.ettubrute.EtTuBrute.MODID;

@OnlyIn(Dist.CLIENT)
public class PiglinBruteRenderer extends BipedRenderer<MobEntity, PiglinModel<MobEntity>> {
   private static final ResourceLocation PIGLIN_BRUTE_TEXTURE = new ResourceLocation(MODID, "textures/entity/piglin/piglin_brute.png");
   private static final ResourceLocation CAMLIN_BRUTE_TEXTURE = new ResourceLocation(MODID, "textures/entity/piglin/camlin_brute.png");

   public PiglinBruteRenderer(EntityRendererManager entityRendererManager, boolean b) {
      super(entityRendererManager, getModel(b), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
      this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.02F)));
   }

   private static PiglinModel<MobEntity> getModel(boolean p_239395_0_) {
      PiglinModel<MobEntity> piglinmodel = new PiglinModel<>(0.0F, 64, 64);
      if (p_239395_0_) {
         piglinmodel.field_239116_b_.showModel = false;
      }

      return piglinmodel;
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(MobEntity entity) {
      if(entity.hasCustomName()){
         String name = entity.getName().getString().toLowerCase().trim();

         boolean isBirthday = false;
         LocalDate localdate = LocalDate.now();
         int dayOfMonth = localdate.get(ChronoField.DAY_OF_MONTH);
         int monthOfYear = localdate.get(ChronoField.MONTH_OF_YEAR);
         if (monthOfYear == 8 && dayOfMonth == 24) {
            isBirthday = true;
         }

         boolean camlin = name.equals("cam")
                 || name.equals("cameron")
                 || (name.equals("birthday boi") && isBirthday)
                 || (name.equals("birthday boy") && isBirthday);
         if(camlin && EtTuBruteConfig.CLIENT.ENABLE_CAMLIN_BRUTE.get()){
            return CAMLIN_BRUTE_TEXTURE;
         }
      }
      return PIGLIN_BRUTE_TEXTURE;
   }

   // Shakes the Piglin Brute while it is zombifying in the Overworld
   protected boolean func_230495_a_(MobEntity mobEntity) {
      return mobEntity instanceof PiglinBruteEntity
              && ((PiglinBruteEntity)mobEntity).canZombify();
   }
}