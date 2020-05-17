package sciwhiz12.voxeltools.net;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sciwhiz12.voxeltools.VoxelTools;

public class VxNetwork {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(VoxelTools.MODID, "channel"), () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    private static int id = 1;

    public static void registerPackets() {
        CHANNEL.registerMessage(
            id++, LeftClickEmptyPacket.class, LeftClickEmptyPacket::encode,
            LeftClickEmptyPacket::decode, LeftClickEmptyPacket::handlePacket
        );
        CHANNEL.registerMessage(
            id++, IndexedChatPacket.class, IndexedChatPacket::encode, IndexedChatPacket::decode,
            IndexedChatPacket::handlePacket
        );
    }
}
