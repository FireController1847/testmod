package com.firecontrol.testmodaudio2.Handlers;

import java.util.List;

import com.firecontrol.testmodaudio2.ReferenceA2;
import com.firecontrol.testmodaudio2.GUIs.GuiScreenAudioManagerA2;
import com.firecontrol.testmodaudio2.GUIs.GuiScreenMusicPlayerA2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GUIEventHandlerA2 {

	@SubscribeEvent
	public void modifySoundsScreen(InitGuiEvent.Post event) {
		if (event.getGui() instanceof GuiScreenOptionsSounds) {
			GuiScreenOptionsSounds gui = (GuiScreenOptionsSounds) event.getGui();
			List<GuiButton> buttons = event.getButtonList();
			for (GuiButton button : buttons) {
				if (button.id == 201) { // Show Subtitles
					button.x = gui.width / 2 + 5;
					button.y -= 24;
				} else if (button.id == 200) { // Done
					button.width = 150;
					button.height = 20;
					button.x -= 55;
				}
			}
			buttons.add(new GuiButton(247, gui.width / 2 + 5, gui.height / 6 + 168, 150, 20,
					I18n.format(ReferenceA2.MOD_ID.toLowerCase() + ".mm")));
		}
	}

	@SubscribeEvent
	public void onActionPerformed(ActionPerformedEvent.Pre event) {
		if (event.getGui() instanceof GuiScreenOptionsSounds && event.getButton().id == 247) {
			GuiScreenOptionsSounds gui = (GuiScreenOptionsSounds) event.getGui();
			gui.mc.displayGuiScreen(new GuiScreenAudioManagerA2(gui));
		}
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiScreenMusicPlayerA2) {
			SoundHandler sh = Minecraft.getMinecraft().getSoundHandler();
			sh.resumeSounds();
		}
	}

}
