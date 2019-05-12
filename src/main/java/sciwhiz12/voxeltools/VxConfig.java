package sciwhiz12.voxeltools;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

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

		public Server(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings").push("general");

			allowItemUse = builder.translation("voxeltools.config.allowItemUse")
					.comment("Enables/disables the functionality of all items.").define("allowItemUse", true);
			builder.pop();
		}
	}
}
