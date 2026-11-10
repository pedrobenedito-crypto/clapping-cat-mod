package com.clappingcat.client;

import com.clappingcat.entity.ClappingCatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * Upright cat. Uses the same box sizes and UV offsets as the vanilla cat model,
 * so every vanilla cat skin (64x32) maps onto it correctly.
 */
public class ClappingCatModel extends EntityModel<ClappingCatEntity> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart tail1;
    private final ModelPart tail2;

    public ClappingCatModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.tail1 = root.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Torso (4 wide x 16 tall x 6 deep), standing up. Ground is y = 24.
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -8.0F, -3.0F, 4.0F, 16.0F, 6.0F),
                PartPose.offset(0.0F, 10.0F, 0.0F));

        // Head with nose and ears.
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F)
                        .texOffs(0, 24).addBox(-1.5F, -0.001F, -3.5F, 3.0F, 2.0F, 2.0F)
                        .texOffs(0, 10).addBox(-2.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F)
                        .texOffs(6, 10).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 0.5F, 0.0F));

        // Hind legs.
        root.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(1.1F, 18.0F, 0.0F));
        root.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(-1.1F, 18.0F, 0.0F));

        // Front legs used as arms.
        root.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offset(3.0F, 4.0F, -1.0F));
        root.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                PartPose.offset(-3.0F, 4.0F, -1.0F));

        // Tail (two segments).
        PartDefinition tail1 = root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F),
                PartPose.offset(0.0F, 16.0F, 3.5F));
        tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(4, 15).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(ClappingCatEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float t = ageInTicks * ClappingCatEntity.CLAP_OMEGA;
        // phase = 1 -> paws touching (this is the moment the clap sound plays).
        float phase = (Mth.sin(t) + 1.0F) * 0.5F;
        float spread = 0.02F + 0.26F * phase;
        float forward = -0.85F + 0.08F * Mth.sin(t * 0.5F);

        this.leftArm.xRot = forward;
        this.rightArm.xRot = forward;
        this.leftArm.zRot = spread;
        this.rightArm.zRot = -spread;

        this.head.xRot = 0.12F * Mth.sin(t);
        this.head.yRot = Mth.clamp(netHeadYaw, -35.0F, 35.0F) * ((float) Math.PI / 180.0F);
        this.head.zRot = 0.10F * Mth.sin(t * 0.5F);

        this.tail1.xRot = 0.5F;
        this.tail1.zRot = 0.35F * Mth.sin(t * 0.5F);
        this.tail2.xRot = 0.4F;
        this.tail2.zRot = 0.3F * Mth.sin(t * 0.5F + 1.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
