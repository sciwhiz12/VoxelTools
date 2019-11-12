package sciwhiz12.voxeltools.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import sciwhiz12.voxeltools.VoxelTools;

public class VxItems {

	@ObjectHolder(VoxelTools.MODID + ":test_item")
	public static final BaseItem test_item = null;

	@ObjectHolder(VoxelTools.MODID + ":jackhammer")
	public static final BaseItem jackhammer = null;

	@ObjectHolder(VoxelTools.MODID + ":dooplicator")
	public static final BaseItem dooplicator = null;

	@ObjectHolder(VoxelTools.MODID + ":sledge")
	public static final BaseItem sledge = null;

	@ObjectHolder(VoxelTools.MODID + ":pliers")
	public static final BaseItem pliers = null;

	@ObjectHolder(VoxelTools.MODID + ":paintbrush")
	public static final BaseItem paintbrush = null;

	@ObjectHolder(VoxelTools.MODID + ":shovel")
	public static final BaseItem shovel = null;
	
	@ObjectHolder(VoxelTools.MODID + ":chainsaw")
	public static final BaseItem chainsaw = null;

	public static final ItemGroup CREATIVE_TAB = (new ItemGroup("voxelTools") {
	      @OnlyIn(Dist.CLIENT)
	      public ItemStack createIcon() {
	         return new ItemStack(VxItems.jackhammer);
	      }
	   }).setTabPath("voxel_tools");

	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new TestItem(), new Jackhammer(), new Dooplicator(), new Sledge(), new Pliers(),
				new Paintbrush(), new Shovel(), new Chainsaw());
	}
}
