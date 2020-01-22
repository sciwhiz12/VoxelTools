package sciwhiz12.voxeltools.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Dooplicator extends Item implements IVoxelTool {
    public Dooplicator(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        if (!world.isRemote) {
            BlockState state = world.getBlockState(pos);
            Item i = state.getBlock().asItem();
            if (i != null) {
                ItemStack stack = new ItemStack(state.getBlock().asItem());
                if (stack != null) {
                    stack.setCount(stack.getMaxStackSize());
                    if (player.inventory.addItemStackToInventory(stack)) {
                        player.sendMessage(
                                new TranslationTextComponent(
                                        "voxeltools.dooplicator.dooped", new StringTextComponent(
                                                String.valueOf(stack.getMaxStackSize())
                                        ).applyTextStyle(TextFormatting.DARK_PURPLE),
                                        new TranslationTextComponent(
                                                state.getBlock().getTranslationKey()
                                        ).applyTextStyle(TextFormatting.GREEN)
                                ).applyTextStyle(TextFormatting.BLUE)
                        );
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Result hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? Result.ALLOW : Result.DEFAULT;
    }
}
