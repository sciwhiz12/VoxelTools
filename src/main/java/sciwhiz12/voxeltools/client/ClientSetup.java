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
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.item.ClockItem;
import sciwhiz12.voxeltools.item.VxItems;

import static sciwhiz12.voxeltools.VoxelTools.LOGGER;

@EventBusSubscriber(value = Dist.CLIENT, modid = VoxelTools.MODID, bus = Bus.MOD)
public class ClientSetup {
    public static final Marker CLIENT = MarkerManager.getMarker("CLIENT");

    public static final IItemPropertyGetter CLOCK_PROPERTY = (stack, clientWorld, livingEntity) -> {
        double value = 0F;
        Entity entity = livingEntity != null ? livingEntity : stack.getEntityRepresentation();
        if (entity != null) {
            if (clientWorld == null && entity.level instanceof ClientWorld) {
                clientWorld = (ClientWorld) entity.level;
            }
            if (clientWorld != null) {
                value = clientWorld.dimensionType().timeOfDay(stack.getOrCreateTag().getLong(ClockItem.TAG_FIXED_TIME));
            }
        }
        return (float) value;
    };

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.debug(CLIENT, "Setting up client...");
        ItemModelsProperties.register(VxItems.clock.get(), ClockItem.TIME_PREDICATE, CLOCK_PROPERTY);
    }
}
