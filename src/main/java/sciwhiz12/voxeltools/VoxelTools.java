package sciwhiz12.voxeltools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VoxelTools.MODID)
public class VoxelTools {
	public static final String MODID = "voxeltools";

	public static final Logger LOGGER = LogManager.getLogger();

	public VoxelTools() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setting up common...");
	}

	private void setupClient(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		LOGGER.info("Setting up client...");
	}

	@Mod.EventBusSubscriber(bus = Bus.MOD)
	public static class RegistryEvents {
	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD)
	public static class ClientRegistryEvents {
	}
}
