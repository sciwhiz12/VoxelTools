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
    public void func_239207_a_(ItemStack itemStack, TransformType transform, MatrixStack matrixStack,
            IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        final Minecraft mc = Minecraft.getInstance();
        final ItemRenderer itemRenderer = mc.getItemRenderer();
        boolean renderItem = true;
        IBakedModel brushModel = itemRenderer.getItemModelWithOverrides(itemStack, null, null);
        if (transform == TransformType.GUI) {
            CompoundNBT tag = itemStack.getChildTag(PaintbrushItem.TAG_ID_STOREDBLOCK);
            if (tag != null) {
                matrixStack.push();
                matrixStack.rotate(Vector3f.XP.rotationDegrees(30));
                matrixStack.rotate(Vector3f.YP.rotationDegrees(225));
                if (Screen.hasShiftDown()) {
                    renderItem = false;
                    matrixStack.translate(-0.66D, 0.25D, -0.66D);
                    matrixStack.scale(0.625F, 0.625F, 0.625F);
                } else {
                    matrixStack.scale(0.35F, 0.35F, 0.35F);
                    matrixStack.translate(-2D, 0.5D, -2D);
                }
                RenderHelper.setupGui3DDiffuseLighting();
                BlockState state = NBTUtil.readBlockState(tag);
                IRenderTypeBuffer.Impl specialBuffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                Minecraft.getInstance().getBlockRendererDispatcher()
                        .renderBlock(state, matrixStack, specialBuffer, combinedLight, combinedOverlay,
                                EmptyModelData.INSTANCE);
                specialBuffer.finish();
                RenderHelper.setupGuiFlatDiffuseLighting();
                matrixStack.pop();
            }
        }
        if (renderItem) {
            matrixStack.push();
            IVertexBuilder builder = ItemRenderer
                    .getBuffer(buffer, RenderTypeLookup.func_239219_a_(itemStack, true), true, itemStack.hasEffect());
            itemRenderer.renderQuads(matrixStack, builder,
                    brushModel.getQuads(null, null, new Random(42L), EmptyModelData.INSTANCE), itemStack, combinedLight,
                    combinedOverlay);
            matrixStack.pop();
        }
    }
}
