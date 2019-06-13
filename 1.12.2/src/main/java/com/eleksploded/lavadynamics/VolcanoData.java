package com.eleksploded.lavadynamics;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class VolcanoData extends WorldSavedData {

	//Good luck with this
	
	//----------Actual saving stuff----------//
	private static String dataName = Reference.MODID + "_VolcanoData";
	static World world;
	public List<Chunk> testedChunks = new ArrayList<Chunk>();
	
	public VolcanoData() {
		super(dataName);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		int[] x = nbt.getIntArray("testedChunkX");
		int[] z = nbt.getIntArray("testedChunkZ");
		
		if(x.length != z.length) {
			throw new RuntimeException("WHAT THE HELL! HOW IS THERE NOT THE SAME AMOUNT OF VALUE. REEEEEEE");
		}
		
		for(int i : x) {
			testedChunks.add(world.getChunkFromChunkCoords(x[i], z[i]));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		List<Integer> x = new ArrayList<Integer>();
		List<Integer> z = new ArrayList<Integer>();
		
		for(Chunk chunk : testedChunks) {
			x.add(chunk.x);
			z.add(chunk.z);
		}
		
		int[] xArray = new int[x.size()];
		int[] zArray = new int[z.size()];
		
		for(int i : x) {
			xArray[i] = x.get(i);
		}
		for(int j : z) {
			zArray[j] = x.get(j);
		}
		
		compound.setIntArray("testedChunkX", xArray);
		compound.setIntArray("testedChunkZ", zArray);
		
		return compound;
	}
	
	//----------Data Access----------/
	
	public static VolcanoData get(World worldIn) {
		world = worldIn;
		MapStorage storage = world.getMapStorage();
		VolcanoData data = (VolcanoData)storage.getOrLoadData(VolcanoData.class, dataName);
		if(data == null) {
			data = new VolcanoData();
		}
		return data;
	}
	
	public void addTestedChunk(Chunk chunk){
		testedChunks.add(chunk);
		this.markDirty();
	}
	
	public boolean isChunkTested(Chunk chunk) {
		if(testedChunks.contains(chunk)) {
			return true;
		} else {
			return false;
		}
	}
}
