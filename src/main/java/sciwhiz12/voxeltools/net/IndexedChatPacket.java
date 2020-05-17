package sciwhiz12.voxeltools.net;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import sciwhiz12.voxeltools.util.ChatUtil;

public class IndexedChatPacket {
    private final ITextComponent text;

    public IndexedChatPacket(final ITextComponent text) {
        this.text = text;
    }

    public static void encode(IndexedChatPacket pkt, PacketBuffer buf) {
        buf.writeTextComponent(pkt.text);
    }

    public static IndexedChatPacket decode(PacketBuffer buf) {
        return new IndexedChatPacket(buf.readTextComponent());
    }

    public static void handlePacket(IndexedChatPacket pkt, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> ChatUtil.sendIndexedMessage(ctx.get().getSender(), pkt.text));
        ctx.get().setPacketHandled(true);
    }
}
