package com.clappingcat.client;

import com.clappingcat.ClappingCatMod;
import com.clappingcat.entity.ClappingCatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ClappingCatRenderer extends MobRenderer<ClappingCatEntity, ClappingCatModel> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(ClappingCatMod.MOD_ID, "clapping_cat"), "main");

    /** How high the cat floats above its shadow, in blocks. */
    private static final double HOVER = 0.25D;

    public ClappingCatRenderer(EntityRendererProvider.Context context) {
        super(context, new ClappingCatModel(context.bakeLayer(LAYER)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(ClappingCatEntity entity) {
        return entity.getTextureLocation();
    }

    @Override
    protected void scale(ClappingCatEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.8F, 0.8F, 0.8F);
    }

    @Override
    public void render(ClappingCatEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        float t = entity.tickCount + partialTicks;
        double bob = 0.06D * Math.sin(t * ClappingCatEntity.CLAP_OMEGA * 0.5D);
        poseStack.pushPose();
        poseStack.translate(0.0D, HOVER + bob, 0.0D);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
