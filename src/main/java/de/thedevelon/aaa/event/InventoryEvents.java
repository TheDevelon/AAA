package de.thedevelon.aaa.event;

import java.io.IOException;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.thedevelon.aaa.util.Util;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;
import net.md_5.bungee.api.ChatColor;

public class InventoryEvents implements Listener {

	public static Inventory select = Bukkit.createInventory(null, 9, "§2Choose file to edit...");
	private Inventory config = Bukkit.createInventory(null, 9, "§2Choose value to edit...");
	private Inventory hoverable = Bukkit.createInventory(null, 9, Util.translateColorCodes("#aChoose file to edit..."));
	private Inventory clickable = Bukkit.createInventory(null, 9, Util.translateColorCodes("#aChoose file to edit..."));
	private Inventory mixed = Bukkit.createInventory(null, 9, Util.translateColorCodes("#aChoose file to edit..."));
	private Inventory bot = Bukkit.createInventory(null, 9, Util.translateColorCodes("#aChoose file to edit..."));

	private static HashMap<Player, String> changeMode = new HashMap<>();

	private static FileManager cfg = new FileManager();
	private static FileManager strings = new FileManager();

	@EventHandler
	public void onInvClick(org.bukkit.event.inventory.InventoryClickEvent e) {

		// stacks and meta for select
		select(e);
		try {
			config(e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void select(org.bukkit.event.inventory.InventoryClickEvent e) {

		Player p = (Player) e.getWhoClicked();
		// load files
		cfg.loadFile(FileType.CONFIG);
		strings.loadFile(FileType.STRINGS);

		if (e.getSlotType() == SlotType.OUTSIDE)
			return;
		if (e.getView().getTitle() == "§2Choose file to edit..." && e.getCurrentItem().hasItemMeta()) {
			if (p.isOp() || p.hasPermission("aaa.inventory.select")) {
				e.setCancelled(true);
				if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals("§2config.yml §a| §eMain configuration file")) {
					p.openInventory(this.config);
				} else {
					if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals("#2hoverable.yml #a| #eConfiguration file for the hover event")) {
						e.setCancelled(true);
						p.openInventory(this.hoverable);
					} else {
						if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals("#2clickable.yml #a| #eConfiguration file for click event")) {
							p.openInventory(this.clickable);
						} else {
							if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals("#2mixed.yml #a| #eConfiguration file for both events combined")) {
								e.setCancelled(true);
								p.openInventory(this.mixed);
							} else {
								if (e.getCurrentItem().getItemMeta().getDisplayName()
										.equals("#2bot.yml #a| #eConfiguration file for the discord bot")) {
									e.setCancelled(true);
									p.openInventory(this.bot);
								}
							}
						}
					}
				}

			} else {
				p.sendMessage(strings.getString("Inventory.noPermissions"));
			}

		}

	}

	private void config(org.bukkit.event.inventory.InventoryClickEvent e) throws IOException {

		Player p = (Player) e.getWhoClicked();
		// load files
		cfg.loadFile(FileType.CONFIG);
		strings.loadFile(FileType.STRINGS);

		if (e.getSlotType() == SlotType.OUTSIDE)
			return;
		if (e.getView().getTitle() == "§2Choose value to edit..." && e.getCurrentItem().hasItemMeta()) {
			if (p.isOp() || p.hasPermission("aaa.inventory.config")) {
				e.setCancelled(true);
				if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals("§7Enable notifications on drag back. §7Current state: §cfalse")) {
					cfg.changeValue("Notification.notifyOnDragBack", true);
					p.sendMessage(Util.replacePlaceholders(strings.getString("Configuration.changedValue"), p, null, null));
					p.closeInventory();
					p.openInventory(this.config);
				} else {
					if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals("§7Disable notifications on drag back. §7Current state: §atrue")) {
						cfg.changeValue("Notification.notifyOnDragBack", false);
						p.sendMessage(
								Util.replacePlaceholders(strings.getString("Configuration.changedValue"), p, null, null));
						p.closeInventory();
						p.openInventory(this.config);
					} else {
						if (e.getCurrentItem().getItemMeta().getDisplayName()
								.contains("§7Set the delay for the slow mode. Current delay:")) {
							changeMode.put(p, "slowmodedelay");
							p.sendMessage(Util.getPrefix() + ChatColor.translateAlternateColorCodes('#',
									"#aTo change the #evalue#a, insert a #enumber #ain the #echat#a."));
							p.closeInventory();
						} else {
							if (e.getCurrentItem().getItemMeta().getDisplayName()
									.contains("§7Set the prefix of the plugin. Current prefix:")) {
								changeMode.put(p, "prefix");
								p.sendMessage(Util.getPrefix() + ChatColor.translateAlternateColorCodes('#',
										"#aTo change the #eplugin prefix#a, write it into the #echat#a.\n"
												+ "#aYou can use your defined #ecolor code character#a."));
								p.closeInventory();
							} else {
								if (e.getCurrentItem().getItemMeta().getDisplayName()
										.contains("§7Set the color code character of the plugin. Current character:")) {
									changeMode.put(p, "ccchar");
									p.sendMessage(Util.getPrefix() + ChatColor.translateAlternateColorCodes('#',
											"#aTo change the #ecolor code character#a, insert a character in the #echat#a."));
									p.closeInventory();
								}
							}
						}
					}
				}
			} else {
				p.sendMessage(strings.getString("Inventory.noPermissions"));
			}
		}
	}

	@EventHandler
	public void openInv(InventoryOpenEvent e) throws IOException {

		Player p = (Player) e.getPlayer();
		// load files
		cfg.loadFile(FileType.CONFIG);
		strings.loadFile(FileType.STRINGS);

		if (e.getView().getTitle() == "§2Choose file to edit...") {
			if (p.isOp() || p.hasPermission("aaa.inventory.select")) {
				ItemStack selectConfig = new ItemStack(Material.PAPER, 1);
				ItemStack selectHoverable = new ItemStack(Material.PAPER, 1);
				ItemStack selectClickable = new ItemStack(Material.PAPER, 1);
				ItemStack selectMixed = new ItemStack(Material.PAPER, 1);
				ItemStack selectBot = new ItemStack(Material.PAPER, 1);

				ItemMeta config = selectConfig.getItemMeta();
				config.setDisplayName(Util.translateColorCodes("§2config.yml §a| §eMain configuration file"));
				selectConfig.setItemMeta(config);

				ItemMeta hoverable = selectHoverable.getItemMeta();
				hoverable.setDisplayName(
						Util.translateColorCodes("§2hoverable.yml §a| §eConfiguration file for the hover event"));
				selectHoverable.setItemMeta(hoverable);

				ItemMeta clickable = selectClickable.getItemMeta();
				clickable.setDisplayName(
						Util.translateColorCodes("§2clickable.yml §a| §eConfiguration file for click event"));
				selectClickable.setItemMeta(clickable);

				ItemMeta mixed = selectMixed.getItemMeta();
				mixed.setDisplayName(
						Util.translateColorCodes("§2mixed.yml §a| §eConfiguration file for both events combined"));
				selectMixed.setItemMeta(mixed);

				ItemMeta bot = selectBot.getItemMeta();
				bot.setDisplayName(Util.translateColorCodes("§2bot.yml §a| §eConfiguration file for the discord bot"));
				selectBot.setItemMeta(bot);

				select.setItem(0, selectConfig);
				// add future items
			} else {
				p.sendMessage(strings.getString("Inventory.noPermissions"));
			}
		} else {
			if (e.getView().getTitle() == "§2Choose value to edit...") {
				if (p.isOp() || p.hasPermission("aaa.inventory.config")) {

					ItemStack notifyOnDragBackTrue = new ItemStack(Material.EMERALD_BLOCK, 1);
					ItemStack notifyOnDragBackFalse = new ItemStack(Material.REDSTONE_BLOCK, 1);
					ItemStack delayInSec = new ItemStack(Material.PAPER, 1);
					ItemStack prefix = new ItemStack(Material.PAPER, 1);
					ItemStack colorCodeChar = new ItemStack(Material.PAPER, 1);

					ItemMeta dragBackTrue = notifyOnDragBackTrue.getItemMeta();
					dragBackTrue.setDisplayName(
							Util.translateColorCodes("§7Disable notifications on drag back. §7Current state: §atrue"));
					notifyOnDragBackTrue.setItemMeta(dragBackTrue);

					ItemMeta dragBackFalse = notifyOnDragBackFalse.getItemMeta();
					dragBackFalse.setDisplayName(
							Util.translateColorCodes("§7Enable notifications on drag back. §7Current state: §cfalse"));
					notifyOnDragBackFalse.setItemMeta(dragBackFalse);

					ItemMeta delay = delayInSec.getItemMeta();
					delay.setDisplayName(Util.translateColorCodes("§7Set the delay for the slow mode. Current delay: §a"
							+ cfg.getInt("Notification.delayInMilliseconds")));
					delayInSec.setItemMeta(delay);

					ItemMeta m_prefix = prefix.getItemMeta();
					m_prefix.setDisplayName(Util.translateColorCodes("§7Set the prefix of the plugin. Current prefix: "
							+ Util.translateColorCodes(cfg.getString("prefix"))));
					prefix.setItemMeta(m_prefix);

					ItemMeta colorCode = colorCodeChar.getItemMeta();
					colorCode.setDisplayName(Util
							.translateColorCodes("§7Set the color code character of the plugin. Current character: §c"
									+ cfg.getString(("colorCodeChar"))));
					colorCodeChar.setItemMeta(colorCode);

					if (cfg.getBoolean("Notification.notifyOnDragBack")) {
						this.config.setItem(0, notifyOnDragBackTrue);
					} else {
						this.config.setItem(0, notifyOnDragBackFalse);
					}

					this.config.setItem(1, delayInSec);
					this.config.setItem(2, prefix);
					this.config.setItem(3, colorCodeChar);
					// add future items
				} else {
					p.sendMessage(String.valueOf(strings.getString("Inventory.noPermissions")));
				}
			}
		}
	}

	public static HashMap<Player, String> getChangeMode() {
		return changeMode;
	}
}
