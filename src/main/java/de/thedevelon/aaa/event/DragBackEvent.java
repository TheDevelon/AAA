package de.thedevelon.aaa.event;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.thedevelon.aaa.bot.Bot;
import de.thedevelon.aaa.main.Main;
import de.thedevelon.aaa.util.OldFileManager;
import de.thedevelon.aaa.util.Util;
import de.thedevelon.aaa.util.config.CheckForBadNotificationConfiguration;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("deprecation")
public class DragBackEvent implements Listener {


	@EventHandler
	public void onDragBack(AntiAuraAPI.DragBackEvent e) {

		Player p = e.getPlayer();
		FileManager bot = new FileManager();
		FileManager strings = new FileManager();
		// load files
		bot.loadFile(FileType.BOT);
		strings.loadFile(FileType.STRINGS);
		// only if config isn't bad
		if (!CheckForBadNotificationConfiguration.badConfig) {
			FileManager config = new FileManager();
			FileManager hover = new FileManager();
			FileManager click = new FileManager();
			FileManager mixed = new FileManager();
			// load files
			config.loadFile(FileType.CONFIG);
			hover.loadFile(FileType.HOVERABLE);
			click.loadFile(FileType.CLICKABLE);
			mixed.loadFile(FileType.COMBINED);

			if (config.getBoolean("Notification.notifyOnDragBack")) {

				// base messages
				String dragBackMsg = Util.replacePlaceholders(strings.getString("Notification.onDragBack"), p, null, e);
				String displayedMsg = Util.replacePlaceholders(hover.getString("DisplayedMessage.onDragBack"), p, null, e);
				String displayedMsgMixed = Util.replacePlaceholders(mixed.getString("DisplayedMessage.onDragBack"), p, null, e);
				String appendedTextPieceHoverEvent = Util.replacePlaceholders(hover.getString("TextPiece.onDragBack"), p, null, e);
				String appendedTextPieceClickEvent = Util.replacePlaceholders(click.getString("TextPiece.onDragBack"), p, null, e);
				String appendedTextPieceMixed = Util.replacePlaceholders(mixed.getString("TextPiece.onDragBack"), p, null, e);

				// with hover event
				if (hover.getBoolean("onDragBack")) {
					// using wholeMessage
					if (hover.getBoolean("WholeMessage.onDragBack")) {
						TextComponent hoverable = new TextComponent(dragBackMsg);
						hoverable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder(displayedMsg).create()));
						Util.getQueue().add(hoverable);
						// send msg
						for (Player staff : Bukkit.getOnlinePlayers()) {
							// has permission?
							if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
								// show?
								FileManager player = new FileManager();
								player.loadPlayerFile(staff);
								if ((boolean) player.returnPlayerValue("Notification.show")
										&& !(boolean) player.returnPlayerValue(("Notification.slow"))) {
									staff.spigot().sendMessage(hoverable);
								}
							}
						}
					} else {
						// using appended text piece
						TextComponent hoverable = new TextComponent(appendedTextPieceHoverEvent);
						hoverable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder(displayedMsg).create()));
						TextComponent prepared = new TextComponent(dragBackMsg);
						prepared.addExtra(" ");
						prepared.addExtra(hoverable);
						Util.getQueue().add(prepared);

						// send msg
						for (Player staff : Bukkit.getOnlinePlayers()) {
							// has permission?
							if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
								// show?
								FileManager player = new FileManager();
								player.loadPlayerFile(staff);
								if ((boolean) player.returnPlayerValue("Notification.show")
										&& !(boolean) player.returnPlayerValue(("Notification.slow"))) {
									staff.spigot().sendMessage(prepared);
								}
							}
						}
					}
				} else {
					// with click event
					if (click.getBoolean("onDragBack")) {
						// using whole message
						if (click.getBoolean("WholeMessage.onDragBack")) {
							TextComponent clickable = new TextComponent(dragBackMsg);
							clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
									Util.replacePlaceholders(click.getString("Command.onDragBack"), p, null, e)));
							Util.getQueue().add(clickable);
							// send msg
							for (Player staff : Bukkit.getOnlinePlayers()) {
								// has permission?
								if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
									// show?
									FileManager player = new FileManager();
									player.loadPlayerFile(staff);
									if ((boolean) player.returnPlayerValue("Notification.show")
											&& !(boolean) player.returnPlayerValue("Notification.slow")) {
										staff.spigot().sendMessage(clickable);
									}
								}
							}
						} else {
							// using appended text piece
							TextComponent clickable = new TextComponent(appendedTextPieceClickEvent);
							clickable.setClickEvent(
									new ClickEvent(ClickEvent.Action.RUN_COMMAND, click.getString("Command.onDragBack")));
							TextComponent prepared = new TextComponent(dragBackMsg);
							prepared.addExtra(" ");
							prepared.addExtra(clickable);
							Util.getQueue().add(prepared);

							// send msg
							for (Player staff : Bukkit.getOnlinePlayers()) {
								// has permission?
								if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
									// show?
									FileManager player = new FileManager();
									player.loadPlayerFile(staff);
									if ((boolean) player.returnPlayerValue("Notification.show")
											&& !(boolean) player.returnPlayerValue("Notification.slow")) {
										staff.spigot().sendMessage(prepared);
									}
								}
							}
						}
					} else {
						// with click event and hover event
						if (mixed.getBoolean("onDragBack")) {
							// using whole message
							if (mixed.getBoolean("WholeMessage.onDragBack")) {
								TextComponent combined = new TextComponent(dragBackMsg);
								combined.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new ComponentBuilder(displayedMsgMixed).create()));
								combined.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
										Util.replacePlaceholders(mixed.getString("Command.onDragBack"), p, null, e)));
								// add to queue for slow mode
								Util.getQueue().add(combined);

								// send msg
								for (Player staff : Bukkit.getOnlinePlayers()) {
									// has permission?
									if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
										// show?
										FileManager player = new FileManager();
										player.loadPlayerFile(staff);
										if ((boolean) player.returnPlayerValue("Notification.show")
												&& !(boolean) player.returnPlayerValue("Notification.slow")) {
											staff.spigot().sendMessage(combined);
										}
									}
								}
							} else {
								// using appended text piece
								TextComponent combined = new TextComponent(appendedTextPieceMixed);
								combined.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new ComponentBuilder(displayedMsgMixed).create()));
								combined.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
										Util.replacePlaceholders(mixed.getString("Command.onDragBack"), p, null, e)));
								TextComponent prepared = new TextComponent(dragBackMsg);
								prepared.addExtra(" ");
								prepared.addExtra(combined);
								Util.getQueue().add(prepared);

								// send msg
								for (Player staff : Bukkit.getOnlinePlayers()) {
									// has permission?
									if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
										// show?
										FileManager player = new FileManager();
										player.loadPlayerFile(staff);
										if ((boolean) player.returnPlayerValue("Notification.show")
												&& !(boolean) player.returnPlayerValue("Notification.slow")) {
											staff.spigot().sendMessage(prepared);
										}
									}
								}
							}
						} else {
							// only send message without event
							TextComponent prepared = new TextComponent(dragBackMsg);
							Util.getQueue().add(prepared);
							// send msg
							for (Player staff : Bukkit.getOnlinePlayers()) {
								// has permission?
								if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
									// show?
									FileManager player = new FileManager();
									player.loadPlayerFile(staff);
									if ((boolean) player.returnPlayerValue("Notification.show")
											&& !(boolean) player.returnPlayerValue("Notification.slow")) {
										staff.spigot().sendMessage(prepared);
									}
								}
							}
						}
					}
				}
			}
		}
		if (bot.getBoolean("activate")) {
			Bot.sendMessageToQueue(Util.replacePlaceholders(strings.getString("Notification.Bot.onDragBack"), p, null, e));
		}
	}

	@SuppressWarnings("unused")
	@Deprecated
	public void startDelayScheduler() {
		OldFileManager fm = new OldFileManager();
		fm.file("settings", "config", ".yml");
		// id for canceling the thread if needed
		int id = 847566970;
		try {
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					for (Player staff : Bukkit.getOnlinePlayers()) {
						if (!Util.getQueue().isEmpty()) {
							if (staff.hasPermission("aaa.notifications") || staff.isOp()) {
								if ((boolean) fm.getValueManual("player-data",
										staff.getUniqueId().toString() + "/" + staff.getUniqueId().toString(), ".yml",
										"Notification.slow")
										&& (boolean) fm.getValueManual("player-data",
												staff.getUniqueId().toString() + "/" + staff.getUniqueId().toString(),
												".yml", "Notification.show")) {
									staff.spigot().sendMessage(Util.getQueue().get(0));
								}
							}
							Util.getQueue().remove(0);
						}
					}
				}
				// 20 * (seconds) = seconds in ticks
			}, 20, (long) (20 * (double) fm.getValue("Notification.delayInSec")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
