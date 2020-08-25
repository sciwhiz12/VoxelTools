package sciwhiz12.voxeltools.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sciwhiz12.voxeltools.VoxelTools;

public class VxItems {
    public static final ItemGroup CREATIVE_TAB = (new ItemGroup("voxelTools") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(VxItems.dooplicator.get());
        }
    }).setTabPath("voxel_tools");

    private static final Item.Properties TOOL_PROPERTIES = new Item.Properties().setNoRepair()
            .addToolType(ToolType.PICKAXE, ItemTier.DIAMOND.getHarvestLevel()).defaultMaxDamage(0).maxStackSize(1)
            .group(VxItems.CREATIVE_TAB);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VoxelTools.MODID);

    public static final RegistryObject<Item> test_item = ITEMS.register("test_item", () -> new TestItem(TOOL_PROPERTIES));

    public static final RegistryObject<DooplicatorItem> dooplicator = ITEMS
            .register("dooplicator", () -> new DooplicatorItem(TOOL_PROPERTIES));

    public static final RegistryObject<JackhammerItem> jackhammer = ITEMS
            .register("jackhammer", () -> new JackhammerItem(TOOL_PROPERTIES));

    public static final RegistryObject<PaintbrushItem> paintbrush = ITEMS
            .register("paintbrush", () -> new PaintbrushItem(TOOL_PROPERTIES));

    public static final RegistryObject<SledgeItem> sledge = ITEMS.register("sledge", () -> new SledgeItem(TOOL_PROPERTIES));

    public static final RegistryObject<PliersItem> pliers = ITEMS.register("pliers", () -> new PliersItem(TOOL_PROPERTIES));

    public static final RegistryObject<ShovelItem> shovel = ITEMS.register("shovel", () -> new ShovelItem(TOOL_PROPERTIES));

    public static final RegistryObject<ChainsawItem> chainsaw = ITEMS
            .register("chainsaw", () -> new ChainsawItem(TOOL_PROPERTIES));

    public static final RegistryObject<ClockItem> clock = ITEMS.register("clock", () -> new ClockItem(TOOL_PROPERTIES));
}
