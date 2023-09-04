package dev.marston.randomloot.loot.modifiers.users;

import java.util.List;

import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.LootItem.ToolType;
import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;
import dev.marston.randomloot.loot.modifiers.UseModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class TorchPlace implements UseModifier{
	private String name;
	private int damage;
	private static final String DAMAGE = "DAMAGE";
	
	
	public TorchPlace(String name, int damage) {
		this.name = name;
		this.damage = damage;
	}
	
	public TorchPlace() {
		this.name = "Spelunking";
		this.damage = 10;
	}
	
	public Modifier clone() {
		return new TorchPlace();
	}

	@Override
	public CompoundTag toNBT() {
		
		CompoundTag tag = new CompoundTag();
				
		tag.putString(NAME, name);
		tag.putInt(DAMAGE, damage);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new TorchPlace(tag.getString(NAME), tag.getInt(DAMAGE));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "torch_place";
	}

	@Override
	public String color() {
		return "yellow";
	}

	@Override
	public void use(UseOnContext ctx) {
		BlockPos pos = ctx.getClickedPos();

		if (ctx.getClickedFace() != Direction.UP) {
			return;
		}
		
		pos = pos.above();
		

		Level l = ctx.getLevel();
		
		BlockPlaceContext bctx = new BlockPlaceContext(ctx);

		BlockState state = Blocks.TORCH.defaultBlockState();
		

		l.setBlock(pos, state, 0);
		l.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(ctx.getPlayer(), state));

		
		ctx.getItemInHand().hurtAndBreak(this.damage, ctx.getPlayer(), (event) -> {
			event.broadcastBreakEvent(EquipmentSlot.MAINHAND);
         });
		
		
	}

	@Override
	public String description() {
		return "Right clicking on the top of a block with the tool in hand will place a torch and use " + this.damage + " durability points.";
	}

	@Override
	public void writeToLore(List<Component> list, boolean shift) {
		
		MutableComponent comp = Modifier.makeComp(this.name(), this.color());
		list.add(comp);

		
		
	}
	
	@Override
	public Component writeDetailsToLore() {

		return Modifier.makeComp("Right-click to place a torch for " + this.damage + " durability points.",
				ChatFormatting.GRAY);

	}
	
	@Override
	public boolean compatible(Modifier mod) {
		return !ModifierRegistry.USERS.contains(mod);
	}
	

	@Override
	public boolean forTool(ToolType type) {
		return true;
	}

}
