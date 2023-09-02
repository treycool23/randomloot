package dev.marston.randomloot.loot.modifiers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

public class LootUtils {

	
	private static StringTag buildLoreString(String text, String color) {
		JsonObject value = Json.createObjectBuilder()
			     .add("text", text)
			     .add("color", color)
			     .add("italic", false)
			     .build();
			 

		
		StringTag nbtName = StringTag.valueOf(value.toString());
		return nbtName;
	}
	
	public static int getMaxXP(int level) {
		int xp = (int) (500 * Math.pow(2, level));
		return xp;
	}
	
	public static ItemStack addXp(ItemStack item, int amount) {
		
		
		CompoundTag tag = item.getOrCreateTagElement("XP");
		
		int level = tag.getInt("level");
		
		int xp = tag.getInt("xp");
		
		xp += amount;
		
		int max = getMaxXP(level);
		
		while (xp >= max) {
			xp = xp - max;
			level ++;
		}

		
		tag.putInt("level", level);
		tag.putInt("xp", xp);
		
		item.addTagElement("XP", tag);
		
		item = updateLore(item);

		return item;
	}
	
	public static int getLevel(ItemStack item) {
		
		
		CompoundTag tag = item.getOrCreateTagElement("XP");
		
		int level = tag.getInt("level");

		return level;
	}
	
	public static int getXP(ItemStack item) {
		
		
		CompoundTag tag = item.getOrCreateTagElement("XP");
		
		int xp = tag.getInt("xp");

		return xp;
	}
	
	
	public static ItemStack setLevelAndXP(ItemStack item, int level, int xp) {
		
		
		CompoundTag tag = item.getOrCreateTagElement("XP");
		
		tag.putInt("level", level);
		tag.putInt("xp", xp);
		
		item.addTagElement("XP", tag);

		return item;
	}
	
	public static ItemStack updateLore(ItemStack item) {
		
		ListTag lore = new ListTag();
		
		List<Modifier> mods = getModifiers(item);
		
		int level = getLevel(item);
		
		lore.add(buildLoreString("" + getXP(item) + " / " + getMaxXP(level) + " | lvl " + level, "gray"));

		lore.add(buildLoreString("", "gray"));
		lore.add(buildLoreString("Traits", "gray"));
		lore.add(buildLoreString("------------", "gray"));

		
		for (Modifier modifier : mods) {
			lore.add(buildLoreString(modifier.name(), modifier.color()));
		}
		
		lore.add(buildLoreString("------------", "gray"));

		
		CompoundTag display = item.getOrCreateTagElement("display");
		
		display.put("Lore", lore);

		item.addTagElement("display", display);
		
		
		return item;
	}
	
public static List<Modifier> getModifiers(ItemStack item) {
		
		ArrayList<Modifier> tags = new ArrayList<Modifier>();
	
		CompoundTag modifiers = item.getTagElement(Modifier.MODTAG);
		if (modifiers == null) {
			return tags; 
		}
		
		Set<String> mods = modifiers.getAllKeys();
		
		for (Iterator iterator = mods.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			
			CompoundTag modTag = modifiers.getCompound(string);
			
			Modifier finalModifier = ModifierRegistry.loadModifier(string, modTag);
			
			tags.add(finalModifier);
			
		}
		
		return tags;
	}
	
	public static ItemStack addModifier(ItemStack item, Modifier mod) {
		
		CompoundTag modifiers = item.getOrCreateTagElement(Modifier.MODTAG);
		
		
		CompoundTag newTag = mod.toNBT();
		modifiers.put(mod.tagName(), newTag);
		
		item.removeTagKey(Modifier.MODTAG);
		item.addTagElement(Modifier.MODTAG, modifiers);
		
		return item;
	}
	
}
