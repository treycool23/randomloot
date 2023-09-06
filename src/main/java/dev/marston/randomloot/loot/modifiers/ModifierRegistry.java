package dev.marston.randomloot.loot.modifiers;

import java.util.HashMap;
import java.util.Set;

import dev.marston.randomloot.RandomLootMod;
import dev.marston.randomloot.loot.modifiers.breakers.Attracting;
import dev.marston.randomloot.loot.modifiers.breakers.Explode;
import dev.marston.randomloot.loot.modifiers.breakers.Learning;
import dev.marston.randomloot.loot.modifiers.holders.Effect;
import dev.marston.randomloot.loot.modifiers.holders.Hasty;
import dev.marston.randomloot.loot.modifiers.hurter.Charging;
import dev.marston.randomloot.loot.modifiers.hurter.Critical;
import dev.marston.randomloot.loot.modifiers.hurter.Fire;
import dev.marston.randomloot.loot.modifiers.users.DirtPlace;
import dev.marston.randomloot.loot.modifiers.users.TorchPlace;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;

public class ModifierRegistry {
	
	
	public static HashMap<String, Modifier> Modifiers = new HashMap<String, Modifier>();
	
	
	public static Modifier EXPLODE = register(new Explode());
	public static Modifier LEARNING = register(new Learning());
	public static Modifier ATTRACTING = register(new Attracting());

	public static Modifier TORCH_PLACE = register(new TorchPlace());
	public static Modifier DIRT_PLACE = register(new DirtPlace());
	
	public static Modifier FLAMING = register(new Fire());
	public static Modifier CRITICAL = register(new Critical());
	public static Modifier CHARGING = register(new Charging());
	
	public static Modifier HASTY = register(new Hasty());
	public static Modifier FILLING = register(new Effect("Filling", "filling", 2, MobEffects.SATURATION));
	public static Modifier ABSORBTION = register(new Effect("Appley", "absorbtion", 10, MobEffects.ABSORPTION));
	public static Modifier REGENERATING = register(new Effect("Healing", "regeneration", 3, MobEffects.REGENERATION));
	public static Modifier RESISTANT = register(new Effect("Resistant", "resistance", 1, MobEffects.DAMAGE_RESISTANCE));
	public static Modifier FIRE_RESISTANT = register(new Effect("Heat Resistant", "fire_resistance", 1, MobEffects.FIRE_RESISTANCE));

	public static final Set<Modifier> BREAKERS = Set.of(EXPLODE, LEARNING, ATTRACTING);
	public static final Set<Modifier> USERS = Set.of(TORCH_PLACE, DIRT_PLACE);
	public static final Set<Modifier> HURTERS = Set.of(CRITICAL, CHARGING, FLAMING);
	public static final Set<Modifier> HOLDERS = Set.of(HASTY, ABSORBTION, FILLING);

	public static Modifier register(Modifier modifier) {
		
		String tagName = modifier.tagName();
		
		if (Modifiers.containsKey(tagName)) {
			RandomLootMod.LOGGER.error("Cannot register modifier twice!");
			System.exit(1);
		}
		
		Modifiers.put(tagName, modifier);
		
		return modifier;
	}
	
	public static Modifier loadModifier(String name, CompoundTag tag) {
			Modifier m = Modifiers.get(name);
			if (m == null) {
				return null;
			}
			
			return m.fromNBT(tag);
	}
	
	

}
