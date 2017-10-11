package com.firecontrol.testmodaudio;

import java.util.List;

import com.google.common.collect.Lists;

public class ReferenceA {

	public static final String INITIALS = "TMA";
	public static final String MOD_ID = "testmodaudio";
	public static final String NAME = "Test Mod - Audio";
	public static final String VERSION = "1.0.0";
	public static final String MC_VERSIONS = "[1.12,1.13)";
	
	public static Boolean newSong = false;
	public static String currentSongName = "None";
	public static List<String> previousSongs = Lists.<String>newArrayList();
	
}
