package sciwhiz12.voxeltools.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.PacketDistributor;
import sciwhiz12.voxeltools.net.VxNetwork;

public class ChatUtil {
    // chosen by fair dice roll; guaranteed to be random
    public static final int chatIndex = -1098853;

    public static String serialize(ITextComponent msg) {
        return ITextComponent.Serializer.toJson(msg);
    }

    public static ITextComponent deserialize(String json) {
        return ITextComponent.Serializer.fromJson(json);
    }

    public static void sendIndexedMessage(ITextComponent msg) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(
                    msg, chatIndex
            );
        });
    }

    public static void sendIndexedMessage(PlayerEntity player, ITextComponent msg) {
        DistExecutor.runForDist(() -> () -> {
            ChatUtil.sendIndexedMessage(msg);
            return null;
        }, () -> () -> {
            if (player instanceof ServerPlayerEntity) VxNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg
            );
            return null;
        });
    }
}
