package net.simplyrin.afkfpslimit;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by SimplyRin on 2019/02/18.
 *
 * Copyright (c) 2019 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {

	public static final String MODID = "AfkFpsLimitter";
	public static final String VERSION = "1.1";

	private boolean isLimbo;
	private int limitFramerate;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		Minecraft.getMinecraft().gameSettings.limitFramerate = this.getRefreshRate();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String msg = ChatColor.stripColor(event.message.getFormattedText()).trim();

		if (msg.equals("You were spawned in Limbo.") || msg.equals("You are AFK. Move around to return from AFK.")) {
			this.setAfk(true);
		}

		if (msg.contains(" joined the lobby!")) {
			this.setAfk(false);
		}
	}

	public void setAfk(boolean bool) {
		if (this.limitFramerate == 0) {
			this.limitFramerate = this.getRefreshRate();
		}

		if (this.isLimbo == bool) {
			return;
		}
		this.isLimbo = bool;

		if (bool) {
			Minecraft.getMinecraft().gameSettings.limitFramerate = 30;
		} else {
			Minecraft.getMinecraft().gameSettings.limitFramerate = this.limitFramerate;
		}
	}

	public int getRefreshRate() {
		int rate = 60;

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();

		for (int i = 0; i < graphicsDevices.length; i++) {
			DisplayMode dm = graphicsDevices[i].getDisplayMode();

			int refreshRate = dm.getRefreshRate();
			if (refreshRate > rate) {
				rate = refreshRate;
			}
		}
		return rate;
	}

	public void sendMessage(String msg) {
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', msg)));
	}

}
