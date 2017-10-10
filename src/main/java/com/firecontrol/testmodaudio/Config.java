package com.firecontrol.testmodaudio;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {
	
	public static String general = Configuration.CATEGORY_GENERAL;
	
	public static Property flipSkinAndVideo;

	public static Configuration config;
	
	public static void setupConfig() {
		// General
		flipSkinAndVideo = config.get(general, "Flip Skin And Video", true);
		flipSkinAndVideo.setComment("Flips the \"Skin Customization...\" and \"Video Settings...\" buttons in the options menu. Set to false to disable.");
		// Save
		if (config.hasChanged()) {
			config.save();
		}
	}
	
}
