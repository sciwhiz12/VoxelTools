package sciwhiz12.voxeltools.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import sciwhiz12.voxeltools.item.ClockItem;
import sciwhiz12.voxeltools.item.VxItems;

import static sciwhiz12.voxeltools.VoxelTools.MODID;

public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        toolItem(VxItems.test_item);
        toolItem(VxItems.dooplicator);
        toolItem(VxItems.jackhammer);
        toolItem(VxItems.paintbrush);
        toolItem(VxItems.sledge);
        toolItem(VxItems.pliers);
        toolItem(VxItems.shovel);
        toolItem(VxItems.chainsaw);

        clockItem(VxItems.clock, ClockItem.TIME_PREDICATE);
    }

    void toolItem(RegistryObject<? extends Item> i) {
        singleTextureItem(i.getId(), factory.apply(mcLoc("item/handheld")));
    }

    void singleTextureItem(ResourceLocation item, ModelFile parent) {
        final ResourceLocation location = itemLoc(item);
        ItemModelBuilder builder = factory.apply(location).parent(parent);
        generatedModels.put(location, builder.texture("layer0", location));
    }

    void clockItem(RegistryObject<? extends Item> item, ResourceLocation overrideName) {
        final ResourceLocation itemLoc = item.getId();
        // TODO: datagen the main clock model
        for (int iter = 1; iter < 64; iter++) {
            final ResourceLocation loc = clockLoc(itemLoc, iter);
            ItemModelBuilder overrideItem = factory.apply(loc).parent(factory.apply(mcLoc("item/generated")));
            overrideItem.texture("layer0", loc);
            generatedModels.put(loc, overrideItem);
        }
    }

    ResourceLocation clockLoc(ResourceLocation loc, int number) {
        return new ResourceLocation(loc.getNamespace(),
                String.format("%1$s/%2$s/%2$s_%3$02d", ModelProvider.ITEM_FOLDER, loc.getPath(), number));
    }

    ResourceLocation itemLoc(ResourceLocation loc) {
        return new ResourceLocation(loc.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + loc.getPath());
    }
}
