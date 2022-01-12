package tonite.tinkersarchery.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import tonite.tinkersarchery.entities.TinkersArrowEntityOld;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class TinkersArrowRendererOld extends EntityRenderer<TinkersArrowEntityOld> {

    public static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

    private ResourceLocation[] textureLocations;

    public TinkersArrowRendererOld(EntityRendererManager p_i46193_1_, ResourceLocation... textureLocations) {
        super(p_i46193_1_);
        this.textureLocations = textureLocations;
    }

    @Override
    public ResourceLocation getTextureLocation(TinkersArrowEntityOld p_110775_1_) {
        return NORMAL_ARROW_LOCATION;
    }

    public TextureInformation getTextureLocation(TinkersArrowEntityOld entity, int partIndex) {
        if (partIndex >= 0 && partIndex < textureLocations.length) {
            IMaterial material = entity.getTool().getMaterial(partIndex);

            if (material != null) {
                // first, find a render info
                Optional<MaterialRenderInfo> optional = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material.getIdentifier());

                if (optional.isPresent()) {

                    MaterialRenderInfo info = optional.get();

                    return new TextureInformation(textureLocations[partIndex], info.getVertexColor());
                }

            }

        }

        return new TextureInformation(getTextureLocation(entity), 0xFFFFFFFF);
    }

    @Override
    public void render(TinkersArrowEntityOld arrow, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(p_225623_3_, arrow.yRotO, arrow.yRot) - 90.0F));
        p_225623_4_.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(p_225623_3_, arrow.xRotO, arrow.xRot)));
        /* So, um, what is this used for?
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = 0.15625F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = 0.15625F;
        float f7 = 0.3125F;
        float f8 = 0.05625F;*/
        float f9 = (float)arrow.shakeTime - p_225623_3_;
        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            p_225623_4_.mulPose(Vector3f.ZP.rotationDegrees(f10));
        }

        p_225623_4_.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        p_225623_4_.scale(0.05625F, 0.05625F, 0.05625F);
        p_225623_4_.translate(-4.0D, 0.0D, 0.0D);

        if (arrow.getTool() != null) {
            for (int i = 0; i < arrow.getTool().getMaterialsList().size(); i++) {
                renderPart(i, arrow, p_225623_4_, p_225623_5_, p_225623_6_);
            }
        } else {
            renderPartless(arrow, p_225623_4_, p_225623_5_, p_225623_6_);
        }

        p_225623_4_.popPose();
        super.render(arrow, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    public void renderPart(int partIndex, TinkersArrowEntityOld arrow, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        TextureInformation information = getTextureLocation(arrow, partIndex);
        IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(RenderType.entityCutout(information.resourceLocation));

        performRendering(ivertexbuilder, p_225623_4_, p_225623_6_, information.color);
    }

    public void renderPartless(TinkersArrowEntityOld arrow, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(RenderType.entityCutout(getTextureLocation(arrow)));

        performRendering(ivertexbuilder, p_225623_4_, p_225623_6_, 0xFFFFFFFF);
    }

    public void performRendering(IVertexBuilder ivertexbuilder, MatrixStack p_225623_4_, int p_225623_6_, int color) {
        MatrixStack.Entry matrixstack$entry = p_225623_4_.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, p_225623_6_, color);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, p_225623_6_, color);

        for(int j = 0; j < 4; ++j) {
            p_225623_4_.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, p_225623_6_, color);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, p_225623_6_, color);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, p_225623_6_, color);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, p_225623_6_, color);
        }
    }

    public void vertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, IVertexBuilder p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_, int color) {
        p_229039_3_.vertex(p_229039_1_, (float)p_229039_4_, (float)p_229039_5_, (float)p_229039_6_).color((color >>> 16) & 0xFF, (color >>> 8) & 0xFF, (color >>> 0) & 0xFF, (color >>> 24) & 0xFF).uv(p_229039_7_, p_229039_8_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229039_12_).normal(p_229039_2_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }

    private class TextureInformation {
        public ResourceLocation resourceLocation;
        public int color;

        public TextureInformation(ResourceLocation resourceLocation, int color) {
            this.resourceLocation = resourceLocation;
            this.color = color;
        }

    }
}
