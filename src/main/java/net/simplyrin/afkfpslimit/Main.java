package net.simplyrin.afkfpslimit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.simplyrin.afkfpslimit.commands.CommandSetFps;

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
	public static final String VERSION = "1.2";

	private boolean isLimbo;
	@Setter
	private int limitFramerate = 144;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ClientCommandHandler.instance.registerCommand(new CommandSetFps(this));
		MinecraftForge.EVENT_BUS.register(this);

		this.loadConfig();
	}

	@SubscribeEvent
	public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		Minecraft.getMinecraft().gameSettings.limitFramerate = this.limitFramerate;
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String msg = ChatColor.stripColor(event.message.getFormattedText()).trim();

		if (msg.equals("You were spawned in Limbo.") || msg.equals("You are AFK. Move around to return from AFK.")) {
			this.setAfk(true);
		}

		if (msg.startsWith("[") && (msg.endsWith("joined the lobby!") || msg.endsWith("spooked in the lobby!"))) {
			this.setAfk(false);
		}
	}

	public void setAfk(boolean bool) {
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

	public void sendMessage(String msg) {
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', msg)));
	}

	public void saveConfig() {
		File config = new File("config");
		File file = new File(config, "AfkFpsLimitter.txt");
		if (!file.exists()) {
			try {
				System.out.println("[AfkFpsLimitter-" + VERSION + "] Creating new config file...");
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			FileWriter fileWriter = new FileWriter(file);
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
			printWriter.print(this.limitFramerate);
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadConfig() {
		File config = new File("config");
		File file = new File(config, "AfkFpsLimitter.txt");
		if (!file.exists()) {
			return;
		}

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String line = bufferedReader.readLine();
			bufferedReader.close();

			this.limitFramerate = Integer.valueOf(line).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
