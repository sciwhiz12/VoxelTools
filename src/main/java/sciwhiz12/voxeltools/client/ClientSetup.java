package sciwhiz12.voxeltools.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.item.ClockItem;
import sciwhiz12.voxeltools.item.VxItems;

@EventBusSubscriber(value = Dist.CLIENT, modid = VoxelTools.MODID, bus = Bus.MOD)
public class ClientSetup {

    public static final IItemPropertyGetter CLOCK_PROPERTY = (stack, clientWorld, livingEntity) -> {
        double value = 0F;
        Entity entity = livingEntity != null ? livingEntity : stack.getAttachedEntity();
        if (entity != null) {
            if (clientWorld == null && entity.world instanceof ClientWorld) {
                clientWorld = (ClientWorld) entity.world;
            }
            if (clientWorld != null) {
                value = clientWorld.func_230315_m_().func_236032_b_(stack.getOrCreateTag().getLong(ClockItem.TAG_FIXED_TIME));
            }
        }
        return (float) value;
    };

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        VoxelTools.LOGGER.debug("Setting up client...");
        ItemModelsProperties.func_239418_a_(VxItems.clock.get(), ClockItem.TIME_PREDICATE, CLOCK_PROPERTY);
    }
}
