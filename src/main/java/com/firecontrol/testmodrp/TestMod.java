package com.firecontrol.testmodrp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.firecontrol.testmodrp.Handlers.BlockHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MC_VERSIONS)
public class TestMod {

	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Mod Initiated");
		// System.out.println("Mod initiated");
		MinecraftForge.EVENT_BUS.register(new BlockHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
