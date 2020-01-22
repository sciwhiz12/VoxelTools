package sciwhiz12.voxeltools.net;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import sciwhiz12.voxeltools.item.IVoxelTool;

public class LeftClickEmptyPacket {
    protected Hand hand;

    public LeftClickEmptyPacket(final Hand hand) {
        this.hand = hand;
    }

    public static void encode(LeftClickEmptyPacket pkt, PacketBuffer buf) {
        buf.writeString(pkt.hand.name());
    }

    public static LeftClickEmptyPacket decode(PacketBuffer buf) {
        Hand hand = Hand.valueOf(buf.readString());
        return new LeftClickEmptyPacket(hand);
    }

    public static void handlePacket(LeftClickEmptyPacket pkt, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            ItemStack stack = player.getHeldItem(pkt.hand);
            if (stack != null && !stack.isEmpty()) {
                Item item = stack.getItem();
                if (item instanceof IVoxelTool) {
                    IVoxelTool tool = (IVoxelTool) item;
                    tool.onLeftClickEmpty(player, player.world, pkt.hand);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
