package com.firecontrol.testmodaudio.HandlersA;

import java.util.List;

import com.firecontrol.testmodaudio.Config;
import com.firecontrol.testmodaudio.TestModA;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GUIEventHandlerA {

	public GUIEventHandlerA() {
		TestModA.logger.info("GUIEventHandler Initiated");
	}

	@SubscribeEvent
	public void modifyOptionsScreen(InitGuiEvent.Post event) {
		if (event.getGui() instanceof GuiOptions) {
			GuiOptions gui = (GuiOptions) event.getGui();
			TestModA.logger.info("Gui is Options");
			List<GuiButton> buttons = event.getButtonList();
			for (GuiButton button : buttons) {
				if (Config.flipSkinAndVideo.getBoolean()) {
					if (button.id == 110) { // Skin Customization
						button.y += 24;
					} else if (button.id == 101) { // Video Settings
						button.y -= 24;
					}
				}
			}
		} else if (event.getGui() instanceof GuiScreenOptionsSounds) {
			GuiScreenOptionsSounds gui = (GuiScreenOptionsSounds) event.getGui();
			TestModA.logger.info("Gui is OptionsSounds");
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
					I18n.format("options.testmodaudio.settings")));
		}
	}

}
