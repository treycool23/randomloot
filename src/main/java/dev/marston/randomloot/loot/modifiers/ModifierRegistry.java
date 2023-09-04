package dev.marston.randomloot.loot.modifiers;

import java.util.HashMap;
import java.util.Set;

import dev.marston.randomloot.loot.modifiers.breakers.Explode;
import dev.marston.randomloot.loot.modifiers.breakers.Learning;
import dev.marston.randomloot.loot.modifiers.users.DirtPlace;
import dev.marston.randomloot.loot.modifiers.users.TorchPlace;
import net.minecraft.nbt.CompoundTag;

public class ModifierRegistry {
	
	
	public static HashMap<String, Modifier> Modifiers = new HashMap<String, Modifier>();
	
	
	public static Modifier EXPLODE = register(new Explode());
	public static Modifier TORCH_PLACE = register(new TorchPlace());
	public static Modifier DIRT_PLACE = register(new DirtPlace());
	public static Modifier LEARNING = register(new Learning());

	public static final Set<Modifier> BREAKERS = Set.of(EXPLODE, LEARNING);
	public static final Set<Modifier> USERS = Set.of(TORCH_PLACE, DIRT_PLACE);

	
	public static Modifier register(Modifier modifier) {
		
		Modifiers.put(modifier.tagName(), modifier);
		
		return modifier;
	}
	
	public static Modifier loadModifier(String name, CompoundTag tag) {
			Modifier m = Modifiers.get(name);
			
			return m.fromNBT(tag);
	}
	
	

}
