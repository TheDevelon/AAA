package de.thedevelon.aaa.util;

import java.text.DecimalFormatSymbols;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import org.apache.commons.text.StringSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import de.thedevelon.aaa.main.Main;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Util {

	private static ArrayList<TextComponent> queue = new ArrayList<>();

	public static ArrayList<TextComponent> getQueue() {
		return queue;
	}

	public static String returnVersion() {
		PluginDescriptionFile pdf = Objects.requireNonNull(Main.instance.getDescription());
		return pdf.getVersion();
	}

	public static void debug(String msg, Level level) {
		FileManager config = new FileManager();
		// load file
		config.loadFile(FileType.CONFIG);
		if (config.getBoolean("debug")) {
			Bukkit.getLogger().log(level, msg);
		}
	}

	public static String translateColorCodes(String message) {
		FileManager config = new FileManager();
		// load file
		config.loadFile(FileType.CONFIG);

		String raw = config.getString("colorCodeChar");
		char colorCode = raw.charAt(0);
		return ChatColor.translateAlternateColorCodes(colorCode, message);
	}

	public static String getColorCodeChar() {
		FileManager config = new FileManager();
		config.loadFile(FileType.CONFIG);
		return config.getString("colorCodeChar");
	}

	public static String getPrefix() {
		FileManager config = new FileManager();
		config.loadFile(FileType.CONFIG);
		String prepared = replaceColorCodePlacesholders(config.getString("prefix"));
		return translateColorCodes(prepared + " ");
	}

	public static void reloadConfigs() {
		FileManager config = new FileManager();
		FileManager strings = new FileManager();
		FileManager hover = new FileManager();
		FileManager click = new FileManager();
		FileManager combined = new FileManager();
		FileManager bot = new FileManager();
		// load files
		config.loadFile(FileType.CONFIG);
		strings.loadFile(FileType.STRINGS);
		hover.loadFile(FileType.HOVERABLE);
		click.loadFile(FileType.CLICKABLE);
		combined.loadFile(FileType.COMBINED);
		bot.loadFile(FileType.BOT);
		// reload files
		config.reloadFile();
		strings.reloadFile();
		hover.reloadFile();
		click.reloadFile();
		combined.reloadFile();
		bot.reloadFile();
	}

	public static String replacePlaceholders(String input, Player p, AntiAuraAPI.ViolationEvent ev,
			AntiAuraAPI.DragBackEvent ed) {
		FileManager config = new FileManager();
		FileManager bot = new FileManager();
		// load files
		config.loadFile(FileType.CONFIG);
		bot.loadFile(FileType.BOT);
		Map<String, String> values = new HashMap<>();
		// add placeholders
		if (p != null)
			values.put("player", p.getName());
		if (ev != null)
			values.put("hack", ev.getHack());
		if (ed != null)
			values.put("hack", ed.getHack());

		values.put("prefix", getPrefix());
		values.put("version", returnVersion());
		values.put("break", "\n");
		// time / date patterns
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat time = new SimpleDateFormat(config.getString("TimePattern"));
		SimpleDateFormat date = new SimpleDateFormat(config.getString("DatePattern"));
		values.put("timestamp", time.format(cal.getTime()));
		values.put("date", date.format(new Date()));

		if (bot.getBoolean("activate")) {
			StringBuilder builder = new StringBuilder("https://discordapp.com/api/oauth2/authorize?client_id=");
			builder.append(bot.getString("clientid"));
			builder.append("&scope=bot&permissions=");
			builder.append(bot.getString("permissions"));
			String prepared = builder.toString();
			values.put("invitelink", prepared);
		}

		StringSubstitutor sub = new StringSubstitutor(values, "%(", ")");
		String prepared = replaceColorCodePlacesholders(sub.replace(input));
		return translateColorCodes(sub.replace(prepared));
	}

	public static boolean isStringNumeric(String str) {
		DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
		char localeMinusSign = currentLocaleSymbols.getMinusSign();

		if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign)
			return false;

		boolean isDecimalSeparatorFound = false;
		char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

		for (char c : str.substring(1).toCharArray()) {
			if (!Character.isDigit(c)) {
				if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
					isDecimalSeparatorFound = true;
					continue;
				}
				return false;
			}
		}
		return true;
	}

	private static String replaceColorCodePlacesholders(String input) {
		Map<String, String> values = new HashMap<>();
		values.put("?", Util.getColorCodeChar());
		StringSubstitutor sub = new StringSubstitutor(values, "*", "*");
		return sub.replace(input);
	}
}