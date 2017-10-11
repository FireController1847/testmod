package com.firecontrol.testmodaudio.HandlersA.AudioA;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PositionedSoundRecordA extends PositionedSoundA {
	public PositionedSoundRecordA(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn,
			BlockPos pos) {
		this(soundIn, categoryIn, volumeIn, pitchIn, (float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F,
				(float) pos.getZ() + 0.5F);
	}

	public static PositionedSoundRecordA getMasterRecord(SoundEvent soundIn, float pitchIn) {
		return getRecord(soundIn, pitchIn, 0.25F);
	}

	public static PositionedSoundRecordA getRecord(SoundEvent soundIn, float pitchIn, float volumeIn) {
		return new PositionedSoundRecordA(soundIn, SoundCategory.MASTER, volumeIn, pitchIn, false, 0,
				ISoundA.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
	}

	public static PositionedSoundRecordA getMusicRecord(SoundEvent soundIn) {
		return new PositionedSoundRecordA(soundIn, SoundCategory.MUSIC, 1.0F, 1.0F, false, 0,
				ISoundA.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
	}

	public static PositionedSoundRecordA getRecordSoundRecord(SoundEvent soundIn, float xIn, float yIn, float zIn) {
		return new PositionedSoundRecordA(soundIn, SoundCategory.RECORDS, 4.0F, 1.0F, false, 0,
				ISoundA.AttenuationType.LINEAR, xIn, yIn, zIn);
	}

	public PositionedSoundRecordA(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn,
			float xIn, float yIn, float zIn) {
		this(soundIn, categoryIn, volumeIn, pitchIn, false, 0, ISoundA.AttenuationType.LINEAR, xIn, yIn, zIn);
	}

	private PositionedSoundRecordA(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn,
			boolean repeatIn, int repeatDelayIn, ISoundA.AttenuationType attenuationTypeIn, float xIn, float yIn,
			float zIn) {
		this(soundIn.getSoundName(), categoryIn, volumeIn, pitchIn, repeatIn, repeatDelayIn, attenuationTypeIn, xIn,
				yIn, zIn);
	}

	public PositionedSoundRecordA(ResourceLocation soundId, SoundCategory categoryIn, float volumeIn, float pitchIn,
			boolean repeatIn, int repeatDelayIn, ISoundA.AttenuationType attenuationTypeIn, float xIn, float yIn,
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
