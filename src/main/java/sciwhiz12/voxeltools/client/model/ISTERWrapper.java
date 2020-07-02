package sciwhiz12.voxeltools.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.client.model.BakedModelWrapper;

public class ISTERWrapper extends BakedModelWrapper<IBakedModel> {
    private TransformType currentPerspective;

    public ISTERWrapper(IBakedModel parent, TransformType perspective) {
        super(parent);
        this.currentPerspective = perspective;
    }

    public ISTERWrapper(IBakedModel parent) {
        this(parent, TransformType.NONE);
    }

    public IBakedModel getOriginalModel() {
        return originalModel;
    }

    public TransformType getCurrentPerspective() {
        return currentPerspective;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        this.currentPerspective = cameraTransformType;
        return new ISTERWrapper(originalModel.handlePerspective(cameraTransformType, mat), cameraTransformType);
    }
}
