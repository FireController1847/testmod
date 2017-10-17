package com.firecontrol.testmodaudio2.Handlers;

import java.nio.file.Paths;

import com.firecontrol.testmodaudio2.ReferenceA2;
import com.firecontrol.testmodaudio2.TestModA2;
import com.firecontrol.testmodaudio2.AudioA2.CustomMusicA2;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AudioEventHandlerA2 {

	@SubscribeEvent
	public void onNewSound(SoundSourceEvent event) {
		ISound tSound = event.getSound();
		if (tSound.getCategory() != SoundCategory.MUSIC)
			return;
		Sound sound = event.getSound().getSound();
		if (sound == null) {
			ReferenceA2.currentSongName = "None";
			return;
		}
		String path = sound.getSoundLocation().getResourcePath();
		String fileName = Paths.get(path).getFileName().toString();
		String fileNameCap = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
		String fileNameCapSep = fileNameCap.replaceAll("([^\\d-]?)(-?[\\d\\.]+)([^\\d]?)", "$1 $2 $3").replaceAll(" +",
				" ");
		String songName = fileNameCapSep.substring(0, fileNameCapSep.length() - 1);
		String songFileName = songName;
		if (ReferenceA2.SONG_NAMES.containsKey(songName)) {
			songName = ReferenceA2.SONG_NAMES.get(songName);
		}
		ReferenceA2.currentSongName = songName;
		ReferenceA2.previousSongs.add(songName);
		ReferenceA2.previousSongsFiles.add(songFileName);
		ReferenceA2.newSong = true;
	}

	@SubscribeEvent
	public void onSoundPlayed(PlaySoundEvent event) {
		if (TestModA2.musicTicker == null)
			return;
		if (event.getSound().getCategory() == SoundCategory.MUSIC) {
			if (!(event.getSound() instanceof CustomMusicA2)) {
				event.setResultSound(null);
			}
		}
	}

	@SubscribeEvent
	public void onGameTick(ClientTickEvent event) {
		if (TestModA2.musicTicker == null)
			return;
		TestModA2.musicTicker.update();
	}
}
