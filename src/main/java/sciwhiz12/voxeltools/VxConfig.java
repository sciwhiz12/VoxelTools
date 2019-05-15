package sciwhiz12.voxeltools;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public final class VxConfig {
	static final ForgeConfigSpec serverSpec;
	public static final Server SERVER;
	static {
		final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		serverSpec = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	public static class Server {
		public final BooleanValue allowItemUse;
		public final EnumValue<DefaultPermissionLevel> defaultPermLevel;
		public final IntValue paintbrushRange;
		public final EnumValue<ClickType> jackhammerNoPhys;
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
					.comment("Enables/disables the functionality of all items.").define("allowItemUse", true);

			defaultPermLevel = builder.translation("voxeltools.config.defaultPermLevel").comment(
					"The default permission level of the permission [voxeltools.item], which grants players access to VoxelTools.")
					.defineEnum("defaultPermLevel", DefaultPermissionLevel.OP, DefaultPermissionLevel.values());

			paintbrushRange = builder.translation("voxeltools.config.paintbrushRange").comment(
					"The range of the Magical Paintbrush when remotely painting a block. Anything below the player's reach distance (default 5) will disable remote painting.")
					.defineInRange("paintbrushRange", 16, 0, 48);

			jackhammerNoPhys = builder.translation("voxeltools.config.jackhammerNoPhys")
					.comment("Whether to use no-physics mode for either mouse button, both, or neither.")
					.defineEnum("jackhammerNoPhys", ClickType.RIGHT_CLICK_ONLY, ClickType.values());

			allowOverwrite = builder.translation("voxeltools.config.allowOverwrite").comment(
					"Whether to allow block-moving items (ie Graviton Sledge) to destroy blocks in their way when the player is sneaking.")
					.define("allowOverwrite", true);

			shovelDigRadiusX = builder.translation("voxeltools.config.shovelDigRadiusX")
					.comment("The radius of the Auto-Shoveler 9001's dig ability on the X axis. Set to 0 to disable.")
					.defineInRange("shovelDigRadiusX", 1, 0, 16);

			shovelDigRadiusY = builder.translation("voxeltools.config.shovelDigRadiusY")
					.comment("The radius of the Auto-Shoveler 9001's dig ability on the Y axis. Set to 0 to disable.")
					.defineInRange("shovelDigRadiusY", 1, 0, 16);

			shovelDigRadiusZ = builder.translation("voxeltools.config.shovelDigRadiusZ")
					.comment("The radius of the Auto-Shoveler 9001's dig ability on the Z axis. Set to 0 to disable.")
					.defineInRange("shovelDigRadiusZ", 1, 0, 16);

			shovelFlattenRadius = builder.translation("voxeltools.config.shovelFlattenRadius")
					.comment("The radius of the Auto-Shoveler 9001's flatten ability. Set to 0 to disable.")
					.defineInRange("shovelFlattenRadius", 2, 0, 16);
			shovelFlattenHeight = builder.translation("voxeltools.config.shovelFlattenHeight")
					.comment("The height of the Auto-Shoveler 9001's flatten ability.",
							"Set to 0 to only flatten blocks on the same horizontal plane.")
					.defineInRange("shovelFlattenHeight", 4, 0, 128);
			shovelFlattenHeightOffset = builder.translation("voxeltools.config.shovelFlattenHeightOffset").comment(
					"The Auto-Shoveler 9001's flatten ability's height offset from the block clicked.",
					"A value of 0 is the plane of the block clicked, and 'n' is the plane 'n' blocks above the block.")
					.defineInRange("shovelFlattenHeightOffset", 1, 0, 8);

			chainsawCutRadius = builder.translation("voxeltools.config.chainsawCutRadius")
					.comment("The radius of the A.S.H. Chainsaw's cutting (trees) ability. Set to 0 to disable.")
					.defineInRange("chainsawCutRadius", 4, 0, 32);
			chainsawCleanRadius = builder.translation("voxeltools.config.chainsawCleanRadius")
					.comment("The radius of the A.S.H. Chainsaw's cleaning (vegetation in general) ability. Set to 0 to disable.")
					.defineInRange("chainsawCleanRadius", 5, 0, 32);
			
			builder.pop();
		}

		public boolean hasPermission(EntityPlayer player) {
			return SERVER.allowItemUse.get() && PermissionAPI.hasPermission(player, ITEM_USE_PERMISSION);
		}
	}

	public static final String ITEM_USE_PERMISSION = "voxeltools.item";

	public static enum ClickType {
		BOTH, RIGHT_CLICK_ONLY, LEFT_CLICK_ONLY, NONE;
	}
}
