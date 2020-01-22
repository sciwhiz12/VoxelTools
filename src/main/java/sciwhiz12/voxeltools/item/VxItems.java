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
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            // return new ItemStack(VxItems.jackhammer);
            return new ItemStack(VxItems.test_item.get());
        }
    }).setTabPath("voxel_tools");
    private static final Item.Properties TOOL_PROPERTIES = new Item.Properties().setNoRepair()
            .addToolType(ToolType.PICKAXE, ItemTier.DIAMOND.getHarvestLevel()).defaultMaxDamage(0)
            .maxStackSize(1).group(VxItems.CREATIVE_TAB);

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<Item>(
            ForgeRegistries.ITEMS, VoxelTools.MODID
    );

    public static final RegistryObject<Item> test_item = ITEMS.register(
            "test_item", () -> new TestItem(TOOL_PROPERTIES)
    );

    /*
     * @ObjectHolder(VoxelTools.MODID + ":jackhammer") public static final Item
     * jackhammer = null;
     * 
     * @ObjectHolder(VoxelTools.MODID + ":dooplicator") public static final Item
     * dooplicator = null;
     * 
     * @ObjectHolder(VoxelTools.MODID + ":sledge") public static final Item sledge =
     * null;
     * 
     * @ObjectHolder(VoxelTools.MODID + ":pliers") public static final Item pliers =
     * null;
     * 
     * @ObjectHolder(VoxelTools.MODID + ":paintbrush") public static final Item
     * paintbrush = null;
     * 
     * @ObjectHolder(VoxelTools.MODID + ":shovel") public static final Item shovel =
     * null;
     * 
     * @ObjectHolder(VoxelTools.MODID + ":chainsaw") public static final Item
     * chainsaw = null;
     */
}
