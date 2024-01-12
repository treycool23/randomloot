package dev.marston.randomloot;

import java.util.Map;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.marston.randomloot.loot.LootCase;
import dev.marston.randomloot.loot.LootRegistry;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.recipes.Recipies;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(RandomLootMod.MODID)
public class RandomLootMod {
	public static final String MODID = "randomloot";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, MODID);

	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
			.create(Registries.RECIPE_SERIALIZER, MODID);

	static RandomLootMod INSTANCE;

	public RandomLootMod() {

		if (INSTANCE != null) {
			throw new IllegalStateException();
		}
		INSTANCE = this;

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener((RegisterEvent event) -> {
			if (!event.getRegistryKey().equals(Registries.BLOCK)) {
				return;
			}
			Recipies.init(ForgeRegistries.RECIPE_SERIALIZERS);
		});

		bus.addListener(this::commonSetup);
		bus.addListener(this::addCreative);

		MinecraftForge.EVENT_BUS.register(this);

		ModLootModifiers.register(bus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

		GenWiki.genWiki();

	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info("RandomLoot Common Setup");

	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
			event.accept(LootRegistry.CaseItem);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("Starting server with RandomLoot installed!");

		Globals.Seed = event.getServer().getWorldData().worldGenOptions().seed();
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			LOGGER.info("Client is starting with RandomLoot installed!");

			event.enqueueWork(() -> {
				ItemProperties.register(LootRegistry.ToolItem, new ResourceLocation(RandomLootMod.MODID, "cosmetic"),
						(stack, level, living, id) -> {
							return LootUtils.getTexture(stack);
						});
			});
		}
	}

	@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void registerItems(RegisterEvent event) {

			event.register(ForgeRegistries.Keys.ITEMS, helper -> {
				for (Map.Entry<String, Item> entry : LootRegistry.Items.entrySet()) {
					String key = entry.getKey();
					Item val = entry.getValue();

					helper.register(new ResourceLocation(MODID, key), val);
				}

			});
			LootCase.initDispenser();

		}

	}
}
