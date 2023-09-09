package dev.marston.randomloot.loot.modifiers.breakers;

import java.util.List;

import javax.annotation.Nullable;

import dev.marston.randomloot.RandomLootMod;
import dev.marston.randomloot.loot.LootItem.ToolType;
import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class Attracting implements BlockBreakModifier {

	private String name;
	private float power;
	private final static String POWER = "power";

	public Attracting(String name, float power) {
		this.name = name;
		this.power = power;
	}

	public Attracting() {
		this.name = "Magnetic";
		this.power = 2.0f;
	}

	public Modifier clone() {
		return new Attracting();
	}

	@Override
	public void startBreak(ItemStack itemstack, BlockPos pos, LivingEntity player) {

		Level l = player.level();

		AABB box = new AABB(pos.east().south().below(), pos.west().north().above());

		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<Entity> items = l.getEntities(null, box);

				for (Entity entity : items) {
					RandomLootMod.LOGGER.info("Entity: " + entity.getDisplayName());

					if (entity.getType() == EntityType.ITEM) {
						entity.setPos(player.position());
					}
				}
			}
		};

		thread.start();

	}

	@Override
	public CompoundTag toNBT() {

		CompoundTag tag = new CompoundTag();

		tag.putFloat(POWER, power);

		tag.putString(NAME, name);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Attracting(tag.getString(NAME), tag.getFloat(POWER));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "attracting";
	}

	@Override
	public String color() {
		return "red";
	}

	@Override
	public String description() {
		return "Upon breaking a block (allowed by tool type), all items at that block's position will teleport to you.";
	}

	@Override
	public void writeToLore(List<Component> list, boolean shift) {

		MutableComponent comp = Modifier.makeComp(this.name(), this.color());

		list.add(comp);
	}

	@Override
	public Component writeDetailsToLore(@Nullable Level level) {

		return null;
	}

	@Override
	public boolean compatible(Modifier mod) {
		return true;
	}

	@Override
	public boolean forTool(ToolType type) {
		return type.equals(ToolType.PICKAXE) || type.equals(ToolType.AXE) || type.equals(ToolType.SHOVEL);
	}
}
