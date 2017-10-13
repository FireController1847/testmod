package com.firecontrol.testmodaudio2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.firecontrol.testmodaudio2.Handlers.AudioEventHandlerA2;
import com.firecontrol.testmodaudio2.Handlers.GUIEventHandlerA2;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ReferenceA2.MOD_ID, name = ReferenceA2.NAME, version = ReferenceA2.VERSION, acceptedMinecraftVersions = ReferenceA2.MCVERSIONS)
public class TestModA2 {

	public static final Logger logger = LogManager.getLogger(ReferenceA2.INITIALS);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new GUIEventHandlerA2());
		MinecraftForge.EVENT_BUS.register(new AudioEventHandlerA2());
		logger.info("Mod Initiated");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
