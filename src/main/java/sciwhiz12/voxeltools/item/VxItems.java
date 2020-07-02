package sciwhiz12.voxeltools.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sciwhiz12.voxeltools.VoxelTools;

public class VxItems {
    public static final ItemGroup CREATIVE_TAB = (new ItemGroup("voxelTools") {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(VxItems.dooplicator.get());
        }
    }).setTabPath("voxel_tools");
    private static final Item.Properties TOOL_PROPERTIES = new Item.Properties().setNoRepair()
            .addToolType(ToolType.PICKAXE, ItemTier.DIAMOND.getHarvestLevel()).defaultMaxDamage(0).maxStackSize(1)
            .group(VxItems.CREATIVE_TAB);

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<Item>(ForgeRegistries.ITEMS, VoxelTools.MODID);

    public static final RegistryObject<Item> test_item = ITEMS.register("test_item", () -> new TestItem(TOOL_PROPERTIES));

    public static final RegistryObject<Item> dooplicator = ITEMS
            .register("dooplicator", () -> new Dooplicator(TOOL_PROPERTIES));

    public static final RegistryObject<Item> jackhammer = ITEMS
            .register("jackhammer", () -> new Jackhammer(TOOL_PROPERTIES));

    public static final RegistryObject<Item> paintbrush = ITEMS
            .register("paintbrush", () -> new Paintbrush(TOOL_PROPERTIES));

    public static final RegistryObject<Item> sledge = ITEMS.register("sledge", () -> new Sledge(TOOL_PROPERTIES));

    public static final RegistryObject<Item> pliers = ITEMS.register("pliers", () -> new Pliers(TOOL_PROPERTIES));

    public static final RegistryObject<Item> shovel = ITEMS.register("shovel", () -> new Shovel(TOOL_PROPERTIES));

    public static final RegistryObject<Item> chainsaw = ITEMS.register("chainsaw", () -> new Chainsaw(TOOL_PROPERTIES));

    public static final RegistryObject<Item> clock = ITEMS.register("clock", () -> new Clock(TOOL_PROPERTIES));
}
