package com.infamous.ettubrute.entity.client;

import com.infamous.ettubrute.entity.piglinbrute.PiglinBruteEntity;
import com.infamous.ettubrute.mod.ModEntityTypes;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomPiglinModel<T extends MobEntity> extends PlayerModel<T> {
    public final ModelRenderer leftEar;
    public final ModelRenderer rightEar;
    private final ModelRenderer piglinBody;
    private final ModelRenderer piglinHead;
    private final ModelRenderer piglinLeftArm;
    private final ModelRenderer piglinRightArm;

    public CustomPiglinModel(float v, int widthIn, int heightIn) {
        super(v, false);
        this.textureWidth = widthIn;
        this.textureHeight = heightIn;
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, v);
        this.bipedHead = new ModelRenderer(this);
        this.bipedHead.setTextureOffset(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, v);
        this.bipedHead.setTextureOffset(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, v);
        this.bipedHead.setTextureOffset(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, v);
        this.bipedHead.setTextureOffset(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, v);
        this.leftEar = new ModelRenderer(this);
        this.leftEar.setRotationPoint(4.5F, -6.0F, 0.0F);
        this.leftEar.setTextureOffset(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, v);
        this.bipedHead.addChild(this.leftEar);
        this.rightEar = new ModelRenderer(this);
        this.rightEar.setRotationPoint(-4.5F, -6.0F, 0.0F);
        this.rightEar.setTextureOffset(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, v);
        this.bipedHead.addChild(this.rightEar);
        this.bipedHeadwear = new ModelRenderer(this);
        this.piglinBody = this.bipedBody.func_241662_a_(); // copies whatever is done to the biped version
        this.piglinHead = this.bipedHead.func_241662_a_(); // copies whatever is done to the biped version
        this.piglinLeftArm = this.bipedLeftArm.func_241662_a_(); // copies whatever is done to the biped version
        this.piglinRightArm = this.bipedLeftArm.func_241662_a_(); // copies whatever is done to the biped version
    }

    public void setRotationAngles(T abstractPiglinEntity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        this.bipedBody.copyModelAngles(this.piglinBody);
        this.bipedHead.copyModelAngles(this.piglinHead);
        this.bipedLeftArm.copyModelAngles(this.piglinLeftArm);
        this.bipedRightArm.copyModelAngles(this.piglinRightArm);
        super.setRotationAngles(abstractPiglinEntity, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
        float lvt_7_1_ = 0.5235988F;
        float lvt_8_1_ = p_225597_4_ * 0.1F + p_225597_2_ * 0.5F;
        float lvt_9_1_ = 0.08F + p_225597_3_ * 0.4F;
        this.leftEar.rotateAngleZ = -0.5235988F - MathHelper.cos(lvt_8_1_ * 1.2F) * lvt_9_1_;
        this.rightEar.rotateAngleZ = 0.5235988F + MathHelper.cos(lvt_8_1_) * lvt_9_1_;
        if (abstractPiglinEntity.getType() == EntityType.PIGLIN) {
            PiglinEntity piglinEntity = (PiglinEntity)abstractPiglinEntity;
            PiglinEntity.Action action = piglinEntity.func_234424_eM_();
            if (action == PiglinEntity.Action.DANCING) {
                float lvt_12_1_ = p_225597_4_ / 60.0F;
                this.rightEar.rotateAngleZ = 0.5235988F + 0.017453292F * MathHelper.sin(lvt_12_1_ * 30.0F) * 10.0F;
                this.leftEar.rotateAngleZ = -0.5235988F - 0.017453292F * MathHelper.cos(lvt_12_1_ * 30.0F) * 10.0F;
                this.bipedHead.rotationPointX = MathHelper.sin(lvt_12_1_ * 10.0F);
                this.bipedHead.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) + 0.4F;
                this.bipedRightArm.rotateAngleZ = 0.017453292F * (70.0F + MathHelper.cos(lvt_12_1_ * 40.0F) * 10.0F);
                this.bipedLeftArm.rotateAngleZ = this.bipedRightArm.rotateAngleZ * -1.0F;
                this.bipedRightArm.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) * 0.5F + 1.5F;
                this.bipedLeftArm.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) * 0.5F + 1.5F;
                this.bipedBody.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) * 0.35F;
            } else if (action == PiglinEntity.Action.ATTACKING_WITH_MELEE_WEAPON && this.swingProgress == 0.0F) {
                this.func_239117_a_(abstractPiglinEntity);
            } else if (action == PiglinEntity.Action.CROSSBOW_HOLD) {
                ModelHelper.func_239104_a_(this.bipedRightArm, this.bipedLeftArm, this.bipedHead, !abstractPiglinEntity.isLeftHanded());
            } else if (action == PiglinEntity.Action.CROSSBOW_CHARGE) {
                ModelHelper.func_239102_a_(this.bipedRightArm, this.bipedLeftArm, abstractPiglinEntity, !abstractPiglinEntity.isLeftHanded());
            } else if (action == PiglinEntity.Action.ADMIRING_ITEM) {
                this.bipedHead.rotateAngleX = 0.5F;
                this.bipedHead.rotateAngleY = 0.0F;
                if (abstractPiglinEntity.isLeftHanded()) {
                    this.bipedRightArm.rotateAngleY = -0.5F;
                    this.bipedRightArm.rotateAngleX = -0.9F;
                } else {
                    this.bipedLeftArm.rotateAngleY = 0.5F;
                    this.bipedLeftArm.rotateAngleX = -0.9F;
                }
            }
        }
        else if (abstractPiglinEntity.getType() == ModEntityTypes.PIGLIN_BRUTE.get()) {
            PiglinBruteEntity bruteEntity = (PiglinBruteEntity)abstractPiglinEntity;
            PiglinEntity.Action action = bruteEntity.getAction();
            if (action == PiglinEntity.Action.DANCING) {
                float lvt_12_1_ = p_225597_4_ / 60.0F;
                this.rightEar.rotateAngleZ = 0.5235988F + 0.017453292F * MathHelper.sin(lvt_12_1_ * 30.0F) * 10.0F;
                this.leftEar.rotateAngleZ = -0.5235988F - 0.017453292F * MathHelper.cos(lvt_12_1_ * 30.0F) * 10.0F;
                this.bipedHead.rotationPointX = MathHelper.sin(lvt_12_1_ * 10.0F);
                this.bipedHead.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) + 0.4F;
                this.bipedRightArm.rotateAngleZ = 0.017453292F * (70.0F + MathHelper.cos(lvt_12_1_ * 40.0F) * 10.0F);
                this.bipedLeftArm.rotateAngleZ = this.bipedRightArm.rotateAngleZ * -1.0F;
                this.bipedRightArm.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) * 0.5F + 1.5F;
                this.bipedLeftArm.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) * 0.5F + 1.5F;
                this.bipedBody.rotationPointY = MathHelper.sin(lvt_12_1_ * 40.0F) * 0.35F;
            } else if (action == PiglinEntity.Action.ATTACKING_WITH_MELEE_WEAPON && this.swingProgress == 0.0F) {
                this.func_239117_a_(abstractPiglinEntity);
            } else if (action == PiglinEntity.Action.CROSSBOW_HOLD) {
                ModelHelper.func_239104_a_(this.bipedRightArm, this.bipedLeftArm, this.bipedHead, !abstractPiglinEntity.isLeftHanded());
            } else if (action == PiglinEntity.Action.CROSSBOW_CHARGE) {
                ModelHelper.func_239102_a_(this.bipedRightArm, this.bipedLeftArm, abstractPiglinEntity, !abstractPiglinEntity.isLeftHanded());
            } else if (action == PiglinEntity.Action.ADMIRING_ITEM) {
                this.bipedHead.rotateAngleX = 0.5F;
                this.bipedHead.rotateAngleY = 0.0F;
                if (abstractPiglinEntity.isLeftHanded()) {
                    this.bipedRightArm.rotateAngleY = -0.5F;
                    this.bipedRightArm.rotateAngleX = -0.9F;
                } else {
                    this.bipedLeftArm.rotateAngleY = 0.5F;
                    this.bipedLeftArm.rotateAngleX = -0.9F;
                }
            }
        }
        else if (abstractPiglinEntity.getType() == EntityType.ZOMBIFIED_PIGLIN || abstractPiglinEntity.getType() == ModEntityTypes.ZIGLIN_BRUTE.get()) {
            ModelHelper.func_239105_a_(this.bipedLeftArm, this.bipedRightArm, abstractPiglinEntity.isAggressive(), this.swingProgress, p_225597_4_);
        }

        this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
        this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
        this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
        this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
        this.bipedBodyWear.copyModelAngles(this.bipedBody);
    }

    protected void func_230486_a_(T p_230486_1_, float p_230486_2_) {
        if (this.swingProgress > 0.0F && p_230486_1_ instanceof PiglinEntity && ((PiglinEntity)p_230486_1_).func_234424_eM_() == PiglinEntity.Action.ATTACKING_WITH_MELEE_WEAPON) {
            ModelHelper.func_239103_a_(this.bipedRightArm, this.bipedLeftArm, p_230486_1_, this.swingProgress, p_230486_2_);
        } else {
            super.func_230486_a_(p_230486_1_, p_230486_2_);
        }
    }

    private void func_239117_a_(T p_239117_1_) {
        if (p_239117_1_.isLeftHanded()) {
            this.bipedLeftArm.rotateAngleX = -1.8F;
        } else {
            this.bipedRightArm.rotateAngleX = -1.8F;
        }

    }
}