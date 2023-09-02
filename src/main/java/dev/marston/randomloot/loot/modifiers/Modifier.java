package dev.marston.randomloot.loot.modifiers;

import net.minecraft.nbt.CompoundTag;



public interface Modifier {
	public static final String MODTAG = "modifiers";

	static final String NAME = "name";  
	public String tagName();
	public String name();
	public String color();
	public CompoundTag toNBT();
	public Modifier fromNBT(CompoundTag tag);
}

