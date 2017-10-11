package com.firecontrol.testmodaudio.HandlersA.AudioA;

import javax.annotation.Nullable;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISoundA {

	ResourceLocation getSoundLocation();

	@Nullable
	SoundEventAccessor createAccessor(SoundHandlerA sndHandler);

	Sound getSound();

	SoundCategory getCategory();

	boolean canRepeat();

	int getRepeatDelay();

	float getVolume();

	float getPitch();

	float getXPosF();

	float getYPosF();

	float getZPosF();

	ISoundA.AttenuationType getAttenuationType();

	@SideOnly(Side.CLIENT)
	public static enum AttenuationType {
		NONE(0), LINEAR(2);

		private final int type;

		private AttenuationType(int typeIn) {
			this.type = typeIn;
		}

		public int getTypeInt() {
			return this.type;
		}
	}
}
