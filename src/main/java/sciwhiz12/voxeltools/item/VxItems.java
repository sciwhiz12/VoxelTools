package sciwhiz12.voxeltools.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sciwhiz12.voxeltools.VoxelTools;

public class VxItems {

	@ObjectHolder(VoxelTools.MODID + ":test_item")
	public static final Item test_item = null;

	/*@ObjectHolder(VoxelTools.MODID + ":jackhammer")
	public static final Item jackhammer = null;

	@ObjectHolder(VoxelTools.MODID + ":dooplicator")
	public static final Item dooplicator = null;

	@ObjectHolder(VoxelTools.MODID + ":sledge")
	public static final Item sledge = null;

	@ObjectHolder(VoxelTools.MODID + ":pliers")
	public static final Item pliers = null;

	@ObjectHolder(VoxelTools.MODID + ":paintbrush")
	public static final Item paintbrush = null;

	@ObjectHolder(VoxelTools.MODID + ":shovel")
	public static final Item shovel = null;

	@ObjectHolder(VoxelTools.MODID + ":chainsaw")
	public static final Item chainsaw = null;*/

	public static final ItemGroup CREATIVE_TAB = (new ItemGroup("voxelTools") {
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			//return new ItemStack(VxItems.jackhammer);
		    return new ItemStack(VxItems.test_item);
		}
	}).setTabPath("voxel_tools");

	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(new TestItem(TOOL_PROPERTIES).setRegistryName("test_item"));
		/*registry.register(new Jackhammer(TOOL_PROPERTIES).setRegistryName("jackhammer"));
		registry.register(new Dooplicator(TOOL_PROPERTIES).setRegistryName("dooplicator"));
		registry.register(new Sledge(TOOL_PROPERTIES).setRegistryName("sledge"));
		registry.register(new Pliers(TOOL_PROPERTIES).setRegistryName("pliers"));
		registry.register(new Paintbrush(TOOL_PROPERTIES).setRegistryName("paintbrush"));
		registry.register(new Shovel(TOOL_PROPERTIES).setRegistryName("shovel"));
		registry.register(new Chainsaw(TOOL_PROPERTIES).setRegistryName("chainsaw"));*/
	}

	private static final Item.Properties TOOL_PROPERTIES = new Item.Properties().setNoRepair()
			.addToolType(ToolType.PICKAXE, ItemTier.DIAMOND.getHarvestLevel()).defaultMaxDamage(0).maxStackSize(1)
			.group(VxItems.CREATIVE_TAB);

}
