package sciwhiz12.voxeltools.client;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.item.Clock;
import sciwhiz12.voxeltools.item.VxItems;

@EventBusSubscriber(value = Dist.CLIENT, modid = VoxelTools.MODID, bus = Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        VoxelTools.LOGGER.debug("Setting up client...");
        ItemModelsProperties
                .func_239418_a_(VxItems.clock.get(), new ResourceLocation("time"), (stack, world, livingEntity) -> {
                    Entity entity = livingEntity != null ? livingEntity : stack.getItemFrame();
                    double value = 0.0F;
                    if (world != null) {
                        if (world.func_230315_m_().func_236043_f_()) {
                            value = world.func_230315_m_()
                                    .func_236032_b_(stack.getOrCreateTag().getLong(Clock.TAG_FIXED_TIME));
                        }
                    }
                    return (float) value;
                });
    }
}
