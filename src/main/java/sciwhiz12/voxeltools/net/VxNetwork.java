package sciwhiz12.voxeltools.net;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sciwhiz12.voxeltools.VoxelTools;

public class VxNetwork {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry
            .newSimpleChannel(new ResourceLocation(VoxelTools.MODID, "channel"), () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private static int id = 1;

    public static void registerPackets() {
        CHANNEL.messageBuilder(LeftClickEmptyPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(LeftClickEmptyPacket::encode)
                .decoder(LeftClickEmptyPacket::decode)
                .consumer(LeftClickEmptyPacket::handlePacket)
                .add();
        CHANNEL.messageBuilder(ScrollPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ScrollPacket::encode)
                .decoder(ScrollPacket::decode)
                .consumer(ScrollPacket::handlePacket)
                .add();
        CHANNEL.messageBuilder(SetFreezeTimePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SetFreezeTimePacket::encode)
                .decoder(SetFreezeTimePacket::decode)
                .consumer(SetFreezeTimePacket::handlePacket)
                .add();
    }
}
