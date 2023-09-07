package dev.marston.randomloot.loot;

import java.util.List;

import javax.annotation.Nullable;

import dev.marston.randomloot.RandomLootMod;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack lootCase = player.getItemInHand(hand);
		lootCase.shrink(1); // removing case from inventory
		
		if (player instanceof ServerPlayer) {
			ServerPlayer sPlayer = (ServerPlayer) player;
			StatType<Item> itemUsed = Stats.ITEM_USED;
			
			sPlayer.getStats().increment(sPlayer, itemUsed.get(LootRegistry.CaseItem), 1);
		}
		
		Modifier.TrackEntityParticle(level, player, ParticleTypes.CLOUD);

		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LootUtils.generateTool(player, level); // generate tool and give it to the player
				
			}
		};

		thread.start();

		return InteractionResultHolder.success(lootCase);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tipList, TooltipFlag flag) {

		MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
		comp.append("Right-click for loot!");
		comp = comp.withStyle(ChatFormatting.GRAY);

		tipList.add(comp);

	}

}
