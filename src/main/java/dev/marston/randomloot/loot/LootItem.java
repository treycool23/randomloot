package dev.marston.randomloot.loot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import dev.marston.randomloot.RandomLootMod;
import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.EntityHurtModifier;
import dev.marston.randomloot.loot.modifiers.HoldModifier;
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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
		PICKAXE, SHOVEL, AXE, SWORD, NULL;

		@Override
		public String toString() {
			switch (this) {
			case PICKAXE:
				return "Pickaxe";
			case SHOVEL:
				return "Shovel";
			case AXE:
				return "Axe";
			case SWORD:
				return "Sword";
			default:
				return "Null";
			}
		}
	}

	public LootItem() {
		super(new Properties().stacksTo(1).durability(100));
	}

	public static float getDigSpeed(ItemStack stack, ToolType type) {

		if (type.equals(ToolType.SWORD)) {
			return 1.0f;
		}

		float speed = (LootUtils.getStats(stack) / 2.0f) + 6.0f;
		return speed;
	}

	public static float getAttackSpeed(ItemStack stack, ToolType type) {

		float speed = 0.0f;

		switch (type) {
		case PICKAXE:
			speed = -2.8F;
			break;
		case AXE:
			speed = -3.0F;
			break;
		case SHOVEL:
			speed = -3.0F;
			break;
		case SWORD:
			speed = -2.4F;
			break;
		}

		return speed;
	}

	public static float getAttackDamage(ItemStack stack, ToolType type) {

		float damage = (LootUtils.getStats(stack)) + 1.0f;

		switch (type) {
		case PICKAXE:
			damage = damage * 0.5f;
			break;
		case AXE:
			damage = damage * 1.2f;
			break;
		case SHOVEL:
			damage = damage * 0.6f;
			break;
		}

		return damage;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState block) {

		ToolType type = LootUtils.getToolType(stack);

		TagKey<Block> blocks = null;

		if (type == ToolType.PICKAXE) {
			blocks = BlockTags.MINEABLE_WITH_PICKAXE;
		} else if (type == ToolType.AXE) {
			blocks = BlockTags.MINEABLE_WITH_AXE;
		} else if (type == ToolType.SHOVEL) {
			blocks = BlockTags.MINEABLE_WITH_SHOVEL;
		} else {
			return 1.0f;
		}

		return block.is(blocks) ? getDigSpeed(stack, type) : 1.0F;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {

		ToolType type = LootUtils.getToolType(stack);

		TagKey<Block> blocks = null;

		if (type == ToolType.PICKAXE) {
			blocks = BlockTags.MINEABLE_WITH_PICKAXE;
		} else if (type == ToolType.SHOVEL) {
			blocks = BlockTags.MINEABLE_WITH_SHOVEL;
		} else if (type == ToolType.AXE) {
			blocks = BlockTags.MINEABLE_WITH_AXE;
		} else {
			return false;
		}

		return state.is(blocks)
				&& net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(Tiers.DIAMOND, state);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

		if (!slot.equals(EquipmentSlot.MAINHAND)) {
			return super.getAttributeModifiers(slot, stack);
		}

		ToolType tt = LootUtils.getToolType(stack);

		float damage = getAttackDamage(stack, tt);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
				(double) damage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier",
				(double) getAttackSpeed(stack, tt), AttributeModifier.Operation.ADDITION));

		return builder.build();
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity p_43279_, LivingEntity p_43280_) {

		ToolType type = LootUtils.getToolType(itemstack);

		if (type == ToolType.AXE || type == ToolType.SWORD) {
			LootUtils.addXp(itemstack, 1);

		}

		List<Modifier> mods = LootUtils.getModifiers(itemstack);

		for (Modifier mod : mods) {
			if (mod instanceof EntityHurtModifier) {
				EntityHurtModifier ehm = (EntityHurtModifier) mod;

				ehm.hurtEnemy(itemstack, p_43279_, p_43280_);
			}
		}

		itemstack.hurtAndBreak(1, p_43280_, (p_43296_) -> {
			p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState blockState, BlockPos pos, LivingEntity player) {
		if (!level.isClientSide && blockState.getDestroySpeed(level, pos) != 0.0F) {
			RandomLootMod.LOGGER.info("breaking block!");

			List<Modifier> mods = LootUtils.getModifiers(stack);

			for (Modifier mod : mods) {
				RandomLootMod.LOGGER.info("testing for " + mod.name());

				if (mod instanceof BlockBreakModifier) {
					BlockBreakModifier bbm = (BlockBreakModifier) mod;
					RandomLootMod.LOGGER.info("running " + bbm.name());

					bbm.startBreak(stack, pos, player);
				}
			}
			
			stack.hurtAndBreak(1, player, (p_40992_) -> {
				p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
			
			LootUtils.addXp(stack, 1);

		}

		return true;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {

		

		return super.onBlockStartBreak(itemstack, pos, player);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		float stats = (LootUtils.getStats(stack) + 10.0f) * 80.0f;

		return (int) stats;
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

	private void newLine(List<Component> tipList) {
		tipList.add(makeComp("", ChatFormatting.GRAY));
	}

	@Override
	public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> tipList, TooltipFlag flag) {

		boolean show = Screen.hasShiftDown();

		boolean showDescription = Screen.hasControlDown();

		ToolType tt = LootUtils.getToolType(item);

		if (show) {
			tipList.add(makeComp(tt.toString(), ChatFormatting.BLUE));
		}

		MutableComponent desc = makeComp(LootUtils.getItemLore(item), ChatFormatting.GRAY);
		tipList.add(desc);

		if (show) {
			newLine(tipList);
			int itemLevel = LootUtils.getLevel(item);
			tipList.add(makeComp("Level: " + itemLevel, ChatFormatting.GRAY));
			tipList.add(makeComp("XP: " + LootUtils.getXP(item) + " / " + LootUtils.getMaxXP(itemLevel),
					ChatFormatting.GRAY));
		}

		newLine(tipList);

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
				Component details = modifier.writeDetailsToLore(level);

				if (details != null) {
					MutableComponent detailComp = makeComp(" - ", ChatFormatting.GRAY);
					detailComp.append(details);
					tipList.add(detailComp);
				}
			}
			if (showDescription) {
				MutableComponent detailComp = makeComp("", ChatFormatting.GRAY);
				detailComp.append(modifier.description());
				tipList.add(detailComp);
			}
		}

		if (show) {
			newLine(tipList);

			float digSpeed = LootItem.getDigSpeed(item, tt);
			tipList.add(makeComp(String.format("Speed: %.2f", digSpeed), ChatFormatting.GRAY));

			float attackDamage = LootItem.getAttackDamage(item, tt);
			tipList.add(makeComp(String.format("Damage: %.2f", attackDamage), ChatFormatting.GRAY));

		}

		if (!show && !showDescription) {
			newLine(tipList);
			MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
			comp.append("[Shift for more]");
			comp = comp.withStyle(ChatFormatting.GRAY);
			tipList.add(comp);
			MutableComponent descComp = MutableComponent.create(ComponentContents.EMPTY);
			descComp.append("[Ctrl for trait info]");
			descComp = descComp.withStyle(ChatFormatting.GRAY);
			tipList.add(descComp);

		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity holder, int slot, boolean holding) {

		if (holding) {

			List<Modifier> mods = LootUtils.getModifiers(stack);

			for (Modifier mod : mods) {

				if (mod instanceof HoldModifier) {
					HoldModifier hodlMod = (HoldModifier) mod;

					hodlMod.hold(stack, level, holder);
				}

			}
		}

	}

}
