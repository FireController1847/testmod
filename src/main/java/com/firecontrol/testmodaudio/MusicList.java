package com.firecontrol.testmodaudio;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class MusicList extends GuiListExtended {

	protected final Minecraft mc;
	protected final List<MusicListEntry> musicListEntires;

	public MusicList(Minecraft mcIn, int widthIn, int heightIn, List<MusicListEntry> entries) {
		super(mcIn, widthIn, heightIn, 15, heightIn - 15, 36);
		this.mc = mcIn;
		this.musicListEntires = entries;
		this.centerListVertically = false;
		this.setHasListHeader(true, (int) ((float) mcIn.fontRenderer.FONT_HEIGHT * 1.5F));
	}

	protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn) {
		String s = TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + this.getListHeader();
		this.mc.fontRenderer.drawString(s, insideLeft + this.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2,
				Math.min(this.top + 3, insideTop), 16777215);
	}

	protected String getListHeader() {
		return I18n.format(ReferenceA.MOD_ID.toLowerCase() + ".audio_manager.oldsongs");
	}

	public List<MusicListEntry> getList() {
		return this.musicListEntires;
	}

	protected int getSize() {
		return this.getList().size();
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		if (this.isMouseYWithinSlotBounds(mouseY)) {
			int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);

			if (i >= 0) {
				int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
				int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
				int l = mouseX - j;
				int i1 = mouseY - k;

				if (this.getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i1)) {
					this.setEnabled(false);
					return true;
				}
			}
		}

		return false;
	}

	public boolean mouseReleased(int x, int y, int mouseEvent) {
		return super.mouseReleased(x, y, mouseEvent);
	}

	public IGuiListEntry getListEntry(int index) {
		return (MusicListEntry) this.getList().get(index);
	}

	public int getListWidth() {
		return this.width;
	}

	protected int getScrollBarX() {
		return this.right - 6;
	}

}
