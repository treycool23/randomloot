package dev.marston.randomloot.loot;

import java.util.HashMap;

import net.minecraft.world.item.Item;


public class LootRegistry {
	
	
	
	public static final HashMap<String, Item> Items = new HashMap<String, Item>();

	public static final Item ToolItem = register("tool", new LootItem());
	public static final Item CaseItem = register("case", new LootCase());

	public static Item register(String name, Item item) {
		Items.put(name, item);
		return item;
	}
	
   
	

	
	
	
}
