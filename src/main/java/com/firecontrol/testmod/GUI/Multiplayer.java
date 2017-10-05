package com.firecontrol.testmod.GUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.resources.I18n;

public class Multiplayer extends GuiMultiplayer {

	private static final Logger LOGGER = LogManager.getLogger();
	private ServerSelectionList serverListSelector;
	private ServerList savedServerList;
	private GuiButton btnEditServer;
	private GuiButton btnSelectServer;
	private GuiButton btnDeleteServer;
	private LanServerDetector.LanServerList lanServerList;
	private LanServerDetector.ThreadLanServerFind lanServerDetector;
	private boolean initialized;

	public Multiplayer(GuiScreen parentScreen) {
		super(parentScreen);
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();

		if (this.initialized) {
			this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
		} else {
			this.initialized = true;
			this.savedServerList = new ServerList(this.mc);
			this.savedServerList.loadServerList();
			this.lanServerList = new LanServerDetector.LanServerList();

			try {
				this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
				this.lanServerDetector.start();
			} catch (Exception exception) {
				LOGGER.warn("Unable to start LAN server detection: {}", (Object) exception.getMessage());
			}

			this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, 32,
					this.height - 64, 36);
			this.serverListSelector.updateOnlineServers(this.savedServerList);
		}
		this.createButtons();
	}

	@Override
	public void createButtons() {
		this.btnEditServer = this.addButton(
				new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit")));
		this.btnDeleteServer = this.addButton(
				new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete")));
		this.btnSelectServer = this.addButton(
				new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select")));
		this.buttonList.add(
				new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct")));
		this.buttonList.add(
				new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add")));
		this.buttonList.add(
				new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh")));
		this.buttonList
				.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel")));
		this.selectServer(this.serverListSelector.getSelected());
	}

	@Override
	public void selectServer(int index) {
		this.serverListSelector.setSelectedSlotIndex(index);
		GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null
				: this.serverListSelector.getListEntry(index);
		this.btnSelectServer.enabled = false;
		this.btnEditServer.enabled = false;
		this.btnDeleteServer.enabled = false;

		if (guilistextended$iguilistentry != null
				&& !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
			this.btnSelectServer.enabled = true;

			if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
				this.btnEditServer.enabled = true;
				this.btnDeleteServer.enabled = true;
			}
		}
	}
}
