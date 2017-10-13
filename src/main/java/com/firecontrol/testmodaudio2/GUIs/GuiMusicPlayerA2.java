package com.firecontrol.testmodaudio2.GUIs;

import java.io.IOException;
import java.util.List;

import com.firecontrol.testmodaudio2.ReferenceA2;
import com.firecontrol.testmodaudio2.GUIs.Lists.EntryPreviousSongs;
import com.firecontrol.testmodaudio2.GUIs.Lists.ListPreviousSongs;
import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiMusicPlayerA2 extends GuiScreen {

	private List<EntryPreviousSongs> previousSongsEntries;
	private ListPreviousSongs previousSongs;
	private final GuiScreen parent;
	private String title;

	public GuiMusicPlayerA2(GuiScreen parentIn) {
		this.parent = parentIn;
	}

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
		// this.buttonList.add(new GuiButton(401, this.width / 2 + 5, this.height / 6 -
		// 12, 150, 20, "Music Available"));
		this.buttonList.add(new GuiButton(400, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
	}
	
	public void updatePreviousEntryList() {
		this.previousSongsEntries.clear();
		this.previousSongsEntries.add(new EntryPreviousSongs("TestSongInital", true));
		for (int i = 0; i < 15; i++) {
			this.previousSongsEntries.add(new EntryPreviousSongs("TestSong" + (i+1), false));
		}
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.previousSongs.handleMouseInput();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (!button.enabled)
			return;
		if (button.id == 400) {
			this.mc.displayGuiScreen(this.parent);
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.previousSongs.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void mouseRelease(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.previousSongs.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
