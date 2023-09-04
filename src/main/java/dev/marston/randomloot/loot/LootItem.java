package dev.marston.randomloot.loot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.UseModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
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

	public static float getDigSpeed(ItemStack stack) {
		float speed = (LootUtils.getStats(stack) / 2.0f) + 6.0f;
		return speed;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState block) {

		ToolType type = LootUtils.getToolType(stack);

		TagKey<Block> blocks = null;

		if (type == ToolType.PICKAXE) {
			blocks = BlockTags.MINEABLE_WITH_PICKAXE;
		}

		return block.is(blocks) ? getDigSpeed(stack) : 1.0F;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {

		ToolType type = LootUtils.getToolType(stack);

		TagKey<Block> blocks = null;

		if (type == ToolType.PICKAXE) {
			blocks = BlockTags.MINEABLE_WITH_PICKAXE;
		}

		return state.is(blocks)
				&& net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(Tiers.DIAMOND, state);
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState blockState, BlockPos pos, LivingEntity player) {
		if (!level.isClientSide && blockState.getDestroySpeed(level, pos) != 0.0F) {
			stack.hurtAndBreak(1, player, (p_40992_) -> {
				p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {

		ToolType type = LootUtils.getToolType(itemstack);

		if (type != ToolType.AXE && type != ToolType.PICKAXE && type != ToolType.SHOVEL) {
			return super.onBlockStartBreak(itemstack, pos, player);
		}

		List<Modifier> mods = LootUtils.getModifiers(itemstack);

		for (Modifier mod : mods) {
			if (mod instanceof BlockBreakModifier) {
				BlockBreakModifier bbm = (BlockBreakModifier) mod;

				bbm.startBreak(itemstack, pos, player);
			}
		}

		LootUtils.addXp(itemstack, 1);

		return super.onBlockStartBreak(itemstack, pos, player);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {

		ItemStack itemstack = ctx.getItemInHand();

		List<Modifier> mods = LootUtils.getModifiers(itemstack);

		for (Modifier mod : mods) {

			if (mod instanceof UseModifier) {
				UseModifier um = (UseModifier) mod;

				um.use(ctx);
			}

		}

		return InteractionResult.PASS;
	}

	private MutableComponent makeComp(String text, ChatFormatting color) {
		MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
		comp.append(text);
		comp = comp.withStyle(color);

		return comp;
	}

	@Override
	public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> tipList, TooltipFlag flag) {

		boolean show = Screen.hasShiftDown();

		MutableComponent desc = makeComp(LootUtils.getItemLore(item), ChatFormatting.GRAY);
		tipList.add(desc);

		if (show) {
			int itemLevel = LootUtils.getLevel(item);
			tipList.add(
					makeComp("" + LootUtils.getXP(item) + " / " + LootUtils.getMaxXP(itemLevel) + " | lvl " + itemLevel,
							ChatFormatting.GRAY));
		}

		tipList.add(makeComp("", ChatFormatting.GRAY));

		List<Modifier> mods = LootUtils.getModifiers(item);
		Collections.sort(mods, new Comparator<Modifier>() {
			@Override
			public int compare(final Modifier object1, final Modifier object2) {
				return object1.tagName().compareTo(object2.tagName());
			}
		});

		for (Modifier modifier : mods) {
			modifier.writeToLore(tipList, show);
			if (show) {
				Component details = modifier.writeDetailsToLore();

				if (details != null) {
					MutableComponent detailComp = makeComp(" - ", ChatFormatting.GRAY);
					detailComp.append(details);
					tipList.add(detailComp);
				}
			}
		}

		if (show) {
			tipList.add(makeComp("", ChatFormatting.GRAY));

			float digSpeed = LootItem.getDigSpeed(item);
			tipList.add(makeComp(String.format("Speed: %.2f", digSpeed), ChatFormatting.GRAY));
		}

		tipList.add(makeComp("", ChatFormatting.GRAY));
		MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
		comp.append("[Shift for details]");
		comp = comp.withStyle(ChatFormatting.GRAY);

		tipList.add(comp);

	}

}
