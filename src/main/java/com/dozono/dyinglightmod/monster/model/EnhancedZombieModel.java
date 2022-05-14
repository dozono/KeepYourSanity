package com.dozono.dyinglightmod.monster.model;

import com.dozono.dyinglightmod.monster.entities.EnhancedZombieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class EnhancedZombieModel <T extends EnhancedZombieEntity> extends EntityModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer rightLeg;
    private final ModelRenderer rightArm;
    private final ModelRenderer bone2;
    private final ModelRenderer rightArm_r1;
    private final ModelRenderer cube_r1_r1;
    private final ModelRenderer bone3;
    private final ModelRenderer rightArm_r2;
    private final ModelRenderer cube_r1_r3;
    private final ModelRenderer leftLeg;
    private final ModelRenderer leftArm;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1_r2;
    private final ModelRenderer leftArm_r1;

    public EnhancedZombieModel() {

        texWidth = 128;
        texHeight = 128;

        head = new ModelRenderer(this);
        head.setPos(0.5F, -4.0F, 0.0F);
        head.setTexSize(0, 28).addBox(-6.0F, -12.0F, -6.0F, 11.0F, 12.0F, 13.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 26.0F, 0.0F);
        body.setTexSize(0, 0).addBox(-9.0F, -31.0F, -4.0F, 18.0F, 18.0F, 9.0F, 0.0F, false);

        rightLeg = new ModelRenderer(this);
        rightLeg.setPos(-4.0F, 11.0F, 1.0F);
        rightLeg.setTexSize(55, 0).addBox(-4.0F, 0.0F, -4.0F, 7.0F, 13.0F, 7.0F, 0.0F, false);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(-8.0F, -1.0F, -4.0F);


        bone2 = new ModelRenderer(this);
        bone2.setPos(-17.0F, 27.0F, 0.0F);
        rightArm.addChild(bone2);


        rightArm_r1 = new ModelRenderer(this);
        rightArm_r1.setPos(25.0F, 0.0F, 0.0F);
        bone2.addChild(rightArm_r1);
        setRotationAngle(rightArm_r1, -1.5708F, 0.0F, 0.0F);
        rightArm_r1.setTexSize(42, 47).addBox(-16.0F, -6.0F, -30.0F, 7.0F, 19.0F, 7.0F, 0.0F, false);

        cube_r1_r1 = new ModelRenderer(this);
        cube_r1_r1.setPos(0.0F, 0.0F, -4.0F);
        bone2.addChild(cube_r1_r1);
        setRotationAngle(cube_r1_r1, 0.3491F, 0.0F, 0.0F);
        cube_r1_r1.setTexSize(35, 28).addBox(9.3F, -31.2F, -7.9F, 1.0F, -1.0F, 4.0F, 0.0F, false);
        cube_r1_r1.setTexSize(35, 28).addBox(12.1F, -31.2F, -7.9F, 1.0F, -1.0F, 4.0F, 0.0F, false);
        cube_r1_r1.setTexSize(35, 28).addBox(14.8F, -31.2F, -7.9F, 1.0F, -1.0F, 4.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-17.0F, 27.0F, 0.0F);
        rightArm.addChild(bone3);
        bone3.setTexSize(44, 78).addBox(12.1F, -28.0F, -19.0F, 1.0F, -1.0F, 13.0F, 0.0F, false);
        bone3.setTexSize(42, 75).addBox(14.8F, -28.0F, -19.0F, 1.0F, -1.0F, 13.0F, 0.0F, false);
        bone3.setTexSize(42, 81).addBox(9.3F, -28.0F, -19.0F, 1.0F, -1.0F, 13.0F, 0.0F, false);

        rightArm_r2 = new ModelRenderer(this);
        rightArm_r2.setPos(25.0F, 0.0F, 0.0F);
        bone3.addChild(rightArm_r2);
        setRotationAngle(rightArm_r2, -1.5708F, 0.0F, 0.0F);
        rightArm_r2.setTexSize(42, 47).addBox(-16.0F, -6.0F, -30.0F, 7.0F, 19.0F, 7.0F, 0.0F, false);

        cube_r1_r3 = new ModelRenderer(this);
        cube_r1_r3.setPos(0.0F, 0.0F, -4.0F);
        bone3.addChild(cube_r1_r3);
        setRotationAngle(cube_r1_r3, 0.3491F, 0.0F, 0.0F);


        leftLeg = new ModelRenderer(this);
        leftLeg.setPos(4.0F, 11.0F, 0.0F);
        leftLeg.setTexSize(0, 54).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 13.0F, 7.0F, 0.0F, false);

        leftArm = new ModelRenderer(this);
        leftArm.setPos(8.0F, -1.0F, 0.0F);


        bone = new ModelRenderer(this);
        bone.setPos(-8.0F, 27.0F, 0.0F);
        leftArm.addChild(bone);
        bone.setTexSize(44, 82).addBox(14.8F, -28.0F, -23.0F, 1.0F, -1.0F, 13.0F, 0.0F, false);
        bone.setTexSize(44, 82).addBox(12.1F, -28.0F, -23.0F, 1.0F, -1.0F, 13.0F, 0.0F, false);
        bone.setTexSize(41, 78).addBox(9.3F, -28.0F, -23.0F, 1.0F, -1.0F, 13.0F, 0.0F, false);

        cube_r1_r2 = new ModelRenderer(this);
        cube_r1_r2.setPos(0.0F, 0.0F, 0.0F);
        bone.addChild(cube_r1_r2);
        setRotationAngle(cube_r1_r2, 0.3491F, 0.0F, 0.0F);
        cube_r1_r2.setTexSize(3, 28).addBox(14.8F, -34.2F, -15.5F, 1.0F, -1.0F, 4.0F, 0.0F, false);
        cube_r1_r2.setTexSize(1, 28).addBox(12.1F, -34.2F, -15.5F, 1.0F, -1.0F, 4.0F, 0.0F, false);
        cube_r1_r2.setTexSize(0, 32).addBox(9.3F, -34.2F, -15.5F, 1.0F, -1.0F, 4.0F, 0.0F, false);

        leftArm_r1 = new ModelRenderer(this);
        leftArm_r1.setPos(0.0F, 0.0F, 0.0F);
        bone.addChild(leftArm_r1);
        setRotationAngle(leftArm_r1, -1.5708F, 0.0F, 0.0F);
        leftArm_r1.setTexSize(49, 21).addBox(9.0F, -3.0F, -30.0F, 7.0F, 19.0F, 7.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount,
                                  float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight,
                       int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
