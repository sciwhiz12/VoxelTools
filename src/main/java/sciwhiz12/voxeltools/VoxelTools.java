package sciwhiz12.voxeltools;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import sciwhiz12.voxeltools.item.VxItems;
import sciwhiz12.voxeltools.net.VxNetwork;

@Mod(VoxelTools.MODID)
public class VoxelTools {
    public static final String MODID = "voxeltools";

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Marker COMMON = MarkerManager.getMarker("COMMON");

    public VoxelTools() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        VxItems.ITEMS.register(modBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, VxConfig.serverSpec);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info(COMMON, "Setting up common...");

        VxNetwork.registerPackets();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void handleServerAboutToStartEvent(final FMLServerAboutToStartEvent event) {
        PermissionAPI.registerNode(VxConfig.ITEM_USE_PERMISSION, VxConfig.Server.defaultPermLevel,
                "Allows the use of VoxelTools.");
    }
}
