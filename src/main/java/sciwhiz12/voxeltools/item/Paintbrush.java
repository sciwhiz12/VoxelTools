package sciwhiz12.voxeltools.item;

import static sciwhiz12.voxeltools.util.PaintbrushUtil.TAG_ID_BLOCKNAME;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.TAG_ID_BLOCKSTATE;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.getBlockFromName;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.getBlockState;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.storeBlockState;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.toStringFromState;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VxConfig;

public class Paintbrush extends BaseItem {
	public Paintbrush() {
		super(new Properties(), "paintbrush");
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		NBTTagCompound tag = stack.getOrCreateTag();
		tooltip.add(new TextComponentTranslation("tooltip.voxeltools.paintbrush.blockname",
				getBlockFromName(tag.getString(TAG_ID_BLOCKNAME)).getNameTextComponent()
						.applyTextStyle(TextFormatting.GREEN)).applyTextStyle(TextFormatting.GRAY));
		if (tag.hasKey(TAG_ID_BLOCKSTATE)) {
			if (GuiScreen.isShiftKeyDown()) {
				tooltip.add(new TextComponentTranslation("tooltip.voxeltools.paintbrush.blockstate",
						new TextComponentString(toStringFromState(getBlockState(tag)))
								.applyTextStyle(TextFormatting.GREEN)).applyTextStyle(TextFormatting.GRAY));
			} else {
				tooltip.add(new TextComponentTranslation("tooltip.voxeltools.paintbrush.sneak")
						.applyTextStyle(TextFormatting.GRAY));
			}
		}
	}

	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (!player.world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem() != this)
				stack = player.getHeldItemOffhand();

			NBTTagCompound tag = stack.getOrCreateTag();
			IBlockState state = player.world.getBlockState(pos);

			stack.setTag(storeBlockState(tag, state));

			/*player.sendStatusMessage(new TextComponentTranslation("status.voxeltools.paintbrush.saved",
					getBlockFromName(tag.getString(TAG_ID_BLOCKNAME)).getNameTextComponent()
							.applyTextStyle(TextFormatting.GREEN)).applyTextStyle(TextFormatting.BLUE),
					true);*/

			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem() != this)
				stack = player.getHeldItemOffhand();
			if (!player.isSneaking()) {
				double reach = player.getAttribute(EntityPlayer.REACH_DISTANCE).getValue();
				RayTraceResult trace = ForgeHooks.rayTraceEyes(player,
						Math.max(VxConfig.SERVER.paintbrushRange.get(), reach));
				if (trace != null && trace.type == Type.BLOCK) {
					IBlockState state = getBlockState(stack.getOrCreateTag());
					BlockPos pos = trace.getBlockPos();
					world.setBlockState(pos, state);
				} else if (trace == null || trace.type == Type.MISS) {
					/*player.sendStatusMessage(
							new TextComponentTranslation("status.voxeltools.paintbrush.current",
									getBlockFromName(stack.getOrCreateTag().getString(TAG_ID_BLOCKNAME))
											.getNameTextComponent().applyTextStyle(TextFormatting.GREEN))
													.applyTextStyle(TextFormatting.GRAY),
							true);*/
				}
			} else {
				NBTTagCompound tag = stack.getOrCreateTag();
				stack.setTag(storeBlockState(tag, Blocks.AIR.getDefaultState()));

				/*player.sendStatusMessage(new TextComponentTranslation("status.voxeltools.paintbrush.clear")
						.applyTextStyle(TextFormatting.BLUE), true);*/
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
}
