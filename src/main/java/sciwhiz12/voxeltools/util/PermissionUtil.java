package sciwhiz12.voxeltools.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.permission.PermissionAPI;
import sciwhiz12.voxeltools.VxConfig;

public class PermissionUtil {
    public static boolean hasPermission(PlayerEntity player) {
        return VxConfig.Server.enableItems && PermissionAPI.hasPermission(player, VxConfig.ITEM_USE_PERMISSION);
    }

    public static boolean checkForPermission(PlayerEntity player) {
        if (!VxConfig.Server.enableItems) {
            if (player.isEffectiveAi() && player.acceptsFailure() && player.acceptsSuccess()) {
                player.sendMessage(new TranslationTextComponent("error.voxeltools.disabled").withStyle(TextFormatting.RED),
                        Util.NIL_UUID);
            }
            return false;
        } else if (!PermissionAPI.hasPermission(player, VxConfig.ITEM_USE_PERMISSION)) {
            if (player.isEffectiveAi() && player.acceptsFailure() && player.acceptsSuccess()) {
                player.sendMessage(
                        new TranslationTextComponent("error.voxeltools.no_permission").withStyle(TextFormatting.RED),
                        Util.NIL_UUID);
            }
            return false;
        }
        return true;
    }
}
