package sciwhiz12.voxeltools.net;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import sciwhiz12.voxeltools.item.IScrollListener;

import java.util.function.Supplier;

public class ScrollPacket {
    private final Hand hand;
    private final double scrollDelta;

    public ScrollPacket(final Hand hand, final double scrollDelta) {
        this.hand = hand;
        this.scrollDelta = scrollDelta;
    }

    public static void encode(ScrollPacket pkt, PacketBuffer buf) {
        buf.writeEnum(pkt.hand);
        buf.writeDouble(pkt.scrollDelta);
    }

    public static ScrollPacket decode(PacketBuffer buf) {
        return new ScrollPacket(buf.readEnum(Hand.class), buf.readDouble());
    }

    public static void handlePacket(ScrollPacket pkt, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ItemStack stack = ctx.get().getSender().getItemInHand(pkt.hand);
            if (stack.getItem() instanceof IScrollListener) {
                ((IScrollListener) stack.getItem()).onScroll(stack, ctx.get().getSender(), pkt.scrollDelta);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
