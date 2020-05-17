package sciwhiz12.voxeltools;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.server.permission.DefaultPermissionLevel;

@EventBusSubscriber(modid = VoxelTools.MODID, bus = Bus.MOD)
public final class VxConfig {
    public static final Logger LOGGER = LogManager.getLogger();

    static final ForgeConfigSpec serverSpec;
    public static final Server SERVER_CONFIG;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(
            Server::new
        );
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

            allowItemUse = builder.translation("voxeltools.config.allowItemUse").comment(
                "Enables/disables the functionality of all items."
            ).define("allowItemUse", true);

            defaultPermLevel = builder.translation("voxeltools.config.defaultPermLevel").comment(
                "The default permission level of the permission [voxeltools.item], which grants players access to VoxelTools."
            ).defineEnum(
                "defaultPermLevel", DefaultPermissionLevel.OP, DefaultPermissionLevel.values()
            );

            paintbrushRange = builder.translation("voxeltools.config.paintbrushRange").comment(
                "The range of the Magical Paintbrush when remotely painting a block. Anything below the player's reach distance (default 5) will disable remote painting."
            ).defineInRange("paintbrushRange", 16, 0, 48);

            allowOverwrite = builder.translation("voxeltools.config.allowOverwrite").comment(
                "Whether to allow block-moving items (ie Graviton Sledge) to destroy blocks in their way when the player is sneaking."
            ).define("allowOverwrite", true);

            builder.push("shovel");
            shovelDigRadiusX = builder.translation("voxeltools.config.shovelDigRadiusX").comment(
                "The radius of the Auto-Shoveler 9001's dig ability on the X axis. Set to 0 to disable."
            ).defineInRange("shovelDigRadiusX", 1, 0, 16);

            shovelDigRadiusY = builder.translation("voxeltools.config.shovelDigRadiusY").comment(
                "The radius of the Auto-Shoveler 9001's dig ability on the Y axis. Set to 0 to disable."
            ).defineInRange("shovelDigRadiusY", 1, 0, 16);

            shovelDigRadiusZ = builder.translation("voxeltools.config.shovelDigRadiusZ").comment(
                "The radius of the Auto-Shoveler 9001's dig ability on the Z axis. Set to 0 to disable."
            ).defineInRange("shovelDigRadiusZ", 1, 0, 16);

            shovelFlattenRadius = builder.translation("voxeltools.config.shovelFlattenRadius")
                .comment(
                    "The radius of the Auto-Shoveler 9001's flatten ability. Set to 0 to disable."
                ).defineInRange("shovelFlattenRadius", 2, 0, 16);
            shovelFlattenHeight = builder.translation("voxeltools.config.shovelFlattenHeight")
                .comment(
                    "The height of the Auto-Shoveler 9001's flatten ability.",
                    "Set to 0 to only flatten blocks on the same horizontal plane."
                ).defineInRange("shovelFlattenHeight", 4, 0, 128);
            shovelFlattenHeightOffset = builder.translation(
                "voxeltools.config.shovelFlattenHeightOffset"
            ).comment(
                "The Auto-Shoveler 9001's flatten ability's height offset from the block clicked.",
                "A value of 0 is the plane of the block clicked, and 'n' is the plane 'n' blocks above the block."
            ).defineInRange("shovelFlattenHeightOffset", 1, 0, 8);
            builder.pop();

            builder.push("chainsaw");
            chainsawCutRadius = builder.translation("voxeltools.config.chainsawCutRadius").comment(
                "The radius of the A.S.H. Chainsaw's cutting (trees) ability. Set to 0 to disable."
            ).defineInRange("chainsawCutRadius", 4, 0, 32);
            chainsawCleanRadius = builder.translation("voxeltools.config.chainsawCleanRadius")
                .comment(
                    "The radius of the A.S.H. Chainsaw's cleaning (vegetation in general) ability. Set to 0 to disable."
                ).defineInRange("chainsawCleanRadius", 5, 0, 32);
            builder.pop();

            builder.pop();
        }
    }

    public static class ServerConfig {
        public static boolean allowItemUse;
        public static DefaultPermissionLevel defaultPermLevel;
        public static int paintbrushRange;
        public static boolean allowOverwrite;

        public static int shovelDigRadiusX;
        public static int shovelDigRadiusY;
        public static int shovelDigRadiusZ;

        public static int shovelFlattenRadius;
        public static int shovelFlattenHeight;
        public static int shovelFlattenHeightOffset;

        public static int chainsawCutRadius;
        public static int chainsawCleanRadius;

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
