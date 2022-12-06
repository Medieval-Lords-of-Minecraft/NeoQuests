package me.neoblade298.neoquests.navigation;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

public class Chunk {
	private static HashMap<Integer, HashMap<Integer, Chunk>> chunkMap = new HashMap<Integer, HashMap<Integer, Chunk>>();
	private int x, z;
	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object comp) {
		if (comp instanceof Chunk) {
			Chunk c = (Chunk) comp;
			return x == c.getX() && z == c.getZ();
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}
	
	public static Chunk getChunk(Location loc) {
		// First convert coords to chunk
		int xc = (int) loc.getX();
		int zc = (int) loc.getZ();
		xc /= 16;
		zc /= 16;
		
		return chunkMap.get(xc).get(zc);
	}
	
	public static Chunk getOrCreateChunk(Location loc) {
		// First convert coords to chunk
		int xc = (int) loc.getX();
		int zc = (int) loc.getZ();
		xc /= 16;
		zc /= 16;
		
		HashMap<Integer, Chunk> xchunkMap = chunkMap.getOrDefault(xc, new HashMap<Integer, Chunk>());
		if (!chunkMap.containsKey(xc)) {
			chunkMap.put(xc, xchunkMap);
		}
		if (!xchunkMap.containsKey(zc)) {
			Chunk chunk = new Chunk(xc, zc);
			xchunkMap.put(zc, chunk);
		}
		return xchunkMap.get(zc);
	}
	
	public static boolean containsKey(Location loc) {
		// First convert coords to chunk
		int xc = (int) loc.getX();
		int zc = (int) loc.getZ();
		xc /= 16;
		zc /= 16;
		
		if (chunkMap.containsKey(xc)) {
			return chunkMap.get(xc).containsKey(zc);
		}
		return false;
	}
	
	private static boolean containsKey(int x, int z) {
		if (chunkMap.containsKey(x)) {
			return chunkMap.get(x).containsKey(z);
		}
		return false;
	}
	
	public static ArrayList<Chunk> getSurroundingChunks(Location loc) {
		// First convert coords to chunk
		int xc = (int) loc.getX();
		int zc = (int) loc.getZ();
		xc /= 16;
		zc /= 16;
		
		ArrayList<Chunk> list = new ArrayList<Chunk>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (containsKey(xc+i, zc+j)) {
					// If chunk doesn't exist, guaranteed no points are in it, don't bother creating them
					list.add(chunkMap.get(xc+i).get(zc+j));
				}
			}
		}
		return list;
	}
}
