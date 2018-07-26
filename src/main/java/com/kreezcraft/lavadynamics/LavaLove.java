package com.kreezcraft.lavadynamics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber
public class LavaLove {
	private static final Random RANDOM = null;

	private static void debug(EntityPlayer player, String msg) {
		if (player != null)
			player.sendMessage(new TextComponentString(msg));
	}

	private static void debug(String msg) {
		if (LavaConfig.general.debugMode)
			System.out.println(msg);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(LavaDynamics.MODID)) {
			ConfigManager.sync(LavaDynamics.MODID, Config.Type.INSTANCE);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@SubscribeEvent
	public static void worldSmelting(BlockEvent event) {

		boolean allowErupt = false; // assume that the chunk has a tileentity, until we are sure it doesn't

		EnumFacing facing;

		BlockPos thisPos = null, targetPos = null;
		thisPos = event.getPos();

		EntityPlayer player = null;
		player = event.getWorld().getClosestPlayer(thisPos.getX(), thisPos.getY(), thisPos.getZ(), 10, false);

		Block thisBlock = null, blockTarget = null, blockFromTarget = null;
		ItemStack targetOutput = null;

		IBlockState targetState = null;
		int meta = 0, targetMeta = 0;

		thisBlock = event.getState().getBlock();
		if (thisBlock != Blocks.LAVA && thisBlock != Blocks.FLOWING_LAVA) {
			// it's not lava, get out here!
			// if(LavaConfig.debugMode.getBoolean()) System.out.println("not lava!");
			// this code block prevents unnecessary code execution
			return;
		}

		World worldIn = event.getWorld();

		// We don't do this during worldGen!

		// debug("before chunk neighbor test");
		if (!Support.chunkNeighborsLoaded(worldIn, thisPos))
			return;
		// debug("after chunk neighbor test and before is populated test");
		if (!worldIn.getChunkFromBlockCoords(thisPos).isPopulated())
			return;
		// debug("after is populated test");

		if (LavaConfig.volcanoSettings.volcanoChance > 0) {
			// check for crafted and return if found
			// Chunk theChunk = worldIn.getChunkFromBlockCoords(player.getPosition());
			Chunk theChunk = worldIn.getChunkFromBlockCoords(thisPos);
			if (theChunk != null) {
				// eclipse wanted a null check here?!!
				if (LavaConfig.protection.protection) {
					Map<BlockPos, TileEntity> scanlist = theChunk.getTileEntityMap();
					if (scanlist.isEmpty())
						allowErupt = true;
					else {
						if (LavaConfig.general.debugMode)
							System.out.println("TileEntities were found, allowErupt is false!");
					}
				} else {
					allowErupt = true; // because protection is false, then nothing is safe!
				}
			}
		}

		// check for the 3 main dimensions, refuse all others for now
		if (worldIn.provider.isNether() && !LavaConfig.dimensions.dimNether)
			return;

		if (worldIn.provider.isSurfaceWorld() && !LavaConfig.dimensions.dimOverworld)
			return;

		int dimension = worldIn.provider.getDimension();

		if (dimension == 1 && !LavaConfig.dimensions.dimEnd)
			return;

		if (dimension < -1 || dimension > 1) {
			if (!Arrays.asList(LavaConfig.dimensions.dimsToAllow).contains(dimension))
				return;
		}

		if (LavaConfig.protection.preserveVillages && worldIn.villageCollection.getNearestVillage(thisPos,
				LavaConfig.protection.findVillageRange) != null) {
			// get out of here, we're saving that village!
			return;
		}

		if (allowErupt) {
			Eruption.do_erupt(worldIn, thisPos);
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

			if (!Support.compatible(blockTarget)) {
				if (LavaConfig.general.debugMode)
					System.out.println("Incompatible mod " + Support.getModID(blockTarget) + " detected!");
				return;
			}

			if (LavaDynamics.time == null)
				LavaDynamics.time = worldIn.getWorldTime();
			/*
			 * build the volcanic walls
			 */

			// volcanic wall construction
			// because one of these might be true for blockTarget
			// Blocks.AIR, Blocks.LAVA, Blocks.FLOWING_LAVA)

			if (Support.cardinalIsAir(worldIn, thisPos)) { // walls should be built regardless if another volcano is
															// being
				// generated
				Random rNoise = new Random();
				int lowNoise = LavaConfig.noise.lowNoise;
				int upCheck = rNoise.nextInt(LavaConfig.noise.highNoise) + lowNoise;
				Block aboveMe = worldIn.getBlockState(thisPos.up(upCheck)).getBlock();
				if (LavaConfig.general.debugMode)
					System.out.println("attempt to build a wall at " + thisPos.toString());
				if (
				// (blockTarget == Blocks.LAVA || blockTarget == Blocks.FLOWING_LAVA)
				// &&
				(aboveMe == Blocks.LAVA || aboveMe == Blocks.FLOWING_LAVA)
				// && blockTarget == Blocks.AIR
				) {
					// hopefully this means it's a perimeter lava block and not one in the core of a
					// pool
					Block block = Support.getRndOre().getBlock();

					if (block != Blocks.STONE) {
						if (rNoise.nextInt(100) < LavaConfig.volcanoSettings.nodulePartChance)
							worldIn.setBlockState(thisPos.up(), Blocks.STONE.getDefaultState());
						if (rNoise.nextInt(100) < LavaConfig.volcanoSettings.nodulePartChance)
							worldIn.setBlockState(thisPos.down(), Blocks.STONE.getDefaultState());
						if (rNoise.nextInt(100) < LavaConfig.volcanoSettings.nodulePartChance)
							worldIn.setBlockState(thisPos.east(), Blocks.STONE.getDefaultState());
						if (rNoise.nextInt(100) < LavaConfig.volcanoSettings.nodulePartChance)
							worldIn.setBlockState(thisPos.west(), Blocks.STONE.getDefaultState());
						if (rNoise.nextInt(100) < LavaConfig.volcanoSettings.nodulePartChance)
							worldIn.setBlockState(thisPos.north(), Blocks.STONE.getDefaultState());
						if (rNoise.nextInt(100) < LavaConfig.volcanoSettings.nodulePartChance)
							worldIn.setBlockState(thisPos.south(), Blocks.STONE.getDefaultState());
					}

					worldIn.setBlockState(thisPos, block.getDefaultState());

				}
			}
			/*
			 * end build the walls
			 */

			if (!targetState.isFullBlock() && !LavaConfig.mappings.partialBlock) {
				if (LavaConfig.general.debugMode)
					System.out.println("partial block detected");
				return; // don't convert or consume partial blocks
			}

			targetOutput = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(blockTarget, 1, meta));
			targetMeta = targetOutput.getMetadata();
			blockFromTarget = null;

			if (targetOutput != null) {

				blockFromTarget = Block.getBlockFromItem(targetOutput.getItem());
				boolean furnaceRecipes = LavaConfig.mappings.furnaceRecipes;

				if (blockTarget != Blocks.AIR && blockTarget != Blocks.LAVA && blockTarget != Blocks.FLOWING_LAVA) {

					// if (thisBlock == Blocks.LAVA || thisBlock == Blocks.FLOWING_LAVA) {
					if (blockTarget.isFlammable(worldIn, targetPos, facing) || blockTarget == Blocks.COAL_ORE
							|| blockTarget == Blocks.COAL_BLOCK) {
						Random r = new Random();
						int randomInt = r.nextInt(100) + 1;
						if (randomInt < LavaConfig.onlyTheLava.lavaSpread) {

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

								if (LavaConfig.onlyTheLava.sourceBlock) {
									worldIn.setBlockState(targetPos, Blocks.LAVA.getDefaultState());
								}

							}

							// explosions are generated when new lava is determined to be formed. It is now
							// possible to witness exploding lava flows with out no new lava source blocks.
							randomInt = r.nextInt(100);
							float explosion = (float) ((r.nextFloat() * LavaConfig.explosions.maxExplosion)
									+ LavaConfig.explosions.minExplosion);
							if (randomInt < LavaConfig.explosions.chanceExplosion)
								worldIn.createExplosion(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(),
										explosion, false);
							return;
						}
					}

					// blockFromTarget.getBlockState()
					if (!Arrays.asList(LavaConfig.mappings.smeltingBlacklist)
							.contains(blockTarget.getRegistryName().toString())) {
						// Only smelt if blockTargets RegistryName is not in smeltingBlacklist
						beTheLava(worldIn, furnaceRecipes, targetPos, blockFromTarget, targetOutput, targetMeta,
								facing);
					}
					Support.makeEffect(worldIn, thisPos);

				}
			}
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
				if (LavaConfig.general.debugMode)
					System.out.println("targetOutput should be " + targetOutput.getDisplayName());
				worldIn.setBlockToAir(targetPos);
				lavaSmelt(worldIn, targetOutput, ThreadLocalRandom.current().nextInt(1, 10), facing, targetPos);
			}
		}

		// conversions from mappings here
		// we don't need this to be an else block because we want them to be able to
		// specify other things that could be inworld smelted.
		if (!LavaConfig.mappings.conversions.isEmpty()) {
			String key = blockFromTarget.getRegistryName().toString();
			if (blockFromTarget != null && blockFromTarget != Blocks.AIR) {
				// smelt block in place from furnace recipe
				if (LavaConfig.mappings.conversions.containsKey(key)) {
					if (LavaConfig.mappings.conversions.get(key) != null) {
						ResourceLocation theItem = new ResourceLocation(LavaConfig.mappings.conversions.get(key));
						targetOutput = ForgeRegistries.ITEMS.getValue(theItem).getDefaultInstance();
						// smelt block into item and put in same place as original block
						worldIn.setBlockToAir(targetPos);
						lavaSmelt(worldIn, targetOutput, ThreadLocalRandom.current().nextInt(1, 10), facing, targetPos);
					}
				}
			}
		}
	}
}
