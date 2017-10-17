package com.firecontrol.testmodaudio2.GUIs;

import java.io.IOException;

import com.firecontrol.testmodaudio2.ReferenceA2;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiScreenAudioManagerA2 extends GuiScreen {

	private final GuiScreen parent;
	private String title;

	public GuiScreenAudioManagerA2(GuiScreen parentIn) {
		this.parent = parentIn;
	}

	@Override
	public void initGui() {
		this.title = I18n.format(ReferenceA2.MOD_ID.toLowerCase() + ".mm");
		// Left Side
		this.buttonList.add(new GuiButton(401, this.width / 2 - 155, this.height / 6 - 12, 150, 20,
				I18n.format(ReferenceA2.MOD_ID.toLowerCase() + ".mp")));
		// Right Side
		GuiButton musicAvailableDisabled = new GuiButton(402, this.width / 2 + 5, this.height / 6 - 12, 150, 20,
				I18n.format(ReferenceA2.MOD_ID.toLowerCase() + ".ma"));
		musicAvailableDisabled.enabled = false;
		this.buttonList.add(musicAvailableDisabled);
		this.buttonList.add(new GuiButton(400, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (!button.enabled)
			return;
		if (button.id == 400) {
			this.mc.displayGuiScreen(this.parent);
		} else if (button.id == 401) {
			this.mc.displayGuiScreen(new GuiScreenMusicPlayerA2(this));
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void mouseRelease(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
