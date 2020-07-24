package com.eleksploded.lavadynamics.command;

import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CheckedCommand {
	public static void register(CommandDispatcher<CommandSource> d) {
		d.register(Commands.literal("LD_Check").executes(c -> printHelp(c))
				.then(Commands.literal("isChecked").executes(c -> isChecked(c)))
				.then(Commands.literal("addChecked").executes(c -> addChecked(c))
						.then(Commands.argument("chunkX", IntegerArgumentType.integer())
								.then(Commands.argument("chunkZ", IntegerArgumentType.integer())
										.executes(c -> addChecked(c))
										)))
						.then(Commands.literal("removeChecked").executes(c -> removeChecked(c))
								.then(Commands.argument("chunkX", IntegerArgumentType.integer())
										.then(Commands.argument("chunkZ", IntegerArgumentType.integer())
												.executes(c -> removeChecked(c))
												))
								));
	}

	private static int removeChecked(CommandContext<CommandSource> c) {
		World w = c.getSource().getWorld();
		try {
			int x = IntegerArgumentType.getInteger(c, "chunkX");
			int z = IntegerArgumentType.getInteger(c, "chunkZ");
			Chunk chunk = (Chunk) w.getChunk(x, z);
			IChecked ch = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));

			ch.removeCheck();
			c.getSource().sendFeedback(new TranslationTextComponent("ld.command.removecheck", chunk.getPos().x, chunk.getPos().z), true);
			return 1;
		} catch(IllegalArgumentException e) {
			Chunk chunk = (Chunk) w.getChunk(new BlockPos(c.getSource().getPos()));
			IChecked ch = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));

			ch.removeCheck();
			c.getSource().sendFeedback(new TranslationTextComponent("ld.command.removecheck", chunk.getPos().x, chunk.getPos().z), true);
			return 1;
		}
	}

	private static int addChecked(CommandContext<CommandSource> c) {
		World w = c.getSource().getWorld();
		try {
			int x = IntegerArgumentType.getInteger(c, "chunkX");
			int z = IntegerArgumentType.getInteger(c, "chunkZ");
			Chunk chunk = (Chunk) w.getChunk(x, z);
			IChecked ch = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));

			ch.check();
			c.getSource().sendFeedback(new TranslationTextComponent("ld.command.addedcheck", chunk.getPos().x, chunk.getPos().z), true);
			return 1;
		} catch(IllegalArgumentException e) {
			Chunk chunk = (Chunk) w.getChunk(new BlockPos(c.getSource().getPos()));
			IChecked ch = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));

			ch.check();
			c.getSource().sendFeedback(new TranslationTextComponent("ld.command.addedcheck", chunk.getPos().x, chunk.getPos().z), true);
			return 1;
		}
	}

	private static int isChecked(CommandContext<CommandSource> c) {
		World w = c.getSource().getWorld();
		Chunk chunk = (Chunk) w.getChunk(new BlockPos(c.getSource().getPos()));
		IChecked ch = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));

		TranslationTextComponent responce;

		if(ch.isChecked()) {
			responce = new TranslationTextComponent("ld.command.ischecked", chunk.getPos().x, chunk.getPos().z);
		} else {
			responce = new TranslationTextComponent("ld.command.notchecked", chunk.getPos().x, chunk.getPos().z);
		}

		c.getSource().sendFeedback(responce, false);

		return 1;
	}

	private static int printHelp(CommandContext<CommandSource> c) {
		c.getSource().sendFeedback(new TranslationTextComponent("ld.command.help"), false);
		return 1;
	}
}
