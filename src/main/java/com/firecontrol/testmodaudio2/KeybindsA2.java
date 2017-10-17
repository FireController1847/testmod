package com.firecontrol.testmodaudio2;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.firecontrol.testmodaudio2.GUIs.GuiScreenMusicPlayerA2;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeybindsA2 {

	private final Minecraft mc;
	public static List<KeyBinding> keybinds = Lists.<KeyBinding>newArrayList();

	public KeybindsA2(Minecraft mcIn) {
		this.mc = mcIn;
		init();
		register();
	}

	private void init() {
		keybinds.add(0, new KeyBinding("Music Manager", Keyboard.KEY_F10, ReferenceA2.NAME));
	}

	private void register() {
		for (KeyBinding keybind : keybinds) {
			ClientRegistry.registerKeyBinding(keybind);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (keybinds.get(0).isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenMusicPlayerA2(null));
		}
	}

}
