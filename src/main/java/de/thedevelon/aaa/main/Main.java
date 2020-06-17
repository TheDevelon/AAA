package de.thedevelon.aaa.main;

import java.io.IOException;

import java.util.Timer;
import java.util.logging.Level;

import javax.security.auth.login.LoginException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.thedevelon.aaa.bot.Bot;
import de.thedevelon.aaa.command.AntiAuraAddon;
import de.thedevelon.aaa.event.ChatEvent;
import de.thedevelon.aaa.event.DragBackEvent;
import de.thedevelon.aaa.event.InventoryEvents;
import de.thedevelon.aaa.event.JoinEvent;
import de.thedevelon.aaa.event.ViolationEvent;
import de.thedevelon.aaa.event.task.Queue;
import de.thedevelon.aaa.util.Util;
import de.thedevelon.aaa.util.config.CheckForBadNotificationConfiguration;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {

		FileManager config = new FileManager();
		config.loadFile(FileType.CONFIG);

		getLogger().log(Level.INFO, "Loading AAA v" + Util.returnVersion() + "...");
		try {
			createFiles();

			init();
			this.getLogger().log(Level.INFO, "Enabling repeating tasks... [sync]");

			Timer timer = new Timer();
			int startAfter = 1000;
			// ms * sec = seconds
			int delay = config.getInt("Notification.delayInMilliseconds");
			Queue queue = new Queue(timer);
			timer.schedule(queue, startAfter, delay);

			getLogger().log(Level.INFO, "AntiAuraAddon successfully loaded.");

		} catch (NumberFormatException | IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		// nothing here
	}

	private void init() {
		onLoad();
		CheckForBadNotificationConfiguration.start();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new DragBackEvent(), this);
		pm.registerEvents(new ViolationEvent(), this);
		pm.registerEvents(new JoinEvent(), this);
		pm.registerEvents(new ChatEvent(), this);
		pm.registerEvents(new InventoryEvents(), this);
		// register commands
		getCommand("antiauraaddon").setExecutor(new AntiAuraAddon());

		// init bot
		FileManager botConfig = new FileManager();
		botConfig.loadFile(FileType.BOT);
		Bot bot = new Bot();
		try {
			bot.init();
		} catch (LoginException | IOException e) {
			e.printStackTrace();
		}
		Bot.sendMessageToQueue(String.valueOf(botConfig.getString("welcome")));
	}

	private void createFiles() throws IOException {

		// Create File
		FileManager config = new FileManager();
		config.loadFile(FileType.CONFIG);
		config.createFile();

		// set header
		config.setHeader("AntiAuraAddon - Main configuration file");

		// set values and save
		config.setPaths("Notification.notifyOnDragBack", "Notification.notifyOnViolation",
				"Notification.delayInMilliseconds", "prefix", "colorCodeChar", "debug", "DatePattern", "TimePattern")
				.setValues(true, true, 1, "*?*8[*?*cAAA*?*8] ", '#', false, "yyyy-MM-dd", "HH:mm:ss").apply();
//		paths.add("Notification.notifyOnDragBack");
//		values.add(Boolean.valueOf(true));
//		paths.add("Notification.delayInSec");
//		values.add(Long.valueOf(1));
//		paths.add("prefix");
//		values.add(String.valueOf("#8[#cAAA#8] "));
//		paths.add("colorCodeChar");
//		values.add(Character.valueOf('#'));
//		paths.add("debug");
//		values.add(Boolean.valueOf(false));

		// since version 2
//		paths.add("DatePattern");
//		values.add("yyyy-MM-dd");
//		paths.add("TimePattern");
//		values.add("HH:mm:ss");

		// Create File
		FileManager hoverable = new FileManager();
		hoverable.loadFile(FileType.HOVERABLE);
		hoverable.createFile();

		// set header
		hoverable.setHeader("AntiAuraAddon - Hoverable Notification Messages\n"
				+ "onDragBack :: Should this method be used if a player dragged back?\n"
				+ "wholeMessage :: Use the whole message for the hover event?\n"
				+ "appendTextPiece :: Append an configured text piece at the end of the notification?\n"
				+ "textPiece :: Insert text piece that gets appended to the notification.\n"
				+ "displayedMessage :: Insert message that gets displayed if you hover over the notification.\n"
				+ "More help? Look at the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");

		// set values
		hoverable
				.setPaths("onDragBack", "onViolation", "WholeMessage.onDragBack", "WholeMessage.onViolation",
						"AppendTextPiece.onDragBack", "AppendTextPiece.onViolation", "TextPiece.onDragBack",
						"TextPiece.onViolation", "DisplayedMessage.onDragBack", "DisplayedMessage.onViolation")
				.setValues(false, false, false, false, false, false, "INSERT TEXT PIECE", "INSERT TEXT PIECE",
						"INSERT DISPLAYED MSG", "INSERT DISPLAYED MSG")
				.apply();

		// declare values
//		fm.header("AntiAuraAddon - Hoverable Notification Messages\n"
//				+ "onDragBack :: Should this method be used if a player dragged back?\n"
//				+ "wholeMessage :: Use the whole message for the hover event?\n"
//				+ "appendTextPiece :: Append an configured text piece at the end of the notification?\n"
//				+ "textPiece :: Insert text piece that gets appended to the notification.\n"
//				+ "displayedMessage :: Insert message that gets displayed if you hover over the notification.\n"
//				+ "More help? Look at the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");
//		paths.add("onDragBack");
//		values.add(Boolean.valueOf(false));
//		paths.add("wholeMessage");
//		values.add(Boolean.valueOf(false));
//		paths.add("appendTextPiece");
//		values.add(Boolean.valueOf(false));
//		paths.add("textPiece");
//		values.add(String.valueOf("INSERT TEXT PIECE"));
//		paths.add("displayedMessage");
//		values.add(String.valueOf("INSERT YOUR MESSAGE HERE"));

		// Create File
		FileManager clickable = new FileManager();
		clickable.loadFile(FileType.CLICKABLE);
		clickable.createFile();

		// set header
		clickable.setHeader("AntiAuraAddon - Clickable Notification Messages\n"
				+ "onDragBack :: Should this method be used if a player dragged back?\n"
				+ "wholeMessage :: Use the whole message for the click event?\n"
				+ "appendTextPiece :: Append an configured text piece at the end of the notification?\n"
				+ "textPiece :: Insert text piece that gets appended to the notification.\n"
				+ "command :: Insert command that gets executed if you click on the notification.\n"
				+ "More help? Look at the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");

		// set values and save
		clickable
				.setPaths("onDragBack", "onViolation", "WholeMessage.onDragBack", "WholeMessage.onViolation",
						"AppendTextPiece.onDragBack", "AppendTextPiece.onViolation", "TextPiece.onDragBack",
						"TextPiece.onViolation", "Command.onDragBack", "Command.onViolation")
				.setValues(false, false, false, false, false, false, "INSERT TEXT PIECE HERE", "INSERT TEXT PIECE HERE",
						"/say YAY", "/say YAY")
				.apply();

		// declare values
//		fm.header("AntiAuraAddon - Clickable Notification Messages\n"
//				+ "onDragBack :: Should this method be used if a player dragged back?\n"
//				+ "wholeMessage :: Use the whole message for the click event?\n"
//				+ "appendTextPiece :: Append an configured text piece at the end of the notification?\n"
//				+ "textPiece :: Insert text piece that gets appended to the notification.\n"
//				+ "command :: Insert command that gets executed if you click on the notification.\n"
//				+ "More help? Look at the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");
//		paths.add("onDragBack");
//		values.add(Boolean.valueOf(false));
//		paths.add("wholeMessage");
//		values.add(Boolean.valueOf(false));
//		paths.add("appendTextPiece");
//		values.add(Boolean.valueOf(false));
//		paths.add("textPiece");
//		values.add(String.valueOf("INSERT TEXT PIECE HERE"));
//		paths.add("command");
//		values.add(String.valueOf("INSERT COMMAND"));

		// Create File
		FileManager combined = new FileManager();
		combined.loadFile(FileType.COMBINED);
		combined.createFile();

		// set header
		combined.setHeader("AntiAuraAddon - Mixed Notification Messages (hover + click)\n"
				+ "onDragBack :: Should this method be used if a player dragged back?\n"
				+ "wholeMessage :: Use the whole message for the click event?\n"
				+ "appendTextPiece :: Append an configured text piece at the end of the notification?\n"
				+ "textPiece :: Insert text piece that gets appended to the notification.\n"
				+ "displayedMessage :: Insert message that gets displayed if you hover over the notification.\n"
				+ "command :: Insert command that gets executed if you click on the notification.\n"
				+ "More help? Look at the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");

		// set values and save
		combined.setPaths("onDragBack", "onViolation", "WholeMessage.onDragBack", "WholeMessage.onViolation",
				"AppendTextPiece.onDragBack", "AppendTextPiece.onViolation", "TextPiece.onDragBack",
				"TextPiece.onViolation", "DisplayedMessage.onDragBack","DisplayedMessage.onViolation",
				"Command.onDragBack", "Command.onViolation")
				.setValues(false, false, false, false, false, false, "INSERT TEXT PIECE", "INSERT TEXT PIECE",
						"INSERT DISPLAYED MESSAGE", "INSERT DISPLAYED MESSAGE", "/say YAY", "/say YAY")
				.apply();
		// create mixed.yml
		// declare values
//		fm.header("AntiAuraAddon - Mixed Notification Messages (hover + click)\n"
//				+ "onDragBack :: Should this method be used if a player dragged back?\n"
//				+ "wholeMessage :: Use the whole message for the click event?\n"
//				+ "appendTextPiece :: Append an configured text piece at the end of the notification?\n"
//				+ "textPiece :: Insert text piece that gets appended to the notification.\n"
//				+ "displayedMessage :: Insert message that gets displayed if you hover over the notification.\n"
//				+ "command :: Insert command that gets executed if you click on the notification.\n"
//				+ "More help? Look at the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");
//		paths.add("onDragBack");
//		values.add(Boolean.valueOf(false));
//		paths.add("wholeMessage");
//		values.add(Boolean.valueOf(false));
//		paths.add("appendTextPiece");
//		values.add(Boolean.valueOf(false));
//		paths.add("textPiece");
//		values.add(String.valueOf("INSERT TEXT PIECE"));
//		paths.add("displayedMessage");
//		values.add(String.valueOf("INSERT YOUR MESSAGE HERE"));
//		paths.add("command");
//		values.add(String.valueOf("INSERT COMMAND"));
//		paths.add("config-version");
//		values.add(Integer.valueOf("1"));
		// Create File
		FileManager strings = new FileManager();
		strings.loadFile(FileType.STRINGS);
		strings.createFile();
		
		// set header
		strings.setHeader("AntiAuraAddon - Strings\n" + "Placeholders:\n"
				+ "To get the color code character defined in config.yml use this placeholder: *?*\n"
				+ "Please note: You don't need to use the *?* placeholder, just use the color code character you defined in config.yml.\n"
				+ "Player name = %(player)\n" + "Hack type = %(hack)\n" + "Plugin version = %(version)\n"
				+ "Time stamp = %(timestamp)\n" + "Bot invite link = %(invitelink)\n" + "Next line = %(break)\n" + "\n"
				+ "Please dont use color codes for the bot message string, they have no effect in Discord."
				+ "For more help visit the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");

		// set values and save
		strings.setPaths("Command.createdInviteLink", "Command.botNotActivated", "Command.clientIdEqualsDefault",
				"Command.activatedSlowMode", "Command.deactivatedSlowMode", "Command.displayNotifications",
				"Command.dontDisplayNotifications", "Command.helpMenu", "Command.tooFewArguments",
				"Command.noConsoleSupport", "Command.noPermissions", "Command.tooManyArguments",
				"Notification.onDragBack", "Notification.Bot.onDragBack", "NotificationSettings.added",
				"Error.badConfig", "Command.reload", "Command.unknownArgument", "Command.notificationsNotActivated",
				"Inventory.noPermissions", "Configuration.changedValue", "Notification.onViolation")

				.setValues("%(prefix)*?*aYour invite link: *?*e%(invitelink)*?*a.",
						"%(prefix)*?*cYou did not activate the bot feature!",
						"%(prefix)*?*cYou did not change the client id, didn't you?",
						"%(prefix)*?*aYou activated the slow mode.%(break)*?*aPlease note, you could get older notifications as they are still in the queue.",
						"%(prefix)*?*cYou deactivated the slow mode.",
						"%(prefix)*?*aYou now can see notifications if someone dragged back for cheating.",
						"%(prefix)*?*cYou deactivated the notifications.",
						"%(prefix)*?*aHelp Menu%(break)" + "*?*e/aaa help *?*7| *?*aShows help menu.%(break)"
								+ "*?*e/aaa version *?*7| *?*aShows the current version you are runnign on.%(break)"
								+ "*?*e/aaa toggle *?*7| *?*aToggle if you wanna receive notifications or not.%(break)"
								+ "*?*e/aaa slow *?*7| *?*aThe notifications are coming in slower. Configure the delay in the config.yml.%(break)"
								+ "*?*e/aaa createinvite *?*7| *?*aCreate an invite link for your personal discord bot.",
						"%(prefix)*?*aUse *?*e/aaa help *?*afor help.",
						"%(prefix)This command offers no console support.",
						"%(prefix)*?*cYou don't have the permission to do that!", "%(prefix)*?*Too many arguments.",
						"%(prefix)*?*e%(player) *?*cdragged back for *?*8%(hack)*?*c.",
						"%(player) dragged back for %(hack). %(date) %(timestamp)",
						"%(prefix)*?*e%(player)*?*a, your *?*eplayer file *?*agot updated with new entries.%(break)"
								+ "*?*aYou are now able to see notifications from *?*eAntiAura *?*aprovided by *?*eAntiAuraAddon*?*a.",
						"%(prefix)*?*c*?*lBad configuration! See console for more details.%(break)"
								+ "*?*c*?*lNotifications wont work until you fix the problem.",
						"%(prefix)*?*aConfiguration files successfully reloaded!", "%(prefix)*?*cUnknown argument.",
						"%(prefix)*?*cNotifications are not enabled!",
						"%(prefix)*?*cYou dont have permissions to interact with this inventory.",
						"%(prefix)*?*aYou successfully changed the value!%(break)*?*aDont forget to reload the config with *?*e/aaa reload*?*a.",
						"%(prefix)*?*e%(player) *?*cgot detected for *?*8%(hack)*?*c.")

				.apply();

		// declare values
//		fm.header("AntiAuraAddon - Strings\n" + "Placeholders:\n"
//				+ "To get the color code character defined in config.yml use this placeholder: *?*\n"
//				+ "Please note: You don't need to use the *?* placeholder, just use the color code character you defined in config.yml.\n"
//				+ "Player name = %(player)\n" + "Hack type = %(hack)\n" + "Plugin version = %(version)\n"
//				+ "Time stamp = %(timestamp)\n" + "Bot invite link = %(invitelink)\n" + "Next line = %(break)\n" + "\n"
//				+ "Please dont use color codes for the bot message string, they have no effect in Discord."
//				+ "For more help visit the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");
//		paths.add("Command.createdInviteLink");
//		values.add(String.valueOf("%(prefix)*?*aYour invite link: *?*e%(invitelink)*?*a."));
//		paths.add("Command.botNotActivated");
//		values.add(String.valueOf("%(prefix)*?*cYou did not activate the bot feature!"));
//		paths.add("Command.clientIdEqualsDefault");
//		values.add(String.valueOf("%(prefix)*?*cYou did not change the client id, didn't you?"));
//		paths.add("Command.activatedSlowMode");
//		values.add(String.valueOf(
//				"%(prefix)*?*aYou activated the slow mode.%(break)*?*aPlease note, you could get older notifications as they are still in the queue."));
//		paths.add("Command.deactivatedSlowMode");
//		values.add(String.valueOf("%(prefix)*?*cYou deactivated the slow mode."));
//		paths.add("Command.displayNotifications");
//		values.add(String.valueOf("%(prefix)*?*aYou now can see notifications if someone dragged back for cheating."));
//		paths.add("Command.dontDisplayNotifications");
//		values.add(String.valueOf("%(prefix)*?*cYou deactivated the notifications."));
//		paths.add("Command.helpMenu");
//		values.add(String.valueOf("%(prefix)*?*aHelp Menu%(break)" + "*?*e/aaa help *?*7| *?*aShows help menu.%(break)"
//				+ "*?*e/aaa version *?*7| *?*aShows the current version you are runnign on.%(break)"
//				+ "*?*e/aaa toggle *?*7| *?*aToggle if you wanna receive notifications or not.%(break)"
//				+ "*?*e/aaa slow *?*7| *?*aThe notifications are coming in slower. Configure the delay in the config.yml.%(break)"
//				+ "*?*e/aaa createinvite *?*7| *?*aCreate an invite link for your personal discord bot."));
//		paths.add("Command.tooFewArguments");
//		values.add(String.valueOf("%(prefix)*?*aUse *?*e/aaa help *?*afor help."));
//		paths.add("Command.noConsoleSupport");
//		values.add(String.valueOf("%(prefix)This command offers not console support."));
//		paths.add("Command.noPermissions");
//		values.add(String.valueOf("%(prefix)*?*cYou don't have the permission to do that!"));
//		paths.add("Command.tooManyArguments");
//		values.add(String.valueOf("%(prefix)*?*Too many arguments."));
//		paths.add("Notification.onDragBack");
//		values.add(String.valueOf("%(prefix)*?*e%(player) *?*cdragged back for *?*8%(hack)*?*c."));
//		paths.add("Notification.Bot.onDragBack");
//		values.add(String.valueOf("%(player) dragged back for %(hack). %(date) %(timestamp)"));
//		paths.add("NotificationSettings.added");
//		values.add(String
//				.valueOf("%(prefix)*?*e%(player)*?*a, your *?*eplayer file *?*agot updated with new entrys.%(break)"
//						+ "*?*aYou are now able to see notifications from *?*eAntiAura*?*aprovided by *?*eAntiAuraAddon*?*a."));
//		paths.add("Error.badConfig");
//		values.add(String.valueOf("%(prefix)*?*c*?*lBad configuration! See console for more details.%(break)"
//				+ "*?*c*?*lNotifications wont work until you fix the problem."));

		// since version 2
//		paths.add("Command.reload");
//		values.add(
//				"%(prefix)*?*aConfiguration files successfully reloaded!%(break)*?*cThis does not reload player files!");
//		paths.add("Command.unknownArgument");
//		values.add("%(prefix)*?*cUnknown argument.");
//		paths.add("Command.notificationsNotActivated");
//		values.add("%(prefix)*?*cNotifications are not enabled!");
//		paths.add("Inventory.noPermissions");
//		values.add("%(prefix)*?*cYou dont have permissions to interact with this inventory.");
//		paths.add("Configuration.changedValue");
//		values.add(
//				"%(prefix)*?*aYou successfully changed the value!%(break)*?*aDont forget to reload the config with *?*e/aaa reload*?*a.");

		// since version ...
//		paths.add("Join.notificationsEnabled");
//		values.add("%(prefix)*?*aNotifications enabled!");
//		paths.add("Join.notificationsDisabled");
//		values.add("%(prefix)*?*aNotifications disabled!");

		// Create File
		FileManager bot = new FileManager();
		bot.loadFile(FileType.BOT);
		bot.createFile();

		// set header
		bot.setHeader("AntiAuraAddon - Bot Configuration\n" + "activate :: Do you wanna start the bot upon start?\n"
				+ "token :: Insert your bot token.\n"
				+ "clientid :: Insert your client id, so AAA can create an invite link for your bot.\n"
				+ "permissions :: Insert permission integer to assign permissions to the bot. 8 means the bot has administrator permissions.\n"
				+ "Look at the wiki for help: https://github.com/TheDevelon/antiauraaddon/wiki/Discord-Bridge\n"
				+ "channelid :: Insert the channel id of the channel you prepared for the AAA Bot to send messages.\n"
				+ "activity :: You can write an activity that the bot shows in the task list.\n"
				+ "welcome :: Welcome message on startup.\n"
				+ "repeatInMilliseconds :: Define the delay of how often the bot sends messages into the channel. Works like the slow mode in-game.\n"
				+ "For more help, visit the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");

		// set values and save
		bot.setPaths("activate", "token", "clientid", "permissions", "channelid", "activity", "welcome",
				"repeatInMilliseconds")
				.setValues(false, "insert token here", "insert client id here", 8, 00000000,
						"I am looking for cheaters!", "Bot started!", 2000)
				.apply();
//		fm.header("AntiAuraAddon - Bot Configuration\n" + "activate :: Do you wanna start the bot upon start?\n"
//				+ "token :: Insert your bot token.\n"
//				+ "clientid :: Insert your client id, so AAA can create an invite link for your bot.\n"
//				+ "permissions :: Insert permission integer to assign permissions to the bot. 8 means the bot has administrator permissions.\n"
//				+ "Look at the wiki for help: https://github.com/TheDevelon/antiauraaddon/wiki/Discord-Bridge\n"
//				+ "channelid :: Insert the channel id of the channel you prepared for the AAA Bot to send messages.\n"
//				+ "activity :: You can write an activity that the bot shows in the task list.\n"
//				+ "welcome :: Welcome message on startup.\n"
//				+ "repeatInSec :: Define the delay of how often the bot sends messages into the channel. Works like the slow mode in-game.\n"
//				+ "For more help, visit the wiki: https://github.com/TheDevelon/antiauraaddon/wiki");
//		paths.add("activate");
//		values.add(Boolean.valueOf(false));
//		paths.add("token");
//		values.add(String.valueOf("insert token"));
//		paths.add("clientid");
//		values.add(String.valueOf("INSERT CLIENT ID"));
//		paths.add("permissions");
//		values.add(String.valueOf("8"));
//		paths.add("channelid");
//		values.add(String.valueOf("INSERT CHANNEL ID"));
//		paths.add("activity");
//		values.add(String.valueOf("I'm looking for cheaters!"));
//		paths.add("welcome");
//		values.add(String.valueOf("Bot started!"));
//		paths.add("repeatInSec");
//		values.add(Integer.valueOf(2));

	}

	public static Main instance;

	public static Main getInstance() {
		return instance;
	}

	public void onLoad() {
		instance = this;
	}

}
