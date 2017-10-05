package com.firecontrol.testmod.GUI;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackListEntryServer;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourcePacksCustomGUI extends GuiScreenResourcePacks {

	private final GuiScreen parentScreen;
	private List<ResourcePackListEntry> availableResourcePacks;
	private List<ResourcePackListEntry> selectedResourcePacks;
	private GuiResourcePackAvailable availableResourcePacksList;
	private GuiResourcePackSelected selectedResourcePacksList;
	private boolean changed;
	public boolean update;

	public ResourcePacksCustomGUI(GuiScreen parentScreenIn) {
		super(parentScreenIn);
		this.parentScreen = parentScreenIn;
	}

	public void initGui() {
		
		if (!update) {
			int topRow = 48;
			int bottomRow = 26;
			this.buttonList.add(
					new GuiOptionButton(2, this.width / 2 + 29, this.height - topRow, I18n.format("resourcePack.openFolder")));
			this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 29, this.height - bottomRow, I18n.format("gui.done")));
			this.buttonList.add(new GuiOptionButton(3, this.width /2 - 179, this.height - topRow, "Load External Pack..."));
			this.buttonList.add(new GuiOptionButton(4, this.width /2 - 179, this.height - bottomRow, "Unload External Pack..."));
		}

		if (!this.changed || update) {
			this.availableResourcePacks = Lists.<ResourcePackListEntry>newArrayList();
			this.selectedResourcePacks = Lists.<ResourcePackListEntry>newArrayList();
			ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
			resourcepackrepository.updateRepositoryEntriesAll();
			List<ResourcePackRepository.Entry> list = Lists
					.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
			list.removeAll(resourcepackrepository.getRepositoryEntries());

			for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
				this.availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
			}

			ResourcePackRepository.Entry resourcepackrepository$entry2 = resourcepackrepository.getResourcePackEntry();

			if (resourcepackrepository$entry2 != null) {
				this.selectedResourcePacks
						.add(new ResourcePackListEntryServer(this, resourcepackrepository.getServerResourcePack()));
			}

			for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists
					.reverse(resourcepackrepository.getRepositoryEntries())) {
				this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
			}

			this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
			update = false;
		}

		if (!update) {
			this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height,
					this.availableResourcePacks);
			this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
			this.availableResourcePacksList.registerScrollButtons(7, 8);
			this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, this.height,
					this.selectedResourcePacks);
			this.selectedResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 + 4);
			this.selectedResourcePacksList.registerScrollButtons(7, 8);
		}
	}

	public void handleMouseInput() throws IOException {
		try {
			super.handleMouseInput();
		} catch (Exception e) {}
		this.selectedResourcePacksList.handleMouseInput();
		this.availableResourcePacksList.handleMouseInput();
	}

	public boolean hasResourcePackEntry(ResourcePackListEntry resourcePackEntry) {
		return this.selectedResourcePacks.contains(resourcePackEntry);
	}

	public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry resourcePackEntry) {
		return this.hasResourcePackEntry(resourcePackEntry) ? this.selectedResourcePacks : this.availableResourcePacks;
	}

	public List<ResourcePackListEntry> getAvailableResourcePacks() {
		return this.availableResourcePacks;
	}

	public List<ResourcePackListEntry> getSelectedResourcePacks() {
		return this.selectedResourcePacks;
	}

	public void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == 2) {
				File file1 = this.mc.getResourcePackRepository().getDirResourcepacks();
				OpenGlHelper.openFile(file1);
			} else if (button.id == 1) {
				if (this.changed) {
					List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();

					for (ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks) {
						if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
							list.add(((ResourcePackListEntryFound) resourcepacklistentry).getResourcePackEntry());
						}
					}

					Collections.reverse(list);
					this.mc.getResourcePackRepository().setRepositories(list);
					this.mc.gameSettings.resourcePacks.clear();
					this.mc.gameSettings.incompatibleResourcePacks.clear();

					for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
						this.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());

						if (resourcepackrepository$entry.getPackFormat() != 3) {
							this.mc.gameSettings.incompatibleResourcePacks
									.add(resourcepackrepository$entry.getResourcePackName());
						}
					}

					this.mc.gameSettings.saveOptions();
					this.mc.refreshResources();
				}

				this.mc.displayGuiScreen(this.parentScreen);
			}
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		} catch(Exception e) {}
		this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
		this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
		this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, I18n.format("resourcePack.title"), this.width / 2, 16, 16777215);
		for (GuiButton button : buttonList) {
			button.drawButton(mc, mouseX, mouseY, partialTicks);
		}
	}


	public void markChanged() {
		this.changed = true;
	}

}