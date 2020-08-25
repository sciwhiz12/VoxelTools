package sciwhiz12.voxeltools.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import static sciwhiz12.voxeltools.VoxelTools.LOGGER;
import static sciwhiz12.voxeltools.VoxelTools.MODID;

@EventBusSubscriber(modid = MODID, bus = Bus.MOD)
public class DataGen {
    public static final Marker DATAGEN = MarkerManager.getMarker("DATAGEN");

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        LOGGER.debug(DATAGEN, "Gathering data for data generation");
        DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {
            LOGGER.debug(DATAGEN, "Adding data providers for client assets");
            gen.addProvider(new Languages(gen));
            gen.addProvider(new ItemModels(gen, event.getExistingFileHelper()));
        }
        if (event.includeServer()) {
            LOGGER.debug(DATAGEN, "Adding data providers for server data");
            gen.addProvider(new TagsBlock(gen));
        }
    }
}
