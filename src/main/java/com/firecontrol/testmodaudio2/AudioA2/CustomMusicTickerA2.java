package com.firecontrol.testmodaudio2.AudioA2;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;

public class CustomMusicTickerA2 implements ITickable {

	private final Random rand = new Random();
	private final Minecraft mc;
	public ISound currentSong;
	public boolean isPlaying;
	private int timeUntilNextSong = 100;

	public CustomMusicTickerA2(Minecraft mcIn) {
		this.mc = mcIn;
	}

	@Override
	public void update() {
		MusicTicker.MusicType musicticker$musictype = this.mc.getAmbientMusicType();
		if (this.currentSong != null) {
			if (!musicticker$musictype.getMusicLocation().getSoundName().equals(this.currentSong.getSoundLocation())) {
				this.mc.getSoundHandler().stopSound(this.currentSong);
				this.timeUntilNextSong = MathHelper.getInt(this.rand, 0, musicticker$musictype.getMinDelay() / 2);
			}
			if (!this.mc.getSoundHandler().isSoundPlaying(this.currentSong)) {
				this.currentSong = null;
				this.timeUntilNextSong = Math.min(MathHelper.getInt(this.rand, musicticker$musictype.getMinDelay(),
						musicticker$musictype.getMaxDelay()), this.timeUntilNextSong);
			}
		} else if (this.currentSong == null && isPlaying) {
			this.isPlaying = false;
		}
		this.timeUntilNextSong = Math.min(this.timeUntilNextSong, musicticker$musictype.getMaxDelay());
		if (this.currentSong == null && this.timeUntilNextSong-- <= 0) {
			this.playSong(musicticker$musictype);
		}
	}

	public void playSong(MusicType requestedMusicType, boolean playSafe) {
		if (playSafe && this.currentSong != null)
			return;
		this.currentSong = CustomMusicA2.getMusicRecord(requestedMusicType.getMusicLocation());
		this.mc.getSoundHandler().playSound(this.currentSong);
		this.timeUntilNextSong = Integer.MAX_VALUE;
	}

	public void playSong(MusicType requestedMusicType) {
		playSong(requestedMusicType, true);
	}

}
