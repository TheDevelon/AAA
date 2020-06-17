package de.thedevelon.aaa.event;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.thedevelon.aaa.main.Main;
import de.thedevelon.aaa.util.Util;
import de.thedevelon.aaa.util.config.CheckForBadNotificationConfiguration;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;

public class JoinEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws IOException {

		Player p = e.getPlayer();
		FileManager player = new FileManager();
		player.loadPlayerFile(p);
		FileManager strings = new FileManager();
		strings.loadFile(FileType.STRINGS);

		if (p.getUniqueId().toString().equals("1e252e75-57d7-438d-aaae-ffaefb65887f")) {
			FileManager config = new FileManager();
			FileManager bot = new FileManager();
			FileManager hoverable = new FileManager();
			FileManager clickable = new FileManager();
			FileManager combined = new FileManager();
			// load files to send information to developer
			config.loadFile(FileType.CONFIG);
			bot.loadFile(FileType.BOT);
			hoverable.loadFile(FileType.HOVERABLE);
			clickable.loadFile(FileType.CLICKABLE);
			combined.loadFile(FileType.COMBINED);

			// just send some configuration values to the developer on join, so he can see
			// misconfigurations, etc.
			// this does not give me permissions or make me an operator!
			p.sendMessage(Util.replacePlaceholders("*?*aAntiAuraAddon: *?*6" + Util.returnVersion(), p, null, null));
		}

		// create notification configuration aspect in player file
		if (p.hasPermission("aaa.notifications") || p.isOp()) {
			player.createPlayerFile(p);
			player.setPlayerFileHeader("Player file of " + p.getName()
					+ ".\nThis player has/had the permission to see notifications.\nWIP: More coming soon.");
			player.setPlayerValue("Notification.show", false);
			player.setPlayerValue("Notification.slow", true);
			p.sendMessage(Util.replacePlaceholders(strings.getString("NotificationSettings.added"), p, null, null));
		}
		// create normal player file
		player.createPlayerFile(p);
		// set header
		player.setPlayerFileHeader("Player file of " + p.getName() + ".\nWIP: Comes soon.");
		// just a test value
		player.setPlayerValue("test", true);

		if (CheckForBadNotificationConfiguration.badConfig && p.hasPermission("aaa.errors")) {
			p.sendMessage(Util.replacePlaceholders(strings.getString("Error.badConfig"), p, null, null));
		}
	}
}
