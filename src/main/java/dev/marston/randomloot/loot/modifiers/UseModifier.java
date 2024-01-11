package dev.marston.randomloot.loot.modifiers;

import net.minecraft.world.item.context.UseOnContext;

public interface UseModifier extends Modifier {
	public void use(UseOnContext ctx);

}
