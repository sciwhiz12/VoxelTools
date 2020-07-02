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
    public static final Server SERVER_CONFIG;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER_CONFIG = specPair.getLeft();
    }

    public static final String ITEM_USE_PERMISSION = "voxeltools.item";

    public static class Server {
        public final BooleanValue allowItemUse;
        public final EnumValue<DefaultPermissionLevel> defaultPermLevel;
        public final IntValue paintbrushRange;
        public final BooleanValue allowOverwrite;

        public final IntValue shovelDigRadiusX;
        public final IntValue shovelDigRadiusY;
        public final IntValue shovelDigRadiusZ;

        public final IntValue shovelFlattenRadius;
        public final IntValue shovelFlattenHeight;
        public final IntValue shovelFlattenHeightOffset;

        public final IntValue chainsawCutRadius;
        public final IntValue chainsawCleanRadius;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings").push("general");

            allowItemUse = builder.translation("voxeltools.config.allowItemUse")
                    .comment("Enables/disables the functionality of all items.")
                    .define("allowItemUse", ServerConfig.allowItemUse);

            defaultPermLevel = builder.translation("voxeltools.config.defaultPermLevel").comment(
                    "The default permission level of the permission [voxeltools.item], which grants players access to " +
                            "VoxelTools.")
                    .defineEnum("defaultPermLevel", ServerConfig.defaultPermLevel, DefaultPermissionLevel.values());

            paintbrushRange = builder.translation("voxeltools.config.paintbrushRange").comment(
                    "The range of the Magical Paintbrush when remotely painting a block. Anything below the player's " +
                            "reach" + " distance (default 5) will disable remote painting.")
                    .defineInRange("paintbrushRange", ServerConfig.paintbrushRange, 0, 48);

            allowOverwrite = builder.translation("voxeltools.config.allowOverwrite").comment(
                    "Whether to allow block-moving items (ie Graviton Sledge) to destroy blocks in their way when the " +
                            "player is sneaking.")
                    .define("allowOverwrite", ServerConfig.allowOverwrite);

            builder.push("shovel");
            shovelDigRadiusX = builder.translation("voxeltools.config.shovelDigRadiusX")
                    .comment("The radius of the Auto-Shoveler 9001's dig ability on the X axis. Set to 0 to disable.")
                    .defineInRange("shovelDigRadiusX", ServerConfig.shovelDigRadiusX, 0, 16);
            shovelDigRadiusY = builder.translation("voxeltools.config.shovelDigRadiusY")
                    .comment("The radius of the Auto-Shoveler 9001's dig ability on the Y axis. Set to 0 to disable.")
                    .defineInRange("shovelDigRadiusY", ServerConfig.shovelDigRadiusY, 0, 16);
            shovelDigRadiusZ = builder.translation("voxeltools.config.shovelDigRadiusZ")
                    .comment("The radius of the Auto-Shoveler 9001's dig ability on the Z axis. Set to 0 to disable.")
                    .defineInRange("shovelDigRadiusZ", ServerConfig.shovelDigRadiusZ, 0, 16);

            shovelFlattenRadius = builder.translation("voxeltools.config.shovelFlattenRadius")
                    .comment("The radius of the Auto-Shoveler 9001's flatten ability. Set to 0 to disable.")
                    .defineInRange("shovelFlattenRadius", ServerConfig.shovelFlattenRadius, 0, 16);
            shovelFlattenHeight = builder.translation("voxeltools.config.shovelFlattenHeight")
                    .comment("The height of the Auto-Shoveler 9001's flatten ability.",
                            "Set to 0 to only flatten blocks on the same horizontal plane.")
                    .defineInRange("shovelFlattenHeight", ServerConfig.shovelFlattenHeight, 0, 128);
            shovelFlattenHeightOffset = builder.translation("voxeltools.config.shovelFlattenHeightOffset")
                    .comment("The Auto-Shoveler 9001's flatten ability's height offset from the block clicked.",
                            "A value of 0 is the plane of the block clicked, and 'n' is the plane 'n' blocks above the " + "block.")
                    .defineInRange("shovelFlattenHeightOffset", ServerConfig.shovelFlattenHeightOffset, 0, 8);
            builder.pop();

            builder.push("chainsaw");
            chainsawCutRadius = builder.translation("voxeltools.config.chainsawCutRadius")
                    .comment("The radius of the A.S.H. Chainsaw's cutting (trees) ability. Set to 0 to disable.")
                    .defineInRange("chainsawCutRadius", ServerConfig.chainsawCutRadius, 0, 32);
            chainsawCleanRadius = builder.translation("voxeltools.config.chainsawCleanRadius").comment(
                    "The radius of the A.S.H. Chainsaw's cleaning (vegetation in general) ability. Set to 0 to disable.")
                    .defineInRange("chainsawCleanRadius", ServerConfig.chainsawCleanRadius, 0, 32);
            builder.pop();

            builder.pop();
        }
    }

    public static class ServerConfig {
        public static boolean allowItemUse = true;
        public static DefaultPermissionLevel defaultPermLevel = DefaultPermissionLevel.OP;
        public static int paintbrushRange = 16;
        public static boolean allowOverwrite = true;

        public static int shovelDigRadiusX = 1;
        public static int shovelDigRadiusY = 1;
        public static int shovelDigRadiusZ = 1;

        public static int shovelFlattenRadius = 2;
        public static int shovelFlattenHeight = 4;
        public static int shovelFlattenHeightOffset = 1;

        public static int chainsawCutRadius = 4;
        public static int chainsawCleanRadius = 5;

        public static void bakeConfig(ModConfig config) {
            allowItemUse = VxConfig.SERVER_CONFIG.allowItemUse.get();
            defaultPermLevel = VxConfig.SERVER_CONFIG.defaultPermLevel.get();
            paintbrushRange = VxConfig.SERVER_CONFIG.paintbrushRange.get();
            allowOverwrite = VxConfig.SERVER_CONFIG.allowOverwrite.get();

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
        if (config.getSpec() == VxConfig.serverSpec) { ServerConfig.bakeConfig(config); }
    }
}
