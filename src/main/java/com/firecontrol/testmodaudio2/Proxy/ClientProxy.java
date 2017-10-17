package com.firecontrol.testmodaudio2.Proxy;

import com.firecontrol.testmodaudio2.KeybindsA2;
import com.firecontrol.testmodaudio2.TestModA2;
import com.firecontrol.testmodaudio2.AudioA2.CustomMusicTickerA2;
import com.firecontrol.testmodaudio2.Handlers.AudioEventHandlerA2;
import com.firecontrol.testmodaudio2.Handlers.GUIEventHandlerA2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements ICommonProxy {

	public static KeyBinding[] keyBindings;

	@Override
	public void preInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		MinecraftForge.EVENT_BUS.register(new GUIEventHandlerA2());
		MinecraftForge.EVENT_BUS.register(new AudioEventHandlerA2());
		MinecraftForge.EVENT_BUS.register(new KeybindsA2(Minecraft.getMinecraft()));
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub

	}

}
