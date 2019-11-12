package sciwhiz12.voxeltools.item;

import static sciwhiz12.voxeltools.util.PaintbrushUtil.TAG_ID_BLOCKNAME;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.TAG_ID_BLOCKSTATE;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.getBlockFromName;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.getBlockState;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.rangedRayTrace;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.storeBlockState;
import static sciwhiz12.voxeltools.util.PaintbrushUtil.toStringFromState;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VxConfig;

public class Paintbrush extends BaseItem {
	public Paintbrush() {
		super(new Properties(), "paintbrush");
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		CompoundNBT tag = stack.getOrCreateTag();
		tooltip.add(new TranslationTextComponent("tooltip.voxeltools.paintbrush.blockname",
				new TranslationTextComponent(getBlockFromName(tag.getString(TAG_ID_BLOCKNAME)).getTranslationKey())
						.applyTextStyle(TextFormatting.GREEN)).applyTextStyle(TextFormatting.GRAY));
		if (tag.contains(TAG_ID_BLOCKSTATE)) {
			if (Screen.hasShiftDown()) {
				tooltip.add(new TranslationTextComponent("tooltip.voxeltools.paintbrush.blockstate",
						new StringTextComponent(toStringFromState(getBlockState(tag)))
								.applyTextStyle(TextFormatting.GREEN)).applyTextStyle(TextFormatting.GRAY));
			} else {
				tooltip.add(new TranslationTextComponent("tooltip.voxeltools.paintbrush.sneak")
						.applyTextStyle(TextFormatting.GRAY));
			}
		}
	}

	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction face) {
		if (!player.world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem() != this)
				stack = player.getHeldItemOffhand();

			CompoundNBT tag = stack.getOrCreateTag();
			BlockState state = player.world.getBlockState(pos);

			stack.setTag(storeBlockState(tag, state));

			((ServerPlayerEntity) player).sendStatusMessage(
					new TranslationTextComponent("status.voxeltools.paintbrush.saved",
							new TranslationTextComponent(
									getBlockFromName(tag.getString(TAG_ID_BLOCKNAME)).getTranslationKey())
											.applyTextStyle(TextFormatting.GREEN)).applyTextStyle(TextFormatting.BLUE),
					true);

			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem() != this)
				stack = player.getHeldItemOffhand();
			if (!player.isSneaking()) {
				double reach = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
				RayTraceResult trace = rangedRayTrace(world, player, RayTraceContext.FluidMode.ANY,
						Math.max(VxConfig.SERVER.paintbrushRange.get(), reach));
				if (trace != null && trace.getType() == Type.BLOCK) {
					BlockState state = getBlockState(stack.getOrCreateTag());
					BlockPos pos = ((BlockRayTraceResult) trace).getPos();
					world.setBlockState(pos, state);
				} else if (trace == null || trace.getType() == Type.MISS) {
					((ServerPlayerEntity) player)
							.sendStatusMessage(new TranslationTextComponent("status.voxeltools.paintbrush.current",
									new TranslationTextComponent(
											getBlockFromName(stack.getOrCreateTag().getString(TAG_ID_BLOCKNAME))
													.getTranslationKey()).applyTextStyle(TextFormatting.GREEN))
															.applyTextStyle(TextFormatting.GRAY),
									true);
				}
			} else {
				CompoundNBT tag = stack.getOrCreateTag();
				stack.setTag(storeBlockState(tag, Blocks.AIR.getDefaultState()));

				((ServerPlayerEntity) player)
						.sendStatusMessage(new TranslationTextComponent("status.voxeltools.paintbrush.clear")
								.applyTextStyle(TextFormatting.BLUE), true);
			}
			return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}
}
