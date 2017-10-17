package com.firecontrol.testmodaudio2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ReferenceA2 {

	public static final String INITIALS = "TMA2";
	public static final String MOD_ID = "testmodaudio2";
	public static final String NAME = "Test Mod - Audio Two";
	public static final String VERSION = "2.0.0";
	public static final String MCVERSIONS = "[1.12,1.13)";
	public static final String CLIENT_PROXY = "com.firecontrol." + MOD_ID + ".Proxy.ClientProxy";
	public static final String SERVER_PROXY = "com.firecontrol." + MOD_ID + ".Proxy.ClientProxy";
	
	public static Boolean newSong = false;
	public static String currentSongName = "None";
	public static List<String> previousSongs = Lists.<String>newArrayList();
	public static List<String> previousSongsFiles = Lists.<String>newArrayList();

	public static final Map<String, String> SONG_NAMES = new HashMap<String, String>();

	// https://minecraft.gamepedia.com/Music#List
	public static void initiateSongNames() {
		SONG_NAMES.put("11", "11");
		SONG_NAMES.put("13", "Thirteen");
		SONG_NAMES.put("Blocks", "Blocks");
		SONG_NAMES.put("Boss", "Boss");
		SONG_NAMES.put("Calm 1", "Minecraft");
		SONG_NAMES.put("Calm 2", "Clark");
		SONG_NAMES.put("Calm 3", "Sweden");
		SONG_NAMES.put("Cat", "Cat");
		SONG_NAMES.put("Chirp", "Chirp");
		SONG_NAMES.put("Creative 1", "Biome Fest");
		SONG_NAMES.put("Creative 2", "Blind Spots");
		SONG_NAMES.put("Creative 3", "Haunt Muskie");
		SONG_NAMES.put("Creative 4", "Aria Math");
		SONG_NAMES.put("Creative 5", "Dreiton");
		SONG_NAMES.put("Creative 6", "Taswell");
		SONG_NAMES.put("Credits", "Alpha");
		SONG_NAMES.put("End", "The End");
		SONG_NAMES.put("Far", "Far");
		SONG_NAMES.put("Hal 1", "Subwoofer Lullaby");
		SONG_NAMES.put("Hal 2", "Living Mice");
		SONG_NAMES.put("Hal 3", "Haggstrom");
		SONG_NAMES.put("Hal 4", "Danny");
		SONG_NAMES.put("Mall", "Mall");
		SONG_NAMES.put("Mellohi", "Mellohi");
		SONG_NAMES.put("Menu 1", "Mutation");
		SONG_NAMES.put("Menu 2", "Moog City 2");
		SONG_NAMES.put("Menu 3", "Beginning 2");
		SONG_NAMES.put("Menu 4", "Floating Trees");
		SONG_NAMES.put("Nether 1", "Conrete Halls");
		SONG_NAMES.put("Nether 2", "Dead Voxel");
		SONG_NAMES.put("Nether 3", "Warmth");
		SONG_NAMES.put("Nether 4", "Ballad of the Cats");
		SONG_NAMES.put("Nuance 1", "Key");
		SONG_NAMES.put("Nuance 2", "Oxygène");
		SONG_NAMES.put("Piano 1", "Dry Hands");
		SONG_NAMES.put("Piano 2", "Wet Hands");
		SONG_NAMES.put("Piano 3", "Mice on Venus");
		SONG_NAMES.put("Stal", "Stal");
		SONG_NAMES.put("Strad", "Strad");
		SONG_NAMES.put("Wait", "Wait");
		SONG_NAMES.put("Ward", "Ward");
	}
}
