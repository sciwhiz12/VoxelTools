package sciwhiz12.voxeltools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sciwhiz12.voxeltools.item.VxItems;

@Mod(VoxelTools.MODID)
public class VoxelTools {
	public static final String MODID = "voxeltools";

	public static final Logger LOGGER = LogManager.getLogger();

	public VoxelTools() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setting up common...");
	}
	
	@Mod.EventBusSubscriber(bus = Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			VxItems.registerItems(event);
		}
	}
}
