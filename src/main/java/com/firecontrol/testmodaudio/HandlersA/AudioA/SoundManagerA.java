package com.firecontrol.testmodaudio.HandlersA.AudioA;

import com.firecontrol.testmodaudio.TestModA;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.settings.GameSettings;

public class SoundManagerA extends SoundManager {
	
	public boolean isNewSoundManager = true;

	public SoundManagerA(SoundHandler p_i45119_1_, GameSettings p_i45119_2_) {
		super(p_i45119_1_, p_i45119_2_);
	}
	
	public void playSound(ISound p_sound) {
		TestModA.logger.info("Playing sound!");
		super.playSound(p_sound);
	}

}
