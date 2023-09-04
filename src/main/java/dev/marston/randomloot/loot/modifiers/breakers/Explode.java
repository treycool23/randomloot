package dev.marston.randomloot.loot.modifiers.breakers;

import java.util.List;

import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.LootItem.ToolType;
import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;
import dev.marston.randomloot.loot.modifiers.users.TorchPlace;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;

public class Explode implements BlockBreakModifier{

	private String name;
	private float power;
	private final static String POWER = "power";  
	
	public Explode(String name, float power) {
		this.name = name;
		this.power = power;
	}
	
	public Explode() {
		this.name = "Explosive";
		this.power = 4.0f;
	}
	
	public Modifier clone() {
		return new Explode();
	}
	
	@Override
	public void startBreak(ItemStack itemstack, BlockPos pos, Player player) {
		
		Level l = player.level();
		
		l.explode(player, null, null, pos.getX(), pos.getY(), pos.getZ(), power, false, ExplosionInteraction.BLOCK, false);
	}

	@Override
	public CompoundTag toNBT() {
		
		CompoundTag tag = new CompoundTag();
		
		tag.putFloat(POWER, power);
		
		tag.putString(NAME, name);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Explode(tag.getString(NAME), tag.getFloat(POWER));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "explode";
	}

	@Override
	public String color() {
		return "red";
	}

	@Override
	public String description() {
		return "Upon breaking a block (allowed by tool type), the current block position will explode causing damage to surrounding blocks.";
	}
	
	@Override
	public void writeToLore(List<Component> list, boolean shift) {
		
		MutableComponent comp = Modifier.makeComp(this.name(), this.color());
		
		list.add(comp);
	}
	

	@Override
	public Component writeDetailsToLore() {

		return null;
	}
	
	@Override
	public boolean compatible(Modifier mod) {
		return true;
	}
	
	@Override
	public boolean forTool(ToolType type) {
		return type.equals(ToolType.PICKAXE) || type.equals(ToolType.AXE) || type.equals(ToolType.SHOVEL);
	}
}
