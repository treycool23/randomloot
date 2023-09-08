package dev.marston.randomloot.loot.modifiers.hurter;

import java.util.List;

import javax.annotation.Nullable;

import dev.marston.randomloot.loot.LootItem;
import dev.marston.randomloot.loot.LootItem.ToolType;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.EntityHurtModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Draining implements EntityHurtModifier{
	private String name;
	private int points;
	private final static String POINTS = "points";

	public Draining(String name, int points) {
		this.name = name;
		this.points = points;
	}

	public Draining() {
		this.name = "Necrotic";
		this.points = 2;
	}

	public Modifier clone() {
		return new Draining();
	}

	
	@Override
	public CompoundTag toNBT() {

		CompoundTag tag = new CompoundTag();

		tag.putInt(POINTS, points);
		tag.putString(NAME, name);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Draining(tag.getString(NAME), tag.getInt(POINTS));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "necrotic";
	}

	@Override
	public String color() {
		return ChatFormatting.RED.getName();
	}

	@Override
	public String description() {
		return "Heals 10% of damage dealt to target.";
	}

	@Override
	public void writeToLore(List<Component> list, boolean shift) {

		MutableComponent comp = Modifier.makeComp(this.name(), this.color());
		list.add(comp);

	}

	@Override
	public Component writeDetailsToLore(@Nullable Level level) {

		return null;
	}

	@Override
	public boolean compatible(Modifier mod) {
		return true;
	}

	@Override
	public boolean forTool(ToolType type) {
		return type.equals(ToolType.SWORD) || type.equals(ToolType.AXE);
	}

	@Override
	public void hurtEnemy(ItemStack itemstack, LivingEntity hurtee, LivingEntity hurter) {
		float damage = LootItem.getAttackDamage(itemstack, LootUtils.getToolType(itemstack));
		
		hurter.heal(damage * 0.10f);
		
	}
}
