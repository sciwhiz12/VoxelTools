package sciwhiz12.voxeltools.client.model;

import java.util.function.Function;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.item.VxItems;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD, modid = VoxelTools.MODID)
public class ModelHandler {
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        overrideItemModel(event, VxItems.paintbrush.get(), ISTERWrapper::new);
    }

    private static void overrideItemModel(ModelBakeEvent event, Item item, Function<IBakedModel, IBakedModel> transform) {
        overrideModel(event, new ModelResourceLocation(item.getRegistryName(), "inventory"), transform);
    }

    private static void overrideModel(ModelBakeEvent event, ModelResourceLocation mrl,
            Function<IBakedModel, IBakedModel> transform) {
        IBakedModel originalModel = event.getModelRegistry().get(mrl);
        if (originalModel != null) {
            IBakedModel transformedModel = transform.apply(originalModel);
            event.getModelRegistry().put(mrl, transformedModel);
        }
    }
}
