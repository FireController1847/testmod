package com.firecontrol.testmodaudio.HandlersA;

import java.nio.file.Paths;
import java.util.List;

import com.firecontrol.testmodaudio.Config;
import com.firecontrol.testmodaudio.ReferenceA;
import com.firecontrol.testmodaudio.TestModA;
import com.firecontrol.testmodaudio.GUIA.GuiAudioManagerA;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
					I18n.format(ReferenceA.MOD_ID.toLowerCase() + ".audio_manager")));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onActionPerformed(ActionPerformedEvent.Pre event) {
		if (event.getGui() instanceof GuiScreenOptionsSounds && event.getButton().id == 247) {
			GuiScreenOptionsSounds gui = (GuiScreenOptionsSounds) event.getGui();
			gui.mc.displayGuiScreen(new GuiAudioManagerA(gui));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onNewSound(SoundSourceEvent event) {
		ISound tSound = event.getSound();
		if (tSound.getCategory() == SoundCategory.MUSIC) {
			Sound sound = event.getSound().getSound();
			if (sound == null) {
				ReferenceA.currentSongName = "None";
				return;
			}
			String file = sound.getSoundLocation().getResourcePath();
			String fileName = Paths.get(file).getFileName().toString();
			String fileNameCap = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
			String fileNameCapSep = fileNameCap.replaceAll("([^\\d-]?)(-?[\\d\\.]+)([^\\d]?)", "$1 $2 $3")
					.replaceAll(" +", " ");
			ReferenceA.currentSongName = fileNameCapSep;
			ReferenceA.previousSongs.add(fileNameCapSep);
			ReferenceA.newSong = true;
		}
	}

	@SubscribeEvent
	public void onSoundPlayed(PlaySoundEvent event) {
		// event.setResultSound(null);
		// TestModA.soundManager.playSound((ISoundA) event.getSound());
	}

}
