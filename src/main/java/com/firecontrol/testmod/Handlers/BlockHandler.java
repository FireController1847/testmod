package com.firecontrol.testmod.Handlers;

import java.awt.Dimension;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
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
			for (GuiButton button : event.getButtonList()) {

			}
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
		if (event.getGui() instanceof GuiMultiplayer && event.getButton().id == 9) {
			// JFileChooser fc = new JFileChooser();
			// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// fc.getActionMap().get("viewTypeDetails").actionPerformed(null);
			// fc.setPreferredSize(new Dimension(1200, 900));
			// try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// } catch (Exception e) {
			// }
			// if (fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION) {
			// System.out.println(fc.getSelectedFile().getAbsolutePath());
			// }

			try {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.getActionMap().get("viewTypeDetails").actionPerformed(null);
				fc.setPreferredSize(new Dimension(1200, 900));
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				if (fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION) {
					ResourcePackRepository rpr = Minecraft.getMinecraft().getResourcePackRepository();
					Class cls = ResourcePackRepository.class;
					Method md = cls.getDeclaredMethod("getResourcePack", File.class);
					md.setAccessible(true);
					IResourcePack myNewPack = (IResourcePack) md.invoke(rpr,
							new File("E:\\Users\\FireController1847\\Desktop\\Faithful 1.12.2-rv4.zip"));

					Class cls2 = Entry.class;
					Constructor cn = cls2.getDeclaredConstructor(ResourcePackRepository.class, IResourcePack.class);
					cn.setAccessible(true);
					Entry entry = (Entry) cn.newInstance(rpr, myNewPack);
					System.out.println(entry);

					entry.updateResourcePack();
					// List<Entry> repos = rpr.getRepositoryEntriesAll();
					// repos.add(entry);
					// rpr.setRepositories(repos);

					Field fls = cls.getDeclaredField("repositoryEntriesAll");
					fls.setAccessible(true);
					List<Entry> entries = (List<Entry>) fls.get(rpr);
					System.out.println(entries);
					if (entries.contains(entry)) {
						entries.remove(entry);
					}
					entries.add(entry);
					rpr.setRepositories(entries);
					System.out.println(entries);
					Minecraft.getMinecraft().refreshResources();
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

}
