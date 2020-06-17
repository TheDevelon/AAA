package de.thedevelon.aaa.event.task;

import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.thedevelon.aaa.util.Util;
import de.thedevelon.aaa.util.filemanager.FileManager;
import net.md_5.bungee.api.chat.TextComponent;

public class Queue extends TimerTask {

	private Timer timer;

	public Queue(Timer timer) {
		this.timer = timer;
	}

	@Override
	public void run() {
		for (Player staff : Bukkit.getOnlinePlayers()) {
			// filemanager for player files
			FileManager player = new FileManager();
			player.loadPlayerFile(staff);
			if (!Util.getQueue().isEmpty()) {
				if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
					if ((boolean) player.returnPlayerValue("Notification.slow")
							&& (boolean) player.returnPlayerValue("Notification.show")) {
						staff.spigot().sendMessage(Util.getQueue().get(0));
					}
				}
				Util.getQueue().remove(0);
			}
		}

	}

	public Timer getTimer() {
		return timer;
	}
}
