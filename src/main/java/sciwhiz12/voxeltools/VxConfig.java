package sciwhiz12.voxeltools;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = VoxelTools.MODID, bus = Bus.MOD)
public final class VxConfig {
    public static final Logger LOGGER = LogManager.getLogger();

    static final ForgeConfigSpec serverSpec;
    public static final ServerSpec SERVER_CONFIG;

    static {
        final Pair<ServerSpec, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerSpec::new);
        serverSpec = specPair.getRight();
        SERVER_CONFIG = specPair.getLeft();
    }

    public static final String ITEM_USE_PERMISSION = "voxeltools.item";

    public static class ServerSpec {
        public final BooleanValue enableItems;
        public final EnumValue<DefaultPermissionLevel> defaultPermLevel;
        public final BooleanValue allowOverwrite;

        public final IntValue paintbrushRange;

        public final IntValue shovelDigRadiusX;
        public final IntValue shovelDigRadiusY;
        public final IntValue shovelDigRadiusZ;

        public final IntValue shovelFlattenRadius;
        public final IntValue shovelFlattenHeight;
        public final IntValue shovelFlattenHeightOffset;

        public final IntValue chainsawCutRadius;
        public final IntValue chainsawCleanRadius;

        public ServerSpec(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings").push("general");

            enableItems = builder.translation("config.voxeltools.enableItems")
                    .comment("Enables/disables the functionality of all items.").define("enableItems", Server.enableItems);
            defaultPermLevel = builder.translation("config.voxeltools.defaultPermLevel").comment(
                    "The default permission level of the permission [voxeltools.item], which grants players access to " +
                            "VoxelTools.")
                    .defineEnum("defaultPermLevel", Server.defaultPermLevel, DefaultPermissionLevel.values());
            allowOverwrite = builder.translation("config.voxeltools.allowOverwrite").comment(
                    "Whether to allow block-moving items (ie Graviton Sledge) to destroy blocks in their way when the " +
                            "player is sneaking.")
                    .define("allowOverwrite", Server.allowOverwrite);
            builder.pop();

            builder.comment("Item settings").push("items");

            builder.comment("Settings for paintbrush item (Magical Paintbrush)").push("paintbrush");
            paintbrushRange = builder.translation("config.voxeltools.paintbrush.range")
                    .comment("The range of the paintbrush when remotely painting a block.",
                            "Anything below the player's reach distance (default 5) will disable remote painting.")
                    .defineInRange("range", Server.paintbrushRange, 0, 48);
            builder.pop();

            builder.comment("Settings for shovel item (Auto-Shoveler 9001)").push("shovel");
            shovelDigRadiusX = builder.translation("config.voxeltools.shovel.digRadiusX")
                    .comment("The radius of the shovel's dig ability on the X axis. Set to 0 to disable.")
                    .defineInRange("digRadiusX", Server.shovelDigRadiusX, 0, 16);
            shovelDigRadiusY = builder.translation("config.voxeltools.shovel.digRadiusY")
                    .comment("The radius of the shovel's dig ability on the Y axis. Set to 0 to disable.")
                    .defineInRange("digRadiusY", Server.shovelDigRadiusY, 0, 16);
            shovelDigRadiusZ = builder.translation("config.voxeltools.shovel.digRadiusZ")
                    .comment("The radius of the shovel's dig ability on the Z axis. Set to 0 to disable.")
                    .defineInRange("digRadiusZ", Server.shovelDigRadiusZ, 0, 16);

            shovelFlattenRadius = builder.translation("config.voxeltools.shovel.flattenRadius")
                    .comment("The radius of the shovel's flatten ability. Set to 0 to disable.")
                    .defineInRange("flattenRadius", Server.shovelFlattenRadius, 0, 16);
            shovelFlattenHeight = builder.translation("config.voxeltools.shovel.flattenHeight")
                    .comment("The height of the shovel's flatten ability.",
                            "Set to 0 to only flatten blocks on the same horizontal plane.")
                    .defineInRange("flattenHeight", Server.shovelFlattenHeight, 0, 128);
            shovelFlattenHeightOffset = builder.translation("config.voxeltools.shovel.flattenHeightOffset")
                    .comment("The shovel's flatten ability's height offset from the block clicked.",
                            "A value of 0 is the plane of the block clicked, and 'n' is the plane 'n' blocks above the " + "block.")
                    .defineInRange("flattenHeightOffset", Server.shovelFlattenHeightOffset, 0, 8);
            builder.pop();

            builder.comment("Settings for chainsaw (A.S.H. Chainsaw)").push("chainsaw");
            chainsawCutRadius = builder.translation("config.voxeltools.chainsaw.cutRadius")
                    .comment("The radius of the chainsaw's cutting (trees) ability. Set to 0 to disable.")
                    .defineInRange("cutRadius", Server.chainsawCutRadius, 0, 32);
            chainsawCleanRadius = builder.translation("config.voxeltools.chainsaw.cleanRadius")
                    .comment("The radius of the chainsaw's cleaning (vegetation in general) ability. Set to 0 to disable.")
                    .defineInRange("cleanRadius", Server.chainsawCleanRadius, 0, 32);
            builder.pop();

            builder.pop();
        }
    }

    public static class Server {
        public static boolean enableItems = true;
        public static DefaultPermissionLevel defaultPermLevel = DefaultPermissionLevel.OP;
        public static boolean allowOverwrite = true;

        public static int paintbrushRange = 16;

        public static int shovelDigRadiusX = 1;
        public static int shovelDigRadiusY = 1;
        public static int shovelDigRadiusZ = 1;

        public static int shovelFlattenRadius = 2;
        public static int shovelFlattenHeight = 4;
        public static int shovelFlattenHeightOffset = 1;

        public static int chainsawCutRadius = 4;
        public static int chainsawCleanRadius = 5;

        public static void bakeConfig(ModConfig config) {
            enableItems = VxConfig.SERVER_CONFIG.enableItems.get();
            defaultPermLevel = VxConfig.SERVER_CONFIG.defaultPermLevel.get();
            allowOverwrite = VxConfig.SERVER_CONFIG.allowOverwrite.get();

            paintbrushRange = VxConfig.SERVER_CONFIG.paintbrushRange.get();

            shovelDigRadiusX = VxConfig.SERVER_CONFIG.shovelDigRadiusX.get();
            shovelDigRadiusY = VxConfig.SERVER_CONFIG.shovelDigRadiusY.get();
            shovelDigRadiusZ = VxConfig.SERVER_CONFIG.shovelDigRadiusZ.get();

            shovelFlattenRadius = VxConfig.SERVER_CONFIG.shovelFlattenRadius.get();
            shovelFlattenHeight = VxConfig.SERVER_CONFIG.shovelFlattenHeight.get();
            shovelFlattenHeightOffset = VxConfig.SERVER_CONFIG.shovelFlattenHeightOffset.get();

            chainsawCutRadius = VxConfig.SERVER_CONFIG.chainsawCutRadius.get();
            chainsawCleanRadius = VxConfig.SERVER_CONFIG.chainsawCleanRadius.get();

            LOGGER.debug("Baked server config");
        }
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        if (config.getSpec() == VxConfig.serverSpec) { Server.bakeConfig(config); }
    }
}
