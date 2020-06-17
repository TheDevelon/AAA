package de.thedevelon.aaa.util.config;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;

public class CheckForBadNotificationConfiguration {

	public static boolean badConfig;

	public static void start() {

		FileManager hover = new FileManager();
		FileManager click = new FileManager();
		FileManager combined = new FileManager();
		//load files
		hover.loadFile(FileType.HOVERABLE);
		click.loadFile(FileType.CLICKABLE);
		combined.loadFile(FileType.COMBINED);

		// check hoverable.yml
		if (hover.getBoolean("wholeMessage") && hover.getBoolean("appendTextPiece")) {
			Bukkit.getLogger().log(Level.SEVERE,
					"[AntiAuraAddon] " + "Bad configuration! File: hoverable.yml\n" + "\n"
							+ "Boolean 'wholeMessage' and 'appendTextPiece' cannot be both true.\n"
							+ "Please choose only one type. If this error isn't valid please inform the developer.\n"
							+ "As long this error exists, notifications wont work." + "\n" + "End of error report.");
			badConfig = true;
		}
		// check clickable.yml
		if (click.getBoolean("wholeMessage") && click.getBoolean("appendTextPiece")) {
			Bukkit.getLogger().log(Level.SEVERE,
					"[AntiAuraAddon] " + "Bad configuration! File: clickable.yml.\n" + "\n"
							+ "Boolean 'wholeMessage' and 'appendTextPiece' cannot be both true.\n"
							+ "Please choose only one type. If this error isn't valid please inform the developer.\n"
							+ "As long this error exists, notifications wont work." + "\n" + "End of error report.");
			badConfig = true;
		}
		// check mixed.yml
		if (combined.getBoolean("wholeMessage") && combined.getBoolean("appendTextPiece")) {
			Bukkit.getLogger().log(Level.SEVERE,
					"[AntiAuraAddon] " + "Bad configuration! File: mixed.yml.\n" + "\n"
							+ "Boolean 'wholeMessage' and 'appendTextPiece' cannot be both true.\n"
							+ "Please choose only one type. If this error isn't valid please inform the developer.\n"
							+ "As long this error exists, notifications wont work." + "\n" + "End of error report.");
			badConfig = true;
		}

		// check if more as one .yml has onDragBack = true

		boolean hoverOn = hover.getBoolean("onDragBack");
		boolean clickOn = click.getBoolean("onDragBack");
		boolean mixedOn = combined.getBoolean("onDragBack");

		// hover click
		// hover mixed
		// hover mixed click
		// click hover
		// click mixed
		// click mixed hover
		// mixed hover
		// mixed click
		// mixed click hover
		if (hoverOn && clickOn || hoverOn && mixedOn || hoverOn && mixedOn && clickOn || clickOn && hoverOn
				|| clickOn && mixedOn || clickOn && mixedOn && hoverOn || mixedOn && hoverOn || mixedOn && clickOn
				|| mixedOn && clickOn && hoverOn) {
			Bukkit.getLogger().log(Level.SEVERE, "[AntiAuraAddon] " + "Bad configuration!\n" + "\n"
					+ "Value 'onDragBack' is set 'true' too often in directory notifications!\n"
					+ "Only set this value in one file to true, so choose which type you want. If this error isn't valid please inform the developer.\n"
					+ "As long this error exists, notifications wont work." + "\n" + "End of error report.");
			badConfig = true;
		}

		if (!badConfig) {
			Bukkit.getLogger().log(Level.INFO,
					"[AntiAuraAddon] " + "Info: Notification Configurations checked. All should be fine.");
		}
	}
}
