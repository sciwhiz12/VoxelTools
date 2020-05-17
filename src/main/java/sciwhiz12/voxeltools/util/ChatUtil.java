package sciwhiz12.voxeltools.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.PacketDistributor;
import sciwhiz12.voxeltools.net.IndexedChatPacket;
import sciwhiz12.voxeltools.net.VxNetwork;

public class ChatUtil {
    // chosen by fair dice roll; guaranteed to be random
    public static final int chatIndex = -1098853;

    @SuppressWarnings("resource")
    public static void sendIndexedMessage(PlayerEntity player, ITextComponent msg) {
        if (player instanceof ServerPlayerEntity) {
            VxNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                new IndexedChatPacket(msg)
            );
        } else {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(
                    msg, chatIndex
                );
            });
        }
    }
}
