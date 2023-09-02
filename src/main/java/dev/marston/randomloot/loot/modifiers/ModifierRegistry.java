package dev.marston.randomloot.loot.modifiers;

import java.util.HashMap;

import dev.marston.randomloot.loot.modifiers.breakers.Explode;
import net.minecraft.nbt.CompoundTag;

public class ModifierRegistry {
	
	
	public static HashMap<String, Modifier> Modifiers = new HashMap<String, Modifier>();
	
	
	public static Modifier EXPLODE = register(new Explode());
	
	
	public static Modifier register(Modifier modifier) {
		
		Modifiers.put(modifier.tagName(), modifier);
		
		return modifier;
		
		
	}
	
	public static Modifier loadModifier(String name, CompoundTag tag) {
			Modifier m = Modifiers.get(name);
			
			return m.fromNBT(tag);
	}
	
	

}
