package com.firecontrol.testmodaudio.GUIA;

import java.io.IOException;
import java.util.List;

import com.firecontrol.testmodaudio.MusicList;
import com.firecontrol.testmodaudio.MusicListEntry;
import com.firecontrol.testmodaudio.ReferenceA;
import com.firecontrol.testmodaudio.TestModA;
import com.google.common.collect.Lists;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiAudioManager extends GuiScreen {

	private final GuiScreen parent;
	private List<MusicListEntry> songListEntries;
	private MusicList songList;
	protected String title = "Audio Manager";

	public GuiAudioManager(GuiScreen parentIn) {
		this.parent = parentIn;
	}

	public void initGui() {
		this.songListEntries = Lists.<MusicListEntry>newArrayList();
		this.updateSongList();
		this.songList = new MusicList(mc, 150, this.height, this.songListEntries);
		this.songList.setSlotXBoundsFromLeft(this.width / 6 - 60);
		this.songList.registerScrollButtons(7, 8);
		this.title = I18n.format(ReferenceA.MOD_ID.toLowerCase() + ".audio_manager");
		this.buttonList.add(new GuiButton(402, this.width / 2 - 75, this.height / 6, 150, 20, "Stop All Sounds"));
		this.buttonList.add(new GuiButton(401, this.width / 2 - 75, this.height / 6 + 128, 150, 20, "Testing"));
		this.buttonList.add(new GuiButton(400, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.songList.handleMouseInput();
	}

	public void updateSongList() {
		this.songListEntries.clear();
		for (String song : ReferenceA.previousSongs) {
			this.songListEntries.add(0, new MusicListEntry(song, (song == ReferenceA.currentSongName)));
		}
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (!button.enabled)
			return;
		if (button.id == 400) {
			this.mc.displayGuiScreen(this.parent);
		}
		if (button.id == 401) {
			TestModA.logger.info("Button 401");
			SoundHandler sh = this.mc.getSoundHandler();
			MusicTicker mt = this.mc.getMusicTicker();
			sh.stopSounds();
			mt.playMusic(MusicType.MENU);
		}
		if (button.id == 402) {
			TestModA.logger.info("Button 402");
			SoundHandler sh = this.mc.getSoundHandler();
			MusicTicker mt = this.mc.getMusicTicker();
			sh.stopSounds();
			ReferenceA.currentSongName = "None";
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.songList.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void mouseRelease(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		this.songList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
		for (GuiButton button : this.buttonList) {
			if (button.id == 401) {
				button.displayString = ReferenceA.currentSongName;
			}
		}
		if (ReferenceA.newSong) {
			this.updateSongList();
			ReferenceA.newSong = false;
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
