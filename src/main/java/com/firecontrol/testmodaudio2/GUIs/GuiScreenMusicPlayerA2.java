package com.firecontrol.testmodaudio2.GUIs;

import java.io.IOException;
import java.util.List;

import com.firecontrol.testmodaudio2.ReferenceA2;
import com.firecontrol.testmodaudio2.TestModA2;
import com.firecontrol.testmodaudio2.AudioA2.CustomMusicTickerA2;
import com.firecontrol.testmodaudio2.GUIs.Lists.EntryPreviousSongs;
import com.firecontrol.testmodaudio2.GUIs.Lists.ListPreviousSongs;
import com.google.common.collect.Lists;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiScreenMusicPlayerA2 extends GuiScreen {

	private List<EntryPreviousSongs> previousSongsEntries;
	private ListPreviousSongs previousSongs;
	private final GuiScreen parent;
	private String title;

	public GuiScreenMusicPlayerA2(GuiScreen parentIn) {
		this.parent = parentIn;
	}

	@Override
	public void initGui() {
		this.previousSongsEntries = Lists.<EntryPreviousSongs>newArrayList();
		this.updatePreviousEntryList();
		this.previousSongs = new ListPreviousSongs(mc, 150, this.height, this.previousSongsEntries);
		this.previousSongs.setSlotXBoundsFromLeft(this.width / 6 - 60);
		this.previousSongs.registerScrollButtons(7, 8);
		this.title = I18n.format(ReferenceA2.MOD_ID.toLowerCase() + ".mp");
		// Left Side
		// this.buttonList.add(new GuiButton(401, this.width / 2 - 155, this.height / 6
		// - 12, 150, 20, "Music Player"));
		// Right Side
		this.buttonList.add(new GuiButton(401, this.width / 2 + 5, this.height / 6 - 12, 150, 20, "Stop Current Song"));
		this.buttonList.add(new GuiButton(402, this.width / 2 + 5, this.height / 6 + 12, 150, 20, "Play Another Song"));
		this.buttonList
				.add(new GuiButton(403, this.width / 2 + 5, this.height / 6 + 36, 150, 20, "Force Another Song"));
		this.buttonList
		.add(new GuiButton(404, this.width / 2 + 5, this.height / 6 + 60, 150, 20, "Terminate All Sounds"));
		// this.buttonList.add(new GuiButton(401, this.width / 2 + 5, this.height / 6 -
		// 12, 150, 20, "Music Available"));
		this.buttonList.add(new GuiButton(400, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
	}

	public void updatePreviousEntryList() {
		this.previousSongsEntries.clear();
		int i = 0;
		boolean alreadyGotCurrent = false;
		for (String song : ReferenceA2.previousSongs) {
			boolean isCurrent = song == ReferenceA2.currentSongName;
			this.previousSongsEntries.add(0,
					new EntryPreviousSongs(song, ReferenceA2.previousSongsFiles.get(i),
							(isCurrent && alreadyGotCurrent),
							new ResourceLocation(ReferenceA2.MOD_ID, "textures/music/c418.png")));
			if (isCurrent)
				alreadyGotCurrent = true;
			i++;
		}
		// this.previousSongsEntries.add(new EntryPreviousSongs("TestSongC418", true,
		// new ResourceLocation(ReferenceA2.MOD_ID, "textures/music/c418.png")));
		// this.previousSongsEntries.add(new EntryPreviousSongs("TestSongInital", false,
		// null));
		// for (int i = 0; i < 15; i++) {
		// this.previousSongsEntries.add(new EntryPreviousSongs("TestSong" + (i + 1),
		// false, null));
		// }
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.previousSongs.handleMouseInput();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (!button.enabled)
			return;
		if (button.id == 400) {
			if (this.mc.world != null && this.parent != null) {
				SoundHandler sh = this.mc.getSoundHandler();
				sh.pauseSounds();
			}
			this.mc.displayGuiScreen(this.parent);
		} else if (button.id == 401) {
			SoundHandler sh = this.mc.getSoundHandler();
			sh.stopSound(TestModA2.musicTicker.currentSong);
			// System.out.println(ReferenceA2.SONG_NAMES.get("Menu 4"));
		} else if (button.id == 402) {
			CustomMusicTickerA2 mt = TestModA2.musicTicker;
			mt.playSong(this.mc.getAmbientMusicType());
		} else if (button.id == 403) {
			CustomMusicTickerA2 mt = TestModA2.musicTicker;
			mt.playSong(this.mc.getAmbientMusicType(), false);
		} else if (button.id == 404) {
			SoundHandler sh = this.mc.getSoundHandler();
			sh.stopSounds();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.previousSongs.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void mouseRelease(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		this.previousSongs.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
		if (ReferenceA2.newSong) {
			this.updatePreviousEntryList();
			ReferenceA2.newSong = false;
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

}
