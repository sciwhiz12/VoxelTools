package sciwhiz12.voxeltools;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class VxTags {
    public static final Tags.IOptionalNamedTag<Block> GROUND = blockTag("ground");

    public static final Tags.IOptionalNamedTag<Block> TREE_MATTER = blockTag("tree_matter");

    public static final Tags.IOptionalNamedTag<Block> NETHER_VEGETATION = blockTag("nether_vegetation");
    public static final Tags.IOptionalNamedTag<Block> VEGETATION = blockTag("vegetation");

    private static Tags.IOptionalNamedTag<Block> blockTag(String name) {
        return BlockTags.createOptional(new ResourceLocation(VoxelTools.MODID, name));
    }
}
