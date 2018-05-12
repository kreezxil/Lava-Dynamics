package com.kreezcraft.lavadynamics;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.actors.threadpool.Arrays;

@Mod.EventBusSubscriber
public class LavaLove {
	private static final Random RANDOM = null;

	private static void debug(EntityPlayer player, String msg) {
		if (player != null)
			player.sendMessage(new TextComponentString(msg));
	}

	@SubscribeEvent
	public static void worldSmelting(BlockEvent event) {
		// NeighborNotifyEvent
		// public static void worldSmelting(NeighborNotifyEvent event) {

		boolean allowErupt = false; // assume that the chunk has a tileentity, until we are sure it doesn't

		EnumFacing facing;

		BlockPos thisPos = null, targetPos = null;
		thisPos = event.getPos();

		EntityPlayer player = null;
		player = event.getWorld().getClosestPlayer(thisPos.getX(), thisPos.getY(), thisPos.getZ(), 10, false);

		// if(player==null) {
		// System.out.println("no player!");
		// return; //no player
		// }

		Block thisBlock = null, blockTarget = null, blockFromTarget = null;
		ItemStack targetOutput = null;

		IBlockState targetState = null;
		int meta = 0, targetMeta = 0;

		try {
			thisBlock = event.getState().getActualState(null, thisPos).getBlock();
		} catch (Exception e) {
			// nothing left to do, exit function
			return;
		}
		if (thisBlock != Blocks.LAVA && thisBlock != Blocks.FLOWING_LAVA) {
			// it's not lava, get out here!
			// if(Config.debugMode.getBoolean()) System.out.println("not lava!");
			// this code block prevents unnecessary code execution
			return;
		}

		World worldIn = event.getWorld();

		// never do this again! it breaks the mod!
		// if (player == null)
		// return;

		// the following code block checks to see if a player is in the present chunk
		// and if not exits the event to prevent unnecessary code execution light that
		// caused
		// in cascading world gen
		// also a player must be present in a chunk for a volcano to be generated, yeah
		// players
		// are gonna love hate this so bad!
		if (player != null) {
			if (Config.debugMode.getBoolean())
				System.out.println("we have a player");

			// if (thisPos.getX() >> 4 != player.chunkCoordX || thisPos.getZ() >> 4 !=
			// player.chunkCoordZ) {
			if (Math.abs(thisPos.getX() - player.getPosition().getX()) <= 100
					|| Math.abs(thisPos.getZ() - player.getPosition().getZ()) <= 100) {
				if (Config.debugMode.getBoolean())
					System.out.println("player not in range of block event");
				return;// player not in range
			}

		}

		if (Config.volcanoChance.getInt() > 0) {
			// check for crafted and return if found
			// Chunk theChunk = worldIn.getChunkFromBlockCoords(player.getPosition());
			Chunk theChunk = worldIn.getChunkFromBlockCoords(thisPos);
			if (theChunk != null) {
				// eclipse wanted a null check here?!!
				Map<BlockPos, TileEntity> scanlist = theChunk.getTileEntityMap();
				if (scanlist.isEmpty())
					allowErupt = true;
				else {
					if (Config.debugMode.getBoolean())
						System.out.println("TileEntities were found, allowErupt is false!");
				}
			}
		}

		// check for the 3 main dimensions, refuse all others for now
		if (worldIn.provider.isNether() && !Config.dimNether.getBoolean())
			return;

		if (worldIn.provider.isSurfaceWorld() && !Config.dimOverworld.getBoolean())
			return;

		if (worldIn.provider.getDimension() == 1 && !Config.dimEnd.getBoolean())
			return;

		if (worldIn.provider.getDimension() < -1 || worldIn.provider.getDimension() > 1)
			return;

		if (allowErupt) {
			// if (do_erupt(worldIn, thisPos))
			// return;
			do_erupt(worldIn, thisPos);
		}

		for (int i = 0; i < 6; i++) {
			/*
			 * 0 down 1 north 2 south 3 west 4 east
			 */

			switch (i) {
			case 0:
				targetPos = thisPos.down();
				facing = EnumFacing.UP; // because it is below the lava, if it ejected down the player probably have a
										// chance to capture it.
				break;
			case 1:
				targetPos = thisPos.north();
				facing = EnumFacing.NORTH;
				break;
			case 2:
				targetPos = thisPos.south();
				facing = EnumFacing.SOUTH;
				break;
			case 3:
				targetPos = thisPos.west();
				facing = EnumFacing.WEST;
				break;
			case 4:
				targetPos = thisPos.east();
				facing = EnumFacing.EAST;
				break;
			case 5:
				targetPos = thisPos.up();
				facing = EnumFacing.UP;
			default:
				return;
			}

			targetState = worldIn.getBlockState(targetPos);
			blockTarget = targetState.getBlock();
			meta = blockTarget.getMetaFromState(targetState);

			if (!targetState.isFullBlock() && !Config.partialBlock.getBoolean()) {
				if (Config.debugMode.getBoolean())
					System.out.println("partial block detected");
				return; // don't convert or consume partial blocks
			}

			targetOutput = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(blockTarget, 1, meta));
			targetMeta = targetOutput.getMetadata();
			blockFromTarget = null;

			if (targetOutput != null) {

				blockFromTarget = Block.getBlockFromItem(targetOutput.getItem());
				boolean furnaceRecipes = Config.furnaceRecipes.getBoolean();

				// if (blockFromTarget != null || blockFromTarget != Blocks.AIR) { // air as
				// output? wtf?

				if (blockTarget != Blocks.AIR && blockTarget != Blocks.LAVA && blockTarget != Blocks.FLOWING_LAVA) {

					// if (thisBlock == Blocks.LAVA || thisBlock == Blocks.FLOWING_LAVA) {
					if (blockTarget.isFlammable(worldIn, targetPos, facing) || blockTarget == Blocks.COAL_ORE
							|| blockTarget == Blocks.COAL_BLOCK) {
						Random r = new Random();
						int randomInt = r.nextInt(100) + 1;
						if (randomInt < Config.lavaSpread.getInt()) {

							worldIn.setBlockToAir(targetPos);

							for (int j = 0; j < r.nextInt(3) + 1; j++) {
								int which = r.nextInt(3) + 1;
								switch (which) {
								case 0:
									targetPos.add(targetPos.getX() + 1, 0, 0);
									break;
								case 1:
									targetPos.add(0, targetPos.getY() + 1, 0);
									break;
								case 2:
									targetPos.add(0, 0, targetPos.getZ() + 1);
								}

								worldIn.setBlockState(targetPos, Blocks.LAVA.getDefaultState());

							}

							randomInt = r.nextInt(100);
							float explosion = (float) ((r.nextFloat() * Config.maxExplosion.getDouble()) + Config.minExplosion.getDouble());
							if (randomInt < Config.chanceExplosion.getInt())
								worldIn.createExplosion(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(),
										explosion, false);
							return;
						}
					}

					// blockFromTarget.getBlockState()
					beTheLava(worldIn, furnaceRecipes, targetPos, blockFromTarget, targetOutput, targetMeta, facing);
					makeEffect(worldIn, thisPos);

				} else {
					// volcanic wall construction
					// because one of these might be true for blockTarget
					// Blocks.AIR, Blocks.LAVA, Blocks.FLOWING_LAVA)
					Random rNoise = new Random();
					int lowNoise = Config.lowNoise.getInt();
					int highNoise = rNoise.nextInt(Config.highNoise.getInt()) + 1;
					Block aboveMe = worldIn.getBlockState(thisPos.up(highNoise + lowNoise)).getBlock();
					if ((blockTarget != Blocks.LAVA && blockTarget != Blocks.FLOWING_LAVA)
							&& (aboveMe == Blocks.LAVA || aboveMe == Blocks.FLOWING_LAVA)
							&& blockTarget == Blocks.AIR) {
						// hopefully this means it's a perimeter lava block and not one in the core of a
						// pool
						Block block = getRndOre().getBlock();

						if (block != Blocks.STONE) {
							worldIn.setBlockState(thisPos.up(), Blocks.STONE.getDefaultState());
							worldIn.setBlockState(thisPos.down(), Blocks.STONE.getDefaultState());
							worldIn.setBlockState(thisPos.east(), Blocks.STONE.getDefaultState());
							worldIn.setBlockState(thisPos.west(), Blocks.STONE.getDefaultState());
							worldIn.setBlockState(thisPos.north(), Blocks.STONE.getDefaultState());
							worldIn.setBlockState(thisPos.south(), Blocks.STONE.getDefaultState());
						}

						worldIn.setBlockState(thisPos, block.getDefaultState());

					} 
//					else if (aboveMe == Blocks.AIR && blockTarget == Blocks.AIR) {
//						if (rNoise.nextInt(10) <= 2)
//							worldIn.setBlockState(thisPos, Blocks.STONE.getDefaultState());
//					}

				}
			}
		}
	}
	// }

	private static boolean do_erupt(World worldIn, BlockPos thisPos) {
		Random chance = new Random();
		int Volcano = chance.nextInt(100);
		// if (Config.debugMode.getBoolean())
		// System.out.println("Volcano chance is " + Volcano);
		if (Config.volcanoGen.getBoolean())
			return false; // don't start another volcano until the current one is done
		if (Volcano <= Config.volcanoChance.getInt()) {
			// if (Config.debugMode.getBoolean())
			// System.out.println("checking to see if 2 blocks up is air");
			if (thisPos.getY() <= Config.maxYlevel.getInt() && worldIn.getBlockState(thisPos.up(2)) != Blocks.AIR.getDefaultState()) {
				// if (Config.debugMode.getBoolean())
				// System.out.println("checking to see if lava is below y 69");
				if (thisPos.getY() <= Config.psuedoSurface.getInt()) {
					Config.volcanoGen.set(true);
					int diff = Config.psuedoSurface.getInt() - thisPos.getY();
					int extra = chance.nextInt(Config.extraHt.getInt()) + Config.minHt.getInt();
					int vent = chance.nextInt(diff + extra + Config.minHt.getInt());
					for (int i = 0; i < vent; i++) {
						// if (Config.debugMode.getBoolean())
						// System.out.println("y=" + i);
						worldIn.setBlockState(thisPos.up(i), Blocks.LAVA.getDefaultState());
					}
					Config.volcanoGen.set(false);
					return true;
				}
			}
		}
		return false;
	}

	private static IBlockState getRndOre() {
		Random r = new Random();
		int ore = r.nextInt(100);

		Block block;
		// we should pull from the config at this point but for now it will be hard
		// coded
		// 20% chance to generate ore
		// read the if statements for the chances

		if (ore <= 25)
			block = Blocks.COAL_ORE;
		else if (ore >= 26 && ore <= 30)
			block = Blocks.IRON_ORE;
		else if (ore >= 31 && ore <= 32)
			block = Blocks.GOLD_ORE;
		else if (ore >= 33 && ore <= 34)
			block = Blocks.QUARTZ_ORE;
		else if (ore >= 35 && ore <= 36)
			block = Blocks.LAPIS_ORE;
		else if (ore >= 37 && ore <= 38)
			block = Blocks.REDSTONE_ORE;
		else if (ore == 39)
			block = Blocks.DIAMOND_ORE;
		else if (ore == 40)
			block = Blocks.EMERALD_ORE;
		else if (ore == 41)
			block = Blocks.GOLD_ORE;
		else if (ore == 42)
			block = Blocks.REDSTONE_ORE;
		else {
			// this can pull from the config too for different types of non-ore blocks to
			// place
			block = Blocks.STONE;
		}
		return block.getDefaultState();
	}

	private static void makeEffect(World worldIn, BlockPos thisPos) {
		worldIn.playSound((EntityPlayer) null, thisPos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
				2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

		if (worldIn instanceof WorldServer) {
			((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) thisPos.getX() + 0.5D,
					(double) thisPos.getY() + 0.25D, (double) thisPos.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
		}

	}

	public static void lavaSmelt(World worldIn, ItemStack stack, int speed, EnumFacing facing, BlockPos position) {
		double d0 = position.getX();
		double d1 = position.getY();
		double d2 = position.getZ();

		if (facing.getAxis() == EnumFacing.Axis.Y) {
			d1 = d1 - 0.125D;
		} else {
			d1 = d1 - 0.15625D;
		}

		EntityItem entityitem = new EntityItem(worldIn, d0, d1, d2, stack);
		double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
		entityitem.motionX = (double) facing.getFrontOffsetX() * d3;
		entityitem.motionY = 0.20000000298023224D;
		entityitem.motionZ = (double) facing.getFrontOffsetZ() * d3;
		entityitem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
		entityitem.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
		entityitem.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
		worldIn.spawnEntity(entityitem);
	}

	public static void beTheLava(World worldIn, boolean furnaceRecipes, BlockPos targetPos, Block blockFromTarget,
			ItemStack targetOutput, int targetMeta, EnumFacing facing) {
		if (furnaceRecipes) {
			if (blockFromTarget != null && blockFromTarget != Blocks.AIR) {
				// smelt block in place from furnace recipe
				worldIn.setBlockState(targetPos, blockFromTarget.getStateFromMeta(targetMeta));
				return;
			}
			// smelt block into item and put in same place as original block
			if (targetOutput != null && !targetOutput.getDisplayName().equals("Air")) {// this is not cool in my opinion
																						// but it works
				if (Config.debugMode.getBoolean())
					System.out.println("targetOutput should be " + targetOutput.getDisplayName());
				worldIn.setBlockToAir(targetPos);
				lavaSmelt(worldIn, targetOutput, ThreadLocalRandom.current().nextInt(1, 10), facing, targetPos);
			}
		}
		// conversions from mappings here
	}

}
