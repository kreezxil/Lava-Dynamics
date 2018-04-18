package com.kreezcraft.lavadynamics;

import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
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

		EnumFacing facing;

		BlockPos thisPos = null, targetPos = null;
		thisPos = event.getPos();

		EntityPlayer player = null;
		player = event.getWorld().getClosestPlayer(thisPos.getX(), thisPos.getY(), thisPos.getZ(), 10, false);
		
//		if(player==null) {
//			System.out.print("no player!");
//			return; //no player
//		}
		
		

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
		

		World worldIn = event.getWorld();

		if(player!=null) {
			if(Config.debugMode.getBoolean()) System.out.println("we have a player");
			if(thisPos.getX()>>4!=player.chunkCoordX || thisPos.getZ()>>4!=player.chunkCoordZ) {
				if(Config.debugMode.getBoolean()) System.out.println("player not in chunk with block event");
			return;//player not in chunk
			}
		}
		
		if(worldIn.provider.isNether() && !Config.dimNether.getBoolean()) return;
		if(worldIn.provider.isSurfaceWorld() && !Config.dimOverworld.getBoolean()) return;
		if(worldIn.provider.getDimension()==1 && !Config.dimEnd.getBoolean()) return;
		
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

			if(!targetState.isFullBlock() && !Config.partialBlock.getBoolean()) {
				if(Config.debugMode.getBoolean()) System.out.println("partial block detected");
				return; //don't convert or consume partial blocks
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

					if (thisBlock == Blocks.LAVA || thisBlock == Blocks.FLOWING_LAVA) {
						if (blockTarget.isFlammable(worldIn, targetPos, facing) || blockTarget == Blocks.COAL_ORE
								|| blockTarget == Blocks.COAL_BLOCK) {
							Random r = new Random();
							int randomInt = r.nextInt(100) + 1;
							if (randomInt < 10) {

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
								if(randomInt<5) 
									worldIn.createExplosion(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 6f, false);
								return;
							}
						}

						// blockFromTarget.getBlockState()
						beTheLava(worldIn, furnaceRecipes, targetPos, blockFromTarget, targetOutput, targetMeta,
								facing);
						makeEffect(worldIn, thisPos);

					}
				}
			}
		}
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
