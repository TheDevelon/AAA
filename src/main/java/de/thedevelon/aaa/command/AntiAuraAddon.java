package de.thedevelon.aaa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.thedevelon.aaa.event.InventoryEvents;
import de.thedevelon.aaa.util.Util;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;
import net.md_5.bungee.api.ChatColor;

public class AntiAuraAddon implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args) {

		FileManager config = new FileManager();
		FileManager strings = new FileManager();
		FileManager bot = new FileManager();
		//load files
		config.loadFile(FileType.CONFIG);
		strings.loadFile(FileType.STRINGS);
		bot.loadFile(FileType.BOT);

		if (cs instanceof Player) {
			Player p = (Player) cs;
			if (args.length == 0) {
				if (p.hasPermission("aaa.command.help") || p.isOp()) {
					p.sendMessage(Util.replacePlaceholders(strings.getString("Command.tooFewArguments"), p, null, null));
				} else {
					p.sendMessage(Util.replacePlaceholders(strings.getString("Command.noPermissions"), p, null, null));
				}
			} else {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						if (p.hasPermission("aaa.command.help") || p.isOp()) {
							p.sendMessage(Util.replacePlaceholders(strings.getString("Command.helpMenu"), p, null, null));
						} else {
							p.sendMessage(
									Util.replacePlaceholders(strings.getString("Command.noPermissions"), p, null, null));
						}
					} else {
						if (args[0].equalsIgnoreCase("version")) {
							if (p.hasPermission("aaa.command.version") || p.isOp()) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('#', Util.getPrefix()
										+ "#aYou are running on version #e" + Util.returnVersion() + " #a."));
							} else {
								p.sendMessage(
										Util.replacePlaceholders(strings.getString("Command.noPermissions"), p, null, null));
							}
						} else {
							if (args[0].equalsIgnoreCase("toggle")) {
								if (p.hasPermission("aaa.command.toggle") || p.isOp()) {
									if (config.getBoolean("Notification.notifyOnDragBack")) {
										FileManager player = new FileManager();
										player.loadPlayerFile(p);
										if ((boolean) player.returnPlayerValue("Notification.show")) {
											player.changePlayerValue("Notification.show", false);
											p.sendMessage(Util.replacePlaceholders(
													strings.getString("Command.dontDisplayNotifications"), p, null, null));
										} else {
											player.changePlayerValue("Notification.show", true);
											p.sendMessage(Util.replacePlaceholders(
													strings.getString("Command.displayNotifications"), p, null, null));
										}
									} else {
										p.sendMessage(Util.replacePlaceholders(
												String.valueOf(strings.getString("Command.notificationsNotActivated")),
												p, null, null));
									}
								} else {
									p.sendMessage(Util.replacePlaceholders(strings.getString("Command.noPermissions"),
											p, null, null));
								}
							} else {
								if (args[0].equalsIgnoreCase("slow")) {
									if (p.hasPermission("aaa.command.slowmode") || p.isOp()) {
										if (config.getBoolean("Notification.notifyOnDragBack")) {
											FileManager player = new FileManager();
											player.loadPlayerFile(p);
											if ((boolean) player.returnPlayerValue("Notification.slow")) {
												p.sendMessage(Util.replacePlaceholders(
														strings.getString("Command.deactivatedSlowMode"), p, null, null));
												player.changePlayerValue("Notification.slow", false);
											} else {
												p.sendMessage(Util.replacePlaceholders(
														strings.getString("Command.activatedSlowMode"), p, null, null));
												player.changePlayerValue("Notification.slow", true);
											}
										} else {
											p.sendMessage(Util.replacePlaceholders(
													strings.getString("Command.notificationsNotActivated"), p, null, null));
										}
									} else {
										p.sendMessage(Util.replacePlaceholders(
												strings.getString("Command.noPermissions"), p, null, null));
									}
								} else {
									if (args[0].equalsIgnoreCase("createinvite")) {
										if (p.hasPermission("aaa.command.createinvite") || p.isOp()) {
											if (bot.getBoolean("activate")) {
												if (!bot.getString("clientid").equals("insert client id here")) {
													p.sendMessage(Util.replacePlaceholders(
															strings.getString("Command.createdInviteLink"), p, null, null));
												} else {
													p.sendMessage(Util.replacePlaceholders(
															strings.getString("Command.clientIdEqualsDefault"), p,
															null, null));
												}
											} else {
												p.sendMessage(Util.replacePlaceholders(
														strings.getString("Command.botNotActivated"), p, null, null));
											}
										} else {
											p.sendMessage(Util.replacePlaceholders(
													strings.getString("Command.noPermissions"), p, null, null));
										}
									} else {
										if (args[0].equalsIgnoreCase("reload")) {
											if (p.hasPermission("aaa.command.reload") || p.isOp()) {
												Util.reloadConfigs();
												p.sendMessage(String.valueOf(Util.replacePlaceholders(
														strings.getString("Command.reload"), p, null, null)));
											} else {
												p.sendMessage(Util.replacePlaceholders(
														strings.getString("Command.noPermissions"), p, null, null));
											}
										} else {
											if (args[0].equalsIgnoreCase("configure")) {
												if (p.isOp() || p.hasPermission("aaa.command.configure")) {

													p.openInventory(InventoryEvents.select);

												} else {
													p.sendMessage(Util.replacePlaceholders(
															String.valueOf(strings.getString("Command.noPermissions")),
															p, null, null));
												}
											} else {
												p.sendMessage(Util.replacePlaceholders(
														strings.getString("Command.unknownArgument"), p, null, null));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else {
			Util.replacePlaceholders(strings.getString("Command.noConsoleSupport"), null, null, null);
		}

		return false;

	}

}
