package com.eleksploded.lavadynamics.command;

import com.eleksploded.lavadynamics.VolcanoManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;

public class SpawnVolcano {
	public static void register(CommandDispatcher<CommandSource> d) {
		d.register(Commands.literal("spawnVolcano").executes(c -> spawnVolcano(c))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(c -> spawnVolcano(c))));
	}

	private static int spawnVolcano(CommandContext<CommandSource> c) {
		
		try {
			BlockPos p = BlockPosArgument.getBlockPos(c, "pos");
			
			Chunk chunk = (Chunk) c.getSource().getWorld().getChunk(p);
			VolcanoManager.spawnVolcano(c.getSource().getWorld(), chunk);
			
		} catch (CommandSyntaxException e) {
			c.getSource().sendErrorMessage(new TranslationTextComponent("ld.command.invalidpos"));
		} catch (IllegalArgumentException e) {
			Chunk chunk = (Chunk) c.getSource().getWorld().getChunk(new BlockPos(c.getSource().getPos()));
			VolcanoManager.spawnVolcano(c.getSource().getWorld(), chunk);
		}
		
		return 1;
	}
	
	
}
