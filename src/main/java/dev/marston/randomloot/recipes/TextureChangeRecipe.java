package dev.marston.randomloot.recipes;

import dev.marston.randomloot.RandomLootMod;
import dev.marston.randomloot.loot.LootRegistry;
import dev.marston.randomloot.loot.LootUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

public class TextureChangeRecipe extends CustomRecipe {
	public static SimpleCraftingRecipeSerializer<TextureChangeRecipe> SERIALIZER = null;

	private static final Ingredient CHANGE_TEXTURE_INGREDIENT = Ingredient.of(Items.AMETHYST_SHARD); 
	private static final Item ITEM = LootRegistry.ToolItem;

	public TextureChangeRecipe(ResourceLocation name, CraftingBookCategory cat) {
		super(name, cat);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level level) {
		return !this.getOutput(inv).isEmpty();
	}

	private ItemStack getOutput(Container inv) {
		RandomLootMod.LOGGER.info("Checking recipe output!");
		int size = inv.getContainerSize();

		int modCount = 0;
		ItemStack toolItem = null;
		
		for(int i = 0; i < size; i ++) {
			ItemStack item = inv.getItem(i);
			if (item.isEmpty()) { // skip empty items
				RandomLootMod.LOGGER.info("skipping empty item");
				continue;
			}
			
			if (ITEM == item.getItem()) {
				if (toolItem != null) {
					RandomLootMod.LOGGER.info("can't have two tools");
					return ItemStack.EMPTY;
				}
				RandomLootMod.LOGGER.info("found our tool!");
				toolItem = item.copy();
				continue;
			}
			
			if (CHANGE_TEXTURE_INGREDIENT.test(item)) {
				modCount ++;
			}
			
			
		}
		
		if (toolItem == null) {
			RandomLootMod.LOGGER.info("needs to have at least one tool");
			return ItemStack.EMPTY;
		}
		
		if (modCount == 0 ) {
			RandomLootMod.LOGGER.info("no modifiers");
			return ItemStack.EMPTY;
		}
		
		LootUtils.addTexture(toolItem, modCount);
		
		return toolItem;
		
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
		return this.getOutput(inv);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	

	@Override
	public RecipeSerializer<?> getSerializer() {
		return getMySerializer();
	}

	public static RecipeSerializer<?> getMySerializer() {
		RandomLootMod.LOGGER.info("Getting TextureChange Serializer!!!");
		if (SERIALIZER == null) {
			SERIALIZER = new SimpleCraftingRecipeSerializer<TextureChangeRecipe>(
					(name, category) -> new TextureChangeRecipe(name, category));
		}
		return SERIALIZER;
	}

}