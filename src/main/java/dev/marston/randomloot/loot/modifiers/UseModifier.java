package dev.marston.randomloot.loot.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public interface UseModifier extends Modifier{
	public void use(UseOnContext ctx);

	

}
