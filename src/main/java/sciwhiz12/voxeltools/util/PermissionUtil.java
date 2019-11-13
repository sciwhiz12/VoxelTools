package sciwhiz12.voxeltools.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.permission.PermissionAPI;
import sciwhiz12.voxeltools.VxConfig;

public class PermissionUtil {
	public static boolean hasPermission(PlayerEntity player) {
		return VxConfig.SERVER.allowItemUse.get() && PermissionAPI.hasPermission(player, VxConfig.ITEM_USE_PERMISSION);
	}

	public static boolean checkForPermission(PlayerEntity player) {
		if (!VxConfig.SERVER.allowItemUse.get()) {
			if (player.shouldReceiveErrors() && player.shouldReceiveFeedback()) {
				player.sendMessage(
						new TranslationTextComponent("error.voxeltools.disabled").applyTextStyle(TextFormatting.RED));
			}
			return false;
		} else if (!PermissionAPI.hasPermission(player, VxConfig.ITEM_USE_PERMISSION)) {
			if (player.shouldReceiveErrors() && player.shouldReceiveFeedback()) {
				player.sendMessage(
						new TranslationTextComponent("error.voxeltools.noPermission").applyTextStyle(TextFormatting.RED));
			}
			return false;
		}
		return true;
	}
}
