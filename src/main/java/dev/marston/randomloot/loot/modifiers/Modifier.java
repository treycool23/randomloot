package dev.marston.randomloot.loot.modifiers;

import java.util.List;

import dev.marston.randomloot.loot.LootItem.ToolType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;



public interface Modifier {
	
	public static MutableComponent makeComp(String text, ChatFormatting color) {
		MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
		comp.append(text);
		comp = comp.withStyle(color);
		
		return comp;
	}
	public static MutableComponent makeComp(String text, String color) {
		MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
		comp.append(text);
		comp = comp.withStyle(ChatFormatting.getByName(color));
		
		return comp;
	}
	
	public static MutableComponent makeComp(Component compIn) {
		MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
		comp.append(compIn);		
		return comp;
	}
	
	public static final String MODTAG = "modifiers";

	static final String NAME = "name";  
	public String tagName();
	public void writeToLore(List<Component> list, boolean shift);
	public Component writeDetailsToLore();
	public String description();
	public String name();
	public String color();
	public Modifier clone();
	public CompoundTag toNBT();
	public Modifier fromNBT(CompoundTag tag);
	public boolean compatible(Modifier mod);
	public boolean forTool(ToolType type);
}

