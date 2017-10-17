package com.firecontrol.testmodaudio2.GUIs.Lists;

import java.util.List;

import com.firecontrol.testmodaudio2.ReferenceA2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class ListPreviousSongs extends GuiListExtended {

	protected final Minecraft mc;
	protected final List<EntryPreviousSongs> entries;

	public ListPreviousSongs(Minecraft mcIn, int widthIn, int heightIn, List<EntryPreviousSongs> entriesIn) {
		super(mcIn, widthIn, heightIn, 15, heightIn - 55, 36);
		this.mc = mcIn;
		this.entries = entriesIn;
		this.centerListVertically = false;
		this.setHasListHeader(true, (int) ((float) mcIn.fontRenderer.FONT_HEIGHT * 1.5F));
	}

	protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn) {
		String s = TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + this.getListHeader();
		this.mc.fontRenderer.drawString(s, insideLeft + this.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2,
				Math.min(this.top + 3, insideTop), 16777215);
	}

	protected String getListHeader() {
		return I18n.format(ReferenceA2.MOD_ID.toLowerCase() + ".mp.prev");
	}

	public List<EntryPreviousSongs> getList() {
		return this.entries;
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return (EntryPreviousSongs) this.getList().get(index);
	}

	@Override
	protected int getSize() {
		return this.getList().size();
	}

	public int getListWidth() {
		return this.width;
	}

	protected int getScrollBarX() {
		return this.right - 6;
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		return super.mouseClicked(mouseX, mouseY, mouseEvent);
	}

	public boolean mouseReleased(int x, int y, int mouseEvent) {
		return super.mouseReleased(x, y, mouseEvent);
	}

}
