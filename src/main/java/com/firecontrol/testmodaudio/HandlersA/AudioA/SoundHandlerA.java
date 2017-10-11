package com.firecontrol.testmodaudio.HandlersA.AudioA;

import com.firecontrol.testmodaudio.TestModA;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;

public class SoundHandlerA extends SoundHandler {

	public SoundHandlerA(IResourceManager manager, GameSettings gameSettingsIn) {
		super(manager, gameSettingsIn);
	}

}
