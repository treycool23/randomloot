package dev.marston.randomloot.loot;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class LootCase extends Item {

	public LootCase() {
		super(new Properties().stacksTo(1));
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
	      return true;
	   }

	@Override
	public InteractionResult useOn(UseOnContext useContext) {
		ItemStack lootCase = useContext.getItemInHand();
		lootCase.shrink(1); // removing case from inventory

		LootUtils.generateTool(useContext.getPlayer(), useContext.getLevel()); // generate tool and give it to the
																				// player

		return InteractionResult.SUCCESS;
	}
	
	
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tipList, TooltipFlag flag) {
		
		boolean show = Screen.hasShiftDown();
		
		if (!show) {
			return;
		}
		
		MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
		comp.append("Right-click for loot!");
		comp = comp.withStyle(ChatFormatting.GRAY);
		
		tipList.add(comp);
		
	}
	
	
	
}
