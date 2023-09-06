package dev.marston.randomloot.loot.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface BlockBreakModifier extends Modifier{
	public void startBreak(ItemStack itemstack, BlockPos pos, LivingEntity player);

	

}
