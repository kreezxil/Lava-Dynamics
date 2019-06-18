package com.eleksploded.lavadynamics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Volcano {

	private static boolean worldLoaded = false;
	static int timer = Config.volcanoCooldown;
	static List<Integer> validDim = new ArrayList<Integer>();

	//World Loading events
	@SubscribeEvent
	public static void WorldLoaded(WorldEvent.Load event) {
		for(String name : Config.validDimensions){
			validDim.add(getDim(name));
		}
		worldLoaded = true;
	}

	@SubscribeEvent
	public static void WorldUnloaded(WorldEvent.Unload event) {
		worldLoaded = false;
	}

	@SubscribeEvent
	public static void OnChunkLoad(ChunkEvent.Load event) {
		boolean player = false;
		if(event.getWorld().playerEntities.size() !=0){
			player = true;
		}
		if(!player){
			return;
		}
		
		if(!validDim.contains(event.getWorld().provider.getDimension())){
			return;
		}
		
		if(Config.genVolcanoDebug){
			LavaDynamics.Logger.info(timer);
		}
		
		if(timer != 0){
			timer = timer-1;
			return;
		}
		
		if(Config.protectChunks){
			if(event.getChunk().getTileEntityMap().size() > 0){
				return;
			}
		}
		
		//Get saved value for tested chunks
		VolcanoData data = VolcanoData.get(event.getWorld());
		//Get Chunk that was loaded
		Chunk chunk = event.getChunk();
		//Check if chunk is tested already
		if(Config.genVolcanoDebug) {
			LavaDynamics.Logger.info("Checking chunk at " + chunk.x + " " + chunk.z);
		}
		
		//Get Chunk Center
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();	
		int y = chunk.getHeight(new BlockPos(x,70,z));

		//Check if the chunk is already tested
		if(!data.isChunkTested(chunk) && worldLoaded && !Config.worldGen){
			if(Config.genVolcanoDebug) {
				LavaDynamics.Logger.info("Chunk at " + chunk.x + " " + chunk.z + " is not checked already");
			}
			Random rand = new Random();
			//Random Chance to spawn a Volcano
			int num = rand.nextInt(100) + 1;
			if(num <= Config.volcanoChance) {
				//Spawn a volcano
				if(Config.genVolcanoDebug) {
					LavaDynamics.Logger.info("VOLCANO!!!");
				}
				//Add chunk to tested Chunks
				data.addTestedChunk(chunk);
				event.getWorld().setBlockState(new BlockPos(x,y,z), LavaDynamics.VolcanoBlock.getDefaultState());
			} else {
				//Add chunk to tested Chunks
				data.addTestedChunk(chunk);
			}
		} else {
			if(Config.genVolcanoDebug) {
				LavaDynamics.Logger.info("Chunk " + chunk.x + " " + chunk.z + " has already been checked");
			}
		}
		timer = Config.volcanoCooldown;
	}

	public static void genVolcano(Chunk chunk, World world) {
		//----------Setup----------//

		boolean debug = Config.genVolcanoDebug;
		Random rand = new Random(world.getSeed());
		//Get the center of the chunk
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();		
		//Get random y level based on config value
		int y = Config.volcanoYLevel + (rand.nextInt(5) - 2);
		//Get center of chunk into BlockPos
		BlockPos center = new BlockPos(x,y,z);

		if(debug) {
			LavaDynamics.Logger.info("Center Location is " + center);
		}

		//----------Lava Lake----------//

		//Generate new Lava lake at chunk center
		WorldGenLakes lakes = new WorldGenLakes(Blocks.LAVA);
		lakes.generate(world, rand, center);
		if(debug) {
			LavaDynamics.Logger.info("Lava lake generated");
		}

		//----------Lava Pillar---------//

		if(debug) {
			LavaDynamics.Logger.info("Generating Lava Pillar");
		}

		//Get hight and set pillar
		int height = chunk.getHeight(new BlockPos(x,y+5,z)) - center.getY();
		if(debug){
			LavaDynamics.Logger.info("Height for Generation is: " + height);
		}

		//Save top value for later. Init with random value
		int topY = 70;

		for(int i = 0; i <= height; i++) {
			//Add starting height in. Cast int cause it was treating them as Strings for some reason
			int j = (int)i + (int)center.getY();
			if(debug) {
				LavaDynamics.Logger.info("Placing Lava at y=" + j);
			}
			//Set Block, which causes block updates to smelt things
			world.setBlockState(new BlockPos(x,j,z), Blocks.MAGMA.getDefaultState());
			//Save TopY
			topY = j;
			//Pause for configured interval, I would use Timer() but I don't know how to set a fixed amount
			//Removed due to it making minecraft hang
			/*try {
						Thread.sleep(Config.timeToRise);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
		}
		if(debug) {
			LavaDynamics.Logger.info("Lava Pillar Done");
		}

		//----------Surface Generation-----------//
		if(debug) {
			LavaDynamics.Logger.info("Generate Volcano");
		}
		//Generate using Ice Spike Generator with edited values
		VolcanoGenerator gen = new VolcanoGenerator();
		gen.generate(world, rand, new BlockPos(x, 255, z));
		if(debug) {
			LavaDynamics.Logger.info("Done Generating. Filling with lava...");
		}
		//Fill the "Volcano" with lava
		BlockPos fill = new BlockPos(x,topY,z);
		while(world.getBlockState(fill).getBlock() != Blocks.LAVA || world.getBlockState(fill).getBlock() != Blocks.AIR || world.getBlockState(fill).getBlock() != Blocks.MAGMA) {
			if(debug) {
				LavaDynamics.Logger.info("Block is " + world.getBlockState(fill).getBlock());
				LavaDynamics.Logger.info("Setting " + fill + " to lava");
			}
			world.setBlockState(fill, Blocks.LAVA.getDefaultState());
			fill = fill.up();
			//Check for air
			if(world.getBlockState(fill).getBlock() == Blocks.AIR){ break; }
			//Cap at world limit, hopefully this should never happen though
			if(fill.getY() >= 255) {
				break;
			}
		}
		
		if(debug) {
			LavaDynamics.Logger.info("Done filling, Spawning crater");
		}
		
		BlockPos fill1 = fill.down(4);
		
		for(int radius = 3;radius != 0;radius--){
			int x1 = fill1.getX();
			int y1 = fill1.getY()-1;
			int z1 = fill1.getZ();
			
			for(float i1 = 0; i1 < radius; i1 += 0.5) {
				for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5)
					world.setBlockState(new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1 + radius, (int)Math.floor(z1 + Math.cos(j1) * i1)), Blocks.LAVA.getDefaultState());
			}
			fill1 = fill1.down();
		}


		//Make our "eruption"
		world.newExplosion(null, fill.getX()-1, fill.getY()-3, fill.getZ(), Config.craterSize, true, true);

		if(debug) {
			LavaDynamics.Logger.info("Done with crater");
		}
		//----------Done?----------//
	}
	
	private static int getDim(String name){
		
		for(int dim : DimensionManager.getIDs()){
			WorldProvider wp = DimensionManager.getProvider(dim);
			if(name.toLowerCase().equals(wp.getDimensionType().getName().toLowerCase())){
				return dim;
			}
		}
		return 0;
	}
}
