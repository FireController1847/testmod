package com.firecontrol.testmod.Handlers;

import java.awt.Dimension;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;

import com.firecontrol.testmod.GUI.ResourcePacksCustomGUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.ResourcePackRepository.Entry;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHandler {

	public BlockHandler() {
		System.out.println("initiated");
	}

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) {
		System.out.println("BLOCK BROKEN");
	}

	// @SubscribeEvent
	// public void onGuiOpen(GuiOpenEvent event) {
	// System.out.println("A GUI has been opened!");
	// System.out.println(event.getGui());
	// }

	@SubscribeEvent
	public void onGuiScreen(InitGuiEvent event) {
		if (event.getGui() instanceof GuiMultiplayer) {
			GuiMultiplayer gui = (GuiMultiplayer) event.getGui();
			System.out.println("MPS");
			List<GuiButton> buttons = event.getButtonList();
			buttons.clear();
			int topRow = gui.height - 62;
			int bottomRow = gui.height - 40;
			buttons.add(new GuiButton(0, gui.width / 2 + 101, bottomRow, 53, 20, I18n.format("gui.cancel")));
			buttons.add(new GuiButton(2, gui.width / 2 - 103, bottomRow, 50, 20, I18n.format("selectServer.delete")));
			buttons.add(new GuiButton(1, gui.width / 2 - 154, topRow, 100, 20, I18n.format("selectServer.select")));
			buttons.add(new GuiButton(3, gui.width / 2 + 4 + 50, topRow, 100, 20, I18n.format("selectServer.add")));
			buttons.add(new GuiButton(4, gui.width / 2 - 50, topRow, 100, 20, I18n.format("selectServer.direct")));
			buttons.add(new GuiButton(7, gui.width / 2 - 154, bottomRow, 50, 20, I18n.format("selectServer.edit")));
			buttons.add(new GuiButton(8, gui.width / 2 - 52, bottomRow, 50, 20, I18n.format("selectServer.refresh")));
			buttons.add(new GuiButton(9, gui.width / 2 - 1, bottomRow, 50, 20, "Explode"));
			buttons.add(new GuiButton(10, gui.width / 2 + 50, bottomRow, 50, 20, "Secret"));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onActionPerformed(ActionPerformedEvent.Pre event) {
		if (event.getGui() instanceof GuiOptions && event.getButton().id == 105) {
			GuiOptions gui = (GuiOptions) event.getGui();
			event.setCanceled(true);
			gui.mc.displayGuiScreen(new ResourcePacksCustomGUI(gui));
		} else if (event.getGui() instanceof ResourcePacksCustomGUI) {
			ResourcePacksCustomGUI gui = (ResourcePacksCustomGUI) event.getGui();
			GuiButton button = event.getButton();
			if (button.id != 3 && button.id != 4) return;
			try {
				File file = selectResourcePack();
				if (file == null) return;
				ResourcePackRepository rpr = Minecraft.getMinecraft().getResourcePackRepository();
				IResourcePack pack = processResourcePack(rpr, file);
				Entry entry = createNewEntry(rpr, pack);
				List<Entry> entries = getRepositoryEntriesAll(rpr);
				System.out.println("Loading or unloading pack!");
				System.out.println(entries);
				if (button.id == 3) {
					if (entries.contains(entry)) return;
					entries.add(entry);
					gui.markChanged();
				} else if (button.id == 4) {
					if (entries.contains(entry)) {
						entries.remove(entry);
					}
				}
				System.out.println(entries);
				rpr.setRepositories(entries);
				gui.update = true;
				gui.initGui();
			} catch (ResourcePackFileNotFoundException e) {
				if (e.getMessage().contains("pack.mcmeta")) {
					System.out.println("Invalid resource pack! Missing pack.mcmeta!");
				} else {
					System.out.println(e.hashCode());
					System.out.println(e);
				}
			} catch (Exception e) {
				LogManager.getLogger().error(e);
				StackTraceElement[] stack = e.getStackTrace();
				for (StackTraceElement trace : stack) {
					LogManager.getLogger().error(trace);
				}
			}
		}
	}

	private File selectResourcePack() throws Exception {
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Resource Packs", "zip"));
		fc.setFileSelectionMode(fc.FILES_AND_DIRECTORIES);
		fc.getActionMap().get("viewTypeDetails").actionPerformed(null);
		fc.setPreferredSize(new Dimension(1200, 900));
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		if (fc.showOpenDialog(fc) == fc.APPROVE_OPTION) {
			return fc.getSelectedFile();
		} else {
			return null;
		}
	}

	private IResourcePack processResourcePack(ResourcePackRepository rpr, File file) throws Exception {
		Method m = ResourcePackRepository.class.getDeclaredMethod("getResourcePack", File.class);
		m.setAccessible(true);
		return (IResourcePack) m.invoke(rpr, (file.isDirectory() ? file : new File(file.getAbsolutePath())));
	}
	
	private Entry createNewEntry(ResourcePackRepository rpr, IResourcePack pack) throws Exception {
		Constructor c = Entry.class.getDeclaredConstructor(ResourcePackRepository.class, IResourcePack.class);
		c.setAccessible(true);
		return (Entry) c.newInstance(rpr, pack);
	}
	
	private List<Entry> getRepositoryEntriesAll(ResourcePackRepository rpr) throws Exception {
		Field f = ResourcePackRepository.class.getDeclaredField("repositoryEntriesAll");
		f.setAccessible(true);
		List<Entry> newList = new ArrayList((List<Entry>) f.get(rpr));
		return newList;
	}

}
