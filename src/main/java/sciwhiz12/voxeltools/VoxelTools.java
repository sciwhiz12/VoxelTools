package sciwhiz12.voxeltools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.PermissionAPI;
import sciwhiz12.voxeltools.item.VxItems;
import sciwhiz12.voxeltools.net.VxNetwork;

@Mod(VoxelTools.MODID)
public class VoxelTools {
	public static final String MODID = "voxeltools";

	public static final Logger LOGGER = LogManager.getLogger();

	public VoxelTools() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, VxConfig.serverSpec);
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setting up common...");
		VxNetwork.registerPackets();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void handleServerAboutToStartEvent(final FMLServerAboutToStartEvent event) {
		PermissionAPI.registerNode(VxConfig.ITEM_USE_PERMISSION, VxConfig.SERVER.defaultPermLevel.get(),
				"Allows the use of VoxelTools.");
	}

	@Mod.EventBusSubscriber(bus = Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			VxItems.registerItems(event);
		}
	}
}
