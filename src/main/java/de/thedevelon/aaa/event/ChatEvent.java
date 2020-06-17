package de.thedevelon.aaa.event;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.thedevelon.aaa.util.Util;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;

public class ChatEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) throws IOException {

		Player p = e.getPlayer();

		if (p.isOp()) {
			if (InventoryEvents.getChangeMode().containsKey(p)) {

				FileManager cfg = new FileManager();
				cfg.loadFile(FileType.CONFIG);

				if (InventoryEvents.getChangeMode().get(p).equals("slowmodedelay")) {
					e.setCancelled(true);
					cfg.changeValue("Notification.delayInMilliseconds", e.getMessage());
					p.sendMessage(Util.getPrefix() + ChatColor.translateAlternateColorCodes('#',
							"#aYou've set the #eslow mode delay #ato #e" + e.getMessage() + " #eseconds#a..\n"
									+ "#cThe changes are only visible after an restart/reload of the server."));
					InventoryEvents.getChangeMode().remove(p);
				} else {
					if (InventoryEvents.getChangeMode().get(p).equals("prefix")) {
						e.setCancelled(true);
						cfg.changeValue("prefix", e.getMessage());
						p.sendMessage(Util.getPrefix()
								+ ChatColor.translateAlternateColorCodes('#', "#aYou've set the #eprefix #ato #9"
										+ Util.replacePlaceholders(e.getMessage(), p, null, null) + "#a."));
						InventoryEvents.getChangeMode().remove(p);
					} else {
						if (InventoryEvents.getChangeMode().get(p).equals("ccchar")) {
							e.setCancelled(true);
							cfg.changeValue("colorCodeChar", e.getMessage());
							p.sendMessage(Util.getPrefix() + ChatColor.translateAlternateColorCodes('#',
									"#aYou've set the #ecolor code character #ato #c" + e.getMessage() + "#a."));
							InventoryEvents.getChangeMode().remove(p);
						}
					}
				}
			}
		}
	}
}
