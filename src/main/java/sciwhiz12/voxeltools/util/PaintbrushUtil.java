package sciwhiz12.voxeltools.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PaintbrushUtil {
	public static final String TAG_ID_BLOCKSTATE = "blockstate";
	public static final String TAG_ID_BLOCKNAME = "blockname";

	public static NBTTagCompound storeBlockState(NBTTagCompound tag, IBlockState state) {
		if (tag.hasKey(TAG_ID_BLOCKSTATE))
			tag.removeTag(TAG_ID_BLOCKSTATE);
		tag.setString(TAG_ID_BLOCKNAME, state.getBlock().getRegistryName().toString());
		NBTTagCompound statetag = tag.getCompound(TAG_ID_BLOCKSTATE);
		if (!state.getProperties().isEmpty()) {
			for (IProperty<?> prop : state.getProperties()) {
				if (prop instanceof BooleanProperty) {
					statetag.setBoolean(prop.getName(), (Boolean) state.get(prop));
				} else if (prop instanceof IntegerProperty) {
					statetag.setInt(prop.getName(), (Integer) state.get(prop));
				} else if (prop instanceof DirectionProperty) {
					statetag.setString(prop.getName(), ((EnumFacing) state.get(prop)).getName());
				} else if (prop instanceof EnumProperty<?>) {
					EnumProperty<?> enump = (EnumProperty<?>) prop;
					statetag.setString(prop.getName(), state.get(enump).getName());
				}
			}
			tag.setTag(TAG_ID_BLOCKSTATE, statetag);
		} else {
			tag.removeTag(TAG_ID_BLOCKSTATE);
		}

		return tag;
	}

	public static <T extends Enum<T> & IStringSerializable> IBlockState getBlockState(NBTTagCompound tag) {
		Block b = getBlockFromName(tag.getString(TAG_ID_BLOCKNAME));
		IBlockState state = b.getDefaultState();
		if (tag.hasKey(TAG_ID_BLOCKSTATE)) {
			NBTTagCompound statetag = tag.getCompound(TAG_ID_BLOCKSTATE);
			for (IProperty<?> prop : state.getProperties()) {
				String propName = prop.getName();
				if (statetag.hasKey(propName)) {
					if (prop instanceof BooleanProperty) {
						state = state.with((BooleanProperty) prop, statetag.getBoolean(propName));
					} else if (prop instanceof IntegerProperty) {
						state = state.with((IntegerProperty) prop, statetag.getInt(propName));
					} else if (prop instanceof DirectionProperty) {
						state = state.with((DirectionProperty) prop, EnumFacing.byName(statetag.getString(propName)));
					} else if (prop instanceof EnumProperty<?>) {
						@SuppressWarnings("unchecked")
						EnumProperty<T> enump = (EnumProperty<T>) prop;
						state = state.with(enump, enump.parseValue(statetag.getString(propName)).get());
					}
				}
			}
		}
		return state;
	}

	public static String toStringFromState(IBlockState state) {
		String str = "[";
		boolean first = true;
		for (IProperty<?> prop : state.getProperties()) {
			if (!first) {
				str += ",";
			}
			first = false;
			if (prop instanceof BooleanProperty) {
				str += prop.getName() + "=" + state.get(prop);
			} else if (prop instanceof IntegerProperty) {
				str += prop.getName() + "=" + state.get(prop);
			} else if (prop instanceof EnumProperty<?>) {
				str += prop.getName() + "=" + ((IStringSerializable) state.get(prop)).getName();
			}
		}
		return str + "]";
	}

	public static Block getBlockFromName(String resourceLocation) {
		return GameRegistry.findRegistry(Block.class).getValue(ResourceLocation.makeResourceLocation(resourceLocation));
	}
}
