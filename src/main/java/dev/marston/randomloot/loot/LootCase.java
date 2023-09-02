package dev.marston.randomloot.loot;

import dev.marston.randomloot.loot.LootItem.ToolType;
import dev.marston.randomloot.loot.modifiers.LootUtils;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class LootCase extends Item{

	public LootCase() {
		super(new Properties().stacksTo(1));
	}
	
	
	@Override
	public InteractionResult useOn(UseOnContext useContext) {
		ItemStack lootCase = useContext.getItemInHand();
		
		lootCase.shrink(1);
		
		Player player = useContext.getPlayer();
	
		
		ItemStack lootItem = new ItemStack(LootRegistry.ToolItem);
		
		lootItem = LootItem.setToolType(lootItem, ToolType.PICKAXE);
		
		lootItem = LootUtils.addModifier(lootItem, ModifierRegistry.EXPLODE);
		
		
		lootItem = LootUtils.updateLore(lootItem);
		
		
		player.addItem(lootItem);
		
		
		return InteractionResult.SUCCESS;
	}
}
