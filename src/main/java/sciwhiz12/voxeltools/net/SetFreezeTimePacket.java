package sciwhiz12.voxeltools.net;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import sciwhiz12.voxeltools.client.event.TimeInterceptor;

/**
 * Server to client packet ONLY
 */
public class SetFreezeTimePacket {
    private final boolean freeze;
    private final long time;

    public SetFreezeTimePacket(final boolean freeze, final long freezeTime) {
        this.freeze = freeze;
        this.time = freezeTime;
    }

    public static void encode(SetFreezeTimePacket pkt, PacketBuffer buf) {
        buf.writeBoolean(pkt.freeze);
        buf.writeLong(pkt.time);
    }

    public static SetFreezeTimePacket decode(PacketBuffer buf) {
        return new SetFreezeTimePacket(buf.readBoolean(), buf.readLong());
    }

    public static void handlePacket(SetFreezeTimePacket pkt, Supplier<Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            ctx.get().enqueueWork(() -> {
                TimeInterceptor.freezeTime = pkt.freeze;
                TimeInterceptor.frozenTime = pkt.time;
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
