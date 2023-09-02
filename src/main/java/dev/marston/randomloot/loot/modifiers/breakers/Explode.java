package dev.marston.randomloot.loot.modifiers.breakers;

import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
	
}
