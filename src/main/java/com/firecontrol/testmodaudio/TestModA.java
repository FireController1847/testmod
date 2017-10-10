package com.firecontrol.testmodaudio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.firecontrol.testmodaudio.HandlersA.GUIEventHandlerA;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ReferenceA.MOD_ID, name = ReferenceA.NAME, version = ReferenceA.VERSION, acceptedMinecraftVersions = ReferenceA.MC_VERSIONS)
public class TestModA {

	public static final Logger logger = LogManager.getLogger(ReferenceA.INITIALS);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.config = new Configuration(event.getSuggestedConfigurationFile());
		Config.config.load();
		Config.setupConfig();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new GUIEventHandlerA());
		logger.info("Mod Initiated");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
