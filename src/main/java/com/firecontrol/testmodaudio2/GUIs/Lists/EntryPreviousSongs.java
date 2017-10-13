package com.firecontrol.testmodaudio2.GUIs.Lists;

import java.awt.Color;
import java.util.List;

import com.firecontrol.testmodaudio2.ReferenceA2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class EntryPreviousSongs implements IGuiListEntry {

	protected final Minecraft mc;
	public boolean current;
	private String name;

	public EntryPreviousSongs(String name, boolean currentIn) {
		this.mc = Minecraft.getMinecraft();
		this.name = name;
		this.current = currentIn;
	}

	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected, float partialTicks) {
		if (this.current) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableAlpha();
			Gui.drawRect(x - 1, y - 1, x + listWidth - 9, y + slotHeight + 1,
					(new Color((34F / 255F), (139F / 255F), (34F / 255F), 0.4F)).hashCode());
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);

		String s = this.name;
		String s1 = (this.current ? I18n.format(ReferenceA2.MOD_ID.toLowerCase() + ".mp.current") : "");

		int i1 = this.mc.fontRenderer.getStringWidth(s);
		if (i1 > 105) {
			s = this.mc.fontRenderer.trimStringToWidth(s, 105 - this.mc.fontRenderer.getStringWidth("...")) + "...";
		}
		this.mc.fontRenderer.drawStringWithShadow(s, (float) (x + 32 + 2), (float) (y + 1), 16777215);
		List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(s1, 105);
		for (int l = 0; l < 2 && l < list.size(); ++l) {
			this.mc.fontRenderer.drawStringWithShadow(list.get(l), (float) (x + 32 + 2), (float) (y + 12 + 10 * l),
					8421504);
		}
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		return false;
	}

	@Override
	public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		// TODO Auto-generated method stub
	}

}
