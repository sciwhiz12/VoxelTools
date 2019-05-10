package sciwhiz12.voxeltools.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import sciwhiz12.voxeltools.VoxelTools;

@ObjectHolder(VoxelTools.MODID)
public class VxItems {

	@ObjectHolder("test_item")
	public static final BaseItem test_item = null;

	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new TestItem());
	}
}
