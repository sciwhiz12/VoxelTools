package sciwhiz12.voxeltools.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import sciwhiz12.voxeltools.VxTags;

import java.nio.file.Path;

import static sciwhiz12.voxeltools.VoxelTools.MODID;

public class TagsBlock extends ForgeBlockTagsProvider {
    protected String modId;

    public TagsBlock(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, existingFileHelper);
        this.modId = MODID;
    }

    @Override
    public void registerTags() {
        super.registerTags();

        getOrCreateBuilder(VxTags.GROUND)
                .addTag(Tags.Blocks.COBBLESTONE)
                .addTag(Tags.Blocks.DIRT)
                .addTag(Tags.Blocks.END_STONES)
                .addTag(Tags.Blocks.ORES)
                .addTag(Tags.Blocks.STONE)
                .addTag(Tags.Blocks.SANDSTONE)
                .addTag(BlockTags.field_242173_aI)
                .addTag(BlockTags.CORAL_BLOCKS)
                .addTag(BlockTags.ICE)
                .addTag(BlockTags.field_242171_aD)
                .addTag(BlockTags.SAND)
                .add(Blocks.BONE_BLOCK)
                .add(Blocks.CLAY)
                .add(Blocks.FARMLAND)
                .add(Blocks.GLOWSTONE)
                .add(Blocks.GRASS_PATH)
                .add(Blocks.GRAVEL)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.SNOW)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.SOUL_SAND)
                .add(Blocks.SOUL_SOIL);

        getOrCreateBuilder(VxTags.TREE_MATTER)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.LOGS)
                .addTag(BlockTags.SAPLINGS)
                .add(Blocks.NETHER_WART_BLOCK)
                .add(Blocks.SHROOMLIGHT)
                .add(Blocks.WARPED_WART_BLOCK);

        getOrCreateBuilder(VxTags.NETHER_VEGETATION)
                .add(Blocks.CRIMSON_FUNGUS)
                .add(Blocks.CRIMSON_ROOTS)
                .add(Blocks.MUSHROOM_STEM)
                .add(Blocks.NETHER_SPROUTS)
                .add(Blocks.NETHER_WART)
                .add(Blocks.TWISTING_VINES)
                .add(Blocks.TWISTING_VINES_PLANT)
                .add(Blocks.WARPED_FUNGUS)
                .add(Blocks.WARPED_ROOTS)
                .add(Blocks.WEEPING_VINES)
                .add(Blocks.WEEPING_VINES_PLANT);

        getOrCreateBuilder(VxTags.VEGETATION)
                .addTag(VxTags.NETHER_VEGETATION)
                .addTag(VxTags.TREE_MATTER)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.UNDERWATER_BONEMEALS)
                .add(Blocks.BROWN_MUSHROOM)
                .add(Blocks.BROWN_MUSHROOM_BLOCK)
                .add(Blocks.CACTUS)
                .add(Blocks.DEAD_BUSH)
                .add(Blocks.FERN)
                .add(Blocks.GRASS)
                .add(Blocks.KELP)
                .add(Blocks.LARGE_FERN)
                .add(Blocks.MELON)
                .add(Blocks.RED_MUSHROOM)
                .add(Blocks.RED_MUSHROOM_BLOCK)
                .add(Blocks.PUMPKIN)
                .add(Blocks.SEA_PICKLE)
                .add(Blocks.SUGAR_CANE)
                .add(Blocks.TALL_GRASS)
                .add(Blocks.TALL_SEAGRASS)
                .add(Blocks.VINE)
                .add(Blocks.WHEAT);
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        if (!id.getNamespace().equals(modId)) return null;
        return super.makePath(id);
    }

    @Override
    public String getName() {
        return "Block Tags: " + modId;
    }
}
