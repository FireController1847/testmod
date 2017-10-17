package com.firecontrol.testmodaudio2.AudioA2;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class CustomMusicA2 extends PositionedSound implements ISound {

	public CustomMusicA2(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, BlockPos pos) {
		super(soundIn, categoryIn);
	}

	public static CustomMusicA2 getMasterRecord(SoundEvent soundIn, float pitchIn) {
		return getRecord(soundIn, pitchIn, 0.25F);
	}

	public static CustomMusicA2 getRecord(SoundEvent soundIn, float pitchIn, float volumeIn) {
		return new CustomMusicA2(soundIn, SoundCategory.MASTER, volumeIn, pitchIn, false, 0,
				ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
	}

	public static CustomMusicA2 getMusicRecord(SoundEvent soundIn) {
		return new CustomMusicA2(soundIn, SoundCategory.MUSIC, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F,
				0.0F, 0.0F);
	}

	public static CustomMusicA2 getRecordSoundRecord(SoundEvent soundIn, float xIn, float yIn, float zIn) {
		return new CustomMusicA2(soundIn, SoundCategory.RECORDS, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR,
				xIn, yIn, zIn);
	}

	public CustomMusicA2(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, float xIn,
			float yIn, float zIn) {
		this(soundIn, categoryIn, volumeIn, pitchIn, false, 0, ISound.AttenuationType.LINEAR, xIn, yIn, zIn);
	}

	private CustomMusicA2(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, boolean repeatIn,
			int repeatDelayIn, ISound.AttenuationType attenuationTypeIn, float xIn, float yIn, float zIn) {
		this(soundIn.getSoundName(), categoryIn, volumeIn, pitchIn, repeatIn, repeatDelayIn, attenuationTypeIn, xIn,
				yIn, zIn);
	}

	public CustomMusicA2(ResourceLocation soundId, SoundCategory categoryIn, float volumeIn, float pitchIn,
			boolean repeatIn, int repeatDelayIn, ISound.AttenuationType attenuationTypeIn, float xIn, float yIn,
			float zIn) {
		super(soundId, categoryIn);
		this.volume = volumeIn;
		this.pitch = pitchIn;
		this.xPosF = xIn;
		this.yPosF = yIn;
		this.zPosF = zIn;
		this.repeat = repeatIn;
		this.repeatDelay = repeatDelayIn;
		this.attenuationType = attenuationTypeIn;
	}

}
