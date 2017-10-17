package com.firecontrol.testmodaudio2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.firecontrol.testmodaudio2.AudioA2.CustomMusicTickerA2;
import com.firecontrol.testmodaudio2.Proxy.ICommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ReferenceA2.MOD_ID, name = ReferenceA2.NAME, version = ReferenceA2.VERSION, acceptedMinecraftVersions = ReferenceA2.MCVERSIONS)
public class TestModA2 {

	public static final Logger logger = LogManager.getLogger(ReferenceA2.INITIALS);
	public static CustomMusicTickerA2 musicTicker = null;

	@SidedProxy(clientSide = ReferenceA2.CLIENT_PROXY, serverSide = ReferenceA2.SERVER_PROXY)
	public static ICommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		musicTicker = new CustomMusicTickerA2(Minecraft.getMinecraft());
		ReferenceA2.initiateSongNames();
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		logger.info("Mod Initiated");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}

	public void resetMusicTicker() {
		musicTicker = new CustomMusicTickerA2(Minecraft.getMinecraft());
	}

}
