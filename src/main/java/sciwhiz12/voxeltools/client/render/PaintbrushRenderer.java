package sciwhiz12.voxeltools.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import sciwhiz12.voxeltools.item.PaintbrushItem;

import java.util.Random;

public class PaintbrushRenderer extends ItemStackTileEntityRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, TransformType transform, MatrixStack matrixStack,
            IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        final Minecraft mc = Minecraft.getInstance();
        final ItemRenderer itemRenderer = mc.getItemRenderer();
        boolean renderItem = true;
        IBakedModel brushModel = itemRenderer.getModel(itemStack, null, null);
        if (transform == TransformType.GUI) {
            CompoundNBT tag = itemStack.getTagElement(PaintbrushItem.TAG_ID_STOREDBLOCK);
            if (tag != null) {
                matrixStack.pushPose();
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(30));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(225));
                if (Screen.hasShiftDown()) {
                    renderItem = false;
                    matrixStack.translate(-0.66D, 0.25D, -0.66D);
                    matrixStack.scale(0.625F, 0.625F, 0.625F);
                } else {
                    matrixStack.scale(0.35F, 0.35F, 0.35F);
                    matrixStack.translate(-2D, 0.5D, -2D);
                }
                RenderHelper.setupFor3DItems();
                BlockState state = NBTUtil.readBlockState(tag);
                IRenderTypeBuffer.Impl specialBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
                Minecraft.getInstance().getBlockRenderer()
                        .renderBlock(state, matrixStack, specialBuffer, combinedLight, combinedOverlay,
                                EmptyModelData.INSTANCE);
                specialBuffer.endBatch();
                RenderHelper.setupForFlatItems();
                matrixStack.popPose();
            }
        }
        if (renderItem) {
            matrixStack.pushPose();
            IVertexBuilder builder = ItemRenderer
                    .getFoilBuffer(buffer, RenderTypeLookup.getRenderType(itemStack, true), true, itemStack.hasFoil());
            itemRenderer.renderQuadList(matrixStack, builder,
                    brushModel.getQuads(null, null, new Random(42L), EmptyModelData.INSTANCE), itemStack, combinedLight,
                    combinedOverlay);
            matrixStack.popPose();
        }
    }
}
