package sciwhiz12.voxeltools.client.render;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.client.model.data.EmptyModelData;
import sciwhiz12.voxeltools.client.model.ISTERWrapper;
import sciwhiz12.voxeltools.item.Paintbrush;

public class PaintbrushRenderer extends ItemStackTileEntityRenderer {
    @Override
    public void render(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
            int combinedOverlay) {
        final Minecraft mc = Minecraft.getInstance();
        final ItemRenderer itemRenderer = mc.getItemRenderer();
        boolean renderingBlockFull = false;
        IBakedModel brushModel = itemRenderer.getItemModelWithOverrides(itemStack, null, null);
        if (brushModel instanceof ISTERWrapper) {
            if (((ISTERWrapper) brushModel).getCurrentPerspective() == TransformType.GUI) {
                CompoundNBT tag = itemStack.getChildTag(Paintbrush.TAG_ID_STOREDBLOCK);
                if (tag != null) {
                    BlockState state = NBTUtil.readBlockState(tag);
                    IBakedModel model = mc.getBlockRendererDispatcher().getModelForState(state);
                    IVertexBuilder vertex = buffer.getBuffer(RenderTypeLookup.getRenderType(state));
                    matrixStack.push();
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(30));
                    matrixStack.rotate(Vector3f.YP.rotationDegrees(225));
                    if (Screen.hasShiftDown()) {
                        renderingBlockFull = true;
                        matrixStack.translate(-0.66D, 0.25D, -0.66D);
                        matrixStack.scale(0.625F, 0.625F, 0.625F);
                    } else {
                        matrixStack.scale(0.35F, 0.35F, 0.35F);
                        matrixStack.translate(-2D, 0.5D, -2D);
                    }
                    Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                        matrixStack.getLast(), vertex, state, model, 0.F, 0.F, 0.F, combinedLight, combinedOverlay,
                        EmptyModelData.INSTANCE
                    );
                    matrixStack.pop();
                }
            }
        }
        if (!renderingBlockFull) {
            matrixStack.push();
            IVertexBuilder builder = ItemRenderer.getBuffer(
                buffer, RenderTypeLookup.getRenderType(itemStack), true, itemStack.hasEffect()
            );
            itemRenderer.renderQuads(
                matrixStack, builder, brushModel.getQuads(null, null, new Random(42L)), itemStack, combinedLight,
                combinedOverlay
            );
            matrixStack.pop();
        }
    }
}
