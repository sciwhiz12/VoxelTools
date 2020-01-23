package sciwhiz12.voxeltools.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;
import sciwhiz12.voxeltools.item.IVoxelTool;
import sciwhiz12.voxeltools.net.LeftClickEmptyPacket;
import sciwhiz12.voxeltools.net.VxNetwork;
import sciwhiz12.voxeltools.util.BlockUtil;

@EventBusSubscriber(bus = Bus.FORGE)
public class InteractListener {
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        // Client side event
        if (isValidEvent(event)) {
            VxNetwork.CHANNEL.send(
                    PacketDistributor.SERVER.noArg(), new LeftClickEmptyPacket(event.getHand())
            );
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getSide() == LogicalSide.SERVER && event.getResult() == Result.DEFAULT
                && isValidEvent(event)) {
            IVoxelTool tool = (IVoxelTool) event.getItemStack().getItem();
            Result result = tool.hasLeftClickBlockAction(
                    event.getPlayer(), event.getWorld(), event.getHand(), event.getPos(), event
                            .getFace()
            );
            if (result != Result.DEFAULT) {
                boolean cancel = tool.onLeftClickBlock(
                        event.getPlayer(), event.getWorld(), event.getHand(), event.getPos(), event
                                .getFace()
                );
                event.setCanceled(cancel);
                event.setResult(result);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickItem event) {
        if (event.getSide() == LogicalSide.SERVER && event.getResult() == Result.DEFAULT
                && isValidEvent(event)) {
            RayTraceResult res = BlockUtil.rangedRayTrace(
                    event.getWorld(), event.getPlayer(), FluidMode.NONE, event.getPlayer()
                            .getAttribute(PlayerEntity.REACH_DISTANCE).getValue()
            );
            if (res == null || res.getType() != Type.MISS) { return; }
            IVoxelTool tool = (IVoxelTool) event.getItemStack().getItem();
            Result result = tool.hasRightClickEmptyAction(
                    event.getPlayer(), event.getWorld(), event.getHand()
            );
            if (result != Result.DEFAULT) {
                tool.onRightClickEmpty(event.getPlayer(), event.getWorld(), event.getHand());
                event.setResult(result);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getSide() == LogicalSide.SERVER && event.getResult() == Result.DEFAULT
                && isValidEvent(event)) {
            IVoxelTool tool = (IVoxelTool) event.getItemStack().getItem();
            Result result = tool.hasRightClickBlockAction(
                    event.getPlayer(), event.getWorld(), event.getHand(), event.getPos(), event
                            .getFace()
            );
            if (result != Result.DEFAULT) {
                boolean cancel = tool.onRightClickBlock(
                        event.getPlayer(), event.getWorld(), event.getHand(), event.getPos(), event
                                .getFace()
                );
                if (cancel) {
                    event.setCanceled(cancel);
                    event.setCancellationResult(ActionResultType.SUCCESS);
                }
                event.setResult(result);
            }
        }
    }

    public static boolean isValidEvent(PlayerInteractEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getHeldItem(event.getHand());
        if (stack != null && !stack.isEmpty()) {
            Item item = stack.getItem();
            if (item instanceof IVoxelTool) { return true; }
        }
        return false;
    }
}
