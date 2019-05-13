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
