package dev.marston.randomloot.loot;

import java.io.IOException;
import java.util.Set;

import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.LootUtils;
import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;
import dev.marston.randomloot.loot.modifiers.breakers.Explode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LootItem extends Item {
	public static enum ToolType {
		PICKAXE, SHOVEL, AXE, SWORD, NULL
	}

	public LootItem() {
		super(new Properties().stacksTo(1).durability(100));
	}

	public static ToolType getToolType(ItemStack item) {
		CompoundTag toolType = item.getOrCreateTagElement("info");
		String type = toolType.getString("type");
		if (type == "") {
			return ToolType.NULL;
		}
		return ToolType.valueOf(type);
	}

	public static ItemStack setToolType(ItemStack item, ToolType type) {
		CompoundTag toolInfo = item.getOrCreateTagElement("info");
		toolInfo.putString("type", type.toString());
		item.addTagElement("info", toolInfo);
		return item;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState block) {

		ToolType type = getToolType(stack);

		TagKey<Block> blocks = null;

		if (type == ToolType.PICKAXE) {
			blocks = BlockTags.MINEABLE_WITH_PICKAXE;
		}

		return block.is(blocks) ? 3.0f : 1.0F;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {

		ToolType type = getToolType(stack);

		TagKey<Block> blocks = null;

		if (type == ToolType.PICKAXE) {
			blocks = BlockTags.MINEABLE_WITH_PICKAXE;
		}

		return state.is(blocks)
				&& net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(Tiers.DIAMOND, state);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {

		ToolType type = getToolType(itemstack);

		if (type != ToolType.AXE && type != ToolType.PICKAXE && type != ToolType.SHOVEL) {
			return super.onBlockStartBreak(itemstack, pos, player);
		}

		CompoundTag mods = itemstack.getTagElement(Modifier.MODTAG);

		if (mods != null) {
			Set<String> keys = mods.getAllKeys();

			for (String key : keys) {
				Modifier mod = ModifierRegistry.loadModifier(key, mods.getCompound(key));

				if (mod instanceof BlockBreakModifier) {
					BlockBreakModifier bbm = (BlockBreakModifier) mod;

					bbm.startBreak(itemstack, pos, player);
				}

			}
		}

		LootUtils.addXp(itemstack, 1);

		return super.onBlockStartBreak(itemstack, pos, player);
	}

}
