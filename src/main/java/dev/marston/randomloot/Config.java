package dev.marston.randomloot;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = RandomLootMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	private static final ForgeConfigSpec.DoubleValue CASE_CHANCE = BUILDER.comment("chance to find a case in a chest.")
			.defineInRange("caseChance", 0.25, 0.0, 1.0);

	private static final ForgeConfigSpec.DoubleValue MOD_CHANCE = BUILDER
			.comment("chance to find a modifier template in a chest.").defineInRange("modChance", 0.15, 0.0, 1.0);
	
	private static final ForgeConfigSpec.DoubleValue GOODNESS = BUILDER.comment("rate of tool improvement per player")
			.defineInRange("goodness_rate", 1.0, 0.01, 10.0);
	
	static final ForgeConfigSpec SPEC = build();
	
	public static ForgeConfigSpec build() {
		init();
		return BUILDER.build();
	}

	public static double CaseChance;
	public static double ModChance;
	public static double Goodness;

	private static Map<String, ForgeConfigSpec.BooleanValue> MODIFIERS_ENABLED;
	private static Map<String, Boolean> ModsEnabled;

	public static void init() {
		

		MODIFIERS_ENABLED = new HashMap<String, ForgeConfigSpec.BooleanValue>();

		for (Entry<String, Modifier> entry : ModifierRegistry.Modifiers.entrySet()) {
			String key = entry.getKey();
			Modifier mod = entry.getValue();

			MODIFIERS_ENABLED.put(key,
					BUILDER.comment("should the " + mod.name() + " trait be enabled").define(key + "_enabled", true));
		}
	}

	private static boolean validateItemName(final Object obj) {
		return obj instanceof final String itemName
				&& ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
	}
	
	public static boolean traitEnabled(String tagName) {
		return ModsEnabled.get(tagName);
	}

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		CaseChance = CASE_CHANCE.get();
		ModChance = MOD_CHANCE.get();
		Goodness = GOODNESS.get();

		ModsEnabled = new HashMap<String, Boolean>();
		for (Entry<String, BooleanValue> entry : MODIFIERS_ENABLED.entrySet()) {
			String key = entry.getKey();
			BooleanValue val = entry.getValue();

			ModsEnabled.put(key, val.get());
		}

	}
}
