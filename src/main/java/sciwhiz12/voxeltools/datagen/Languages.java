package sciwhiz12.voxeltools.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import sciwhiz12.voxeltools.item.VxItems;

import static sciwhiz12.voxeltools.VoxelTools.MODID;

public class Languages extends LanguageProvider {
    public Languages(DataGenerator gen) {
        super(gen, MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.voxelTools", "VoxelTools");

        addItem(VxItems.test_item, "Testing Item");
        addItem(VxItems.dooplicator, "Dooplication");
        addItem(VxItems.jackhammer, "Pneumatic Jackhammer");
        addItem(VxItems.paintbrush, "Magical Paintbrush");
        addItem(VxItems.sledge, "Graviton Sledge");
        addItem(VxItems.pliers, "Torquemadic Pliers");
        addItem(VxItems.shovel, "Auto-Shoveler 9001");
        addItem(VxItems.chainsaw, "A.S.H. Chainsaw");
        addItem(VxItems.clock, "Tachyonic Chronometer");

        add("error", "disabled", "This item has been disabled by your server administrator.");
        add("error", "no_permission", "You do not have permission to use this item.");

        addTooltip("paintbrush.empty", "Empty palette");
        addTooltip("paintbrush.block.name", "Block: %s");
        addTooltip("paintbrush.block.state", "State: %s");
        addTooltip("paintbrush.sneak", "Sneak to see block state");

        addStatus("dooplicator.dooped", "Dooped %s of %s");
        addStatus("paintbrush.saved", "%s saved into palette");
        addStatus("paintbrush.clear", "Palette cleared");
        addStatus("paintbrush.current", "Current palette: %s");
        addStatus("paintbrush.empty", "Palette is empty");
        addStatus("clock", "Real Time: %s - Stored Time: %s - %s");
        addStatus("clock.active", "ACTIVE");
        addStatus("clock.inactive", "INACTIVE");

        addConfigKeys();
    }

    void addConfigKeys() {
        addConfig("allowItemUse", "Allow Use of VoxelTools", "Enables/disables the functionality of all items.");
        addConfig("defaultPermLevel", "Default Permission Level for VoxelTools",
                "The default permission level of the permission [voxeltools.item], which grants players access to " +
                        "VoxelTools.");

        addConfig("jackhammerNoPhys", "Pneumatic Jackhammer's No-Physics Mode",
                "Whether to use no-physics mode for either mouse button, both, or neither.");
        addConfig("allowOverwrite", "Allow Overwrite of Blocks",
                "Whether to allow block-moving items (ie Graviton Sledge) to destroy blocks in their way when the player " +
                        "is sneaking.");

        addConfig("paintbrushRange", "Magical Paintbrush's Remote Painting Radius",
                "The range of the paintbrush when remotely painting a block. \\nAnything below the player's reach distance" +
                        " (default 5) will disable remote painting.");

        addConfig("shovel.digRadiusX", "Auto-Shoveler 9001's Dig Radius on the X axis",
                "The radius of the shovel's dig ability on the X axis. Set to 0 to disable.");
        addConfig("shovel.digRadiusY", "Auto-Shoveler 9001's Dig Radius on the Y axis",
                "The radius of the shovel's dig ability on the Y axis. Set to 0 to disable.");
        addConfig("shovel.digRadiusZ", "Auto-Shoveler 9001's Dig Radius on the Z axis",
                "The radius of the shovel's dig ability on the Z axis. Set to 0 to disable.");

        addConfig("shovel.flattenRadius", "Auto-Shoveler 9001's Flatten Radius",
                "The radius of the shovel's flatten ability. Set to 0 to disable.");
        addConfig("shovel.flattenHeight", "Auto-Shoveler 9001's Flatten Height",
                "The height of the shovel's flatten ability. Set to 1 to only flatten blocks on the same horizontal plane.");
        addConfig("shovel.flattenHeightOffset", "Auto-Shoveler 9001's Flatten Height Offset",
                "The shovel's flatten ability's height offset from the block clicked.\\n A value of 0 is the plane of the " +
                        "block clicked, and 'n' is the plane 'n' blocks above the block.");

        addConfig("chainsaw.cutRadius", "A.S.H. Chainsaw's Cutting Radius",
                "The radius of the chainsaw's cutting (trees) ability. Set to 0 to disable.");
        addConfig("chainsaw.cleanRadius", "A.S.H. Chainsaw's Cleaning Radius",
                "The radius of the chainsaw's cleaning (vegetation in general) ability. Set to 0 to disable.");
    }

    void addTooltip(final String key, final String value) {
        add("tooltip", key, value);
    }

    void addStatus(final String key, final String value) {
        add("status", key, value);
    }

    void addConfig(final String key, final String main, final String tooltip) {
        add("config", key, main);
        add("config", key + ".tooltip", tooltip);
    }

    /**
     * Convenience method for adding translation keys. Equivalent to
     * {@code add(category + "." + {@link sciwhiz12.voxeltools.VoxelTools#MODID} +
     * "." + subKey, value)}.
     */
    void add(final String category, final String subKey, final String value) {
        add(category + "." + MODID + "." + subKey, value);
    }
}
