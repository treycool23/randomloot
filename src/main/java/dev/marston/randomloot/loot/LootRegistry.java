package dev.marston.randomloot.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import com.mojang.logging.LogUtils;

import dev.marston.randomloot.RandomLootMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegisterEvent.RegisterHelper;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.resources.ResourceLocation;


public class LootRegistry {
	
	
	
	public static final HashMap<String, Item> Items = new HashMap<String, Item>();

	public static final Item ToolItem = register("tool", new LootItem());
	public static final Item CaseItem = register("case", new LootCase());

	public static Item register(String name, Item item) {
		Items.put(name, item);
		return item;
	}
	
   
	

	
	
	
}
