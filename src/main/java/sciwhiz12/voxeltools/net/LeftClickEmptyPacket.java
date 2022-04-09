package sciwhiz12.voxeltools.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import sciwhiz12.voxeltools.item.ILeftClicker;

import java.util.function.Supplier;

public class LeftClickEmptyPacket {
    protected Hand hand;

    public LeftClickEmptyPacket(final Hand hand) {
        this.hand = hand;
    }

    public static void encode(LeftClickEmptyPacket pkt, PacketBuffer buf) {
        buf.writeEnum(pkt.hand);
    }

    public static LeftClickEmptyPacket decode(PacketBuffer buf) {
        return new LeftClickEmptyPacket(buf.readEnum(Hand.class));
    }

    public static void handlePacket(LeftClickEmptyPacket pkt, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            ItemStack stack = player.getItemInHand(pkt.hand);
            if (stack != null && !stack.isEmpty()) {
                Item item = stack.getItem();
                if (item instanceof ILeftClicker.OnEmpty) {
                    ILeftClicker.OnEmpty tool = (ILeftClicker.OnEmpty) item;
                    tool.onLeftClickEmpty(player, player.level, pkt.hand);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
