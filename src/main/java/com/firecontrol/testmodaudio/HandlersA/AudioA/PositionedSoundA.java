package com.firecontrol.testmodaudio.HandlersA.AudioA;

import javax.annotation.Nullable;

import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class PositionedSoundA implements ISoundA {

	protected Sound sound;
	@Nullable
	private SoundEventAccessor soundEvent;
	protected SoundCategory category;
	protected ResourceLocation positionedSoundLocation;
	protected float volume;
	protected float pitch;
	protected float xPosF;
	protected float yPosF;
	protected float zPosF;
	protected boolean repeat;
	/** The number of ticks between repeating the sound */
	protected int repeatDelay;
	protected ISoundA.AttenuationType attenuationType;

	protected PositionedSoundA(SoundEvent soundIn, SoundCategory categoryIn) {
		this(soundIn.getSoundName(), categoryIn);
	}

	protected PositionedSoundA(ResourceLocation soundId, SoundCategory categoryIn) {
		this.volume = 1.0F;
		this.pitch = 1.0F;
		this.attenuationType = ISoundA.AttenuationType.LINEAR;
		this.positionedSoundLocation = soundId;
		this.category = categoryIn;
	}

	public ResourceLocation getSoundLocation() {
		return this.positionedSoundLocation;
	}

	public SoundEventAccessor createAccessor(SoundHandlerA handler) {
		this.soundEvent = handler.getAccessor(this.positionedSoundLocation);

		if (this.soundEvent == null) {
			this.sound = SoundHandlerA.MISSING_SOUND;
		} else {
			this.sound = this.soundEvent.cloneEntry();
		}

		return this.soundEvent;
	}

	public Sound getSound() {
		return this.sound;
	}

	public SoundCategory getCategory() {
		return this.category;
	}

	public boolean canRepeat() {
		return this.repeat;
	}

	public int getRepeatDelay() {
		return this.repeatDelay;
	}

	public float getVolume() {
		return this.volume * this.sound.getVolume();
	}

	public float getPitch() {
		return this.pitch * this.sound.getPitch();
	}

	public float getXPosF() {
		return this.xPosF;
	}

	public float getYPosF() {
		return this.yPosF;
	}

	public float getZPosF() {
		return this.zPosF;
	}

	public ISoundA.AttenuationType getAttenuationType() {
		return this.attenuationType;
	}

}
