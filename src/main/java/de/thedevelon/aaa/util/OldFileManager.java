package de.thedevelon.aaa.util;

import org.bukkit.Bukkit;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

@Deprecated
public class OldFileManager {

	private HashMap<String, File> loaded = new HashMap<>();

	public void file(String dir, String name, String type) {

		loaded.remove("loaded");
		File file = new File(presetDirs(dir) + name + type);
		loaded.put("loaded", file);
	}

	public void createFile(String dir) throws IOException {

		if (!loaded.get("loaded").exists()) {
			File path = new File(presetDirs(dir));
			if (!path.exists()) {
				path.mkdirs();
			}
			boolean created = loaded.get("loaded").createNewFile();

			// TODO: add debug
			if (created) {
				// TODO: add debug
			} else {
			}
		}
	}

	public void setValues(ArrayList<String> paths, ArrayList<Object> values) throws IOException {

		// TODO: add debug
		YamlConfiguration config = YamlConfiguration.loadConfiguration(loaded.get("loaded"));

		if (loaded.get("loaded").exists()) {
			while (values.size() != 0 && paths.size() != 0) {
				// only set values if they are not existing
				if (config.get(paths.get(0)) == null) {
					config.set(paths.get(0), values.get(0));
				}
				values.remove(0);
				paths.remove(0);
			}
			config.save(loaded.get("loaded"));
		} else {
			Bukkit.getLogger().log(Level.WARNING, "[AntiAuraAddon] " + "File does not exist! ("
					+ loaded.get("loaded").getCanonicalPath() + ")\n" + "Cannot continue operation: values not set!");
		}
	}

	// set single value
	public void setValue(String path, Object value) throws IOException {

		// TODO: add debug
		YamlConfiguration config = YamlConfiguration.loadConfiguration(loaded.get("loaded"));

		if (loaded.get("loaded").exists()) {
			if (config.get(path)==null) {
			config.set(path, value);
			config.save(loaded.get("loaded"));
			}
		} else {
			Bukkit.getLogger().log(Level.WARNING, "[AntiAuraAddon] " + "File does not exist! ("
					+ loaded.get("loaded").getCanonicalPath() + ")\n" + "Cannot continue operation: values not set!");
		}
	}

	public void changeValue(String path, Object value) throws IOException {

		// TODO: add debug
		YamlConfiguration config = YamlConfiguration.loadConfiguration(loaded.get("loaded"));

		if (loaded.get("loaded").exists()) {
			config.set(path, value);
			config.save(loaded.get("loaded"));
		} else {
			Bukkit.getLogger().log(Level.WARNING, "[AntiAuraAddon] " + "File does not exist! ("
					+ loaded.get("loaded").getCanonicalPath() + ")\n" + "Cannot continue operation: values not set!");
		}
	}

	public Object getValue(String value) throws IOException {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(loaded.get("loaded"));

		if (loaded.get("loaded").exists()) {
			if (config.get(value) != null) {
				return config.get(value);
			}
		} else {
			Bukkit.getLogger().log(Level.WARNING, "[AntiAuraAddon] " + "File does not exist! (+"
					+ loaded.get("loaded").getCanonicalPath() + ")\n" + "Cannot continue operation: fetched no value");
		}
		return null;
	}
	
	public String getString(Object target) {
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(loaded.get("loaded"));
		if(!Util.isStringNumeric((String) target)) {
			return config.getString((String) target);
		}
		return null;
	}

	public Object getValueManual(String dir, String name, String type, String value) {

		File file = new File(presetDirs(dir) + name + type);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		if (file.exists()) {
			if (config.get(value) != null) {
				return config.get(value);
			}
		} else {
			try {
				Bukkit.getLogger().log(Level.WARNING, "[AntiAuraAddon] " + "File does not exist! (+"
						+ file.getCanonicalPath() + ")\n" + "Cannot continue operation: fetched no value");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public boolean valueExists(String path) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(loaded.get("loaded"));
		
		if(config.get(path) == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public File getFile() {
		return loaded.get("loaded");
	}

	public void header(String header) throws IOException {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(loaded.get("loaded"));
		config.options().header(header);
		config.save(loaded.get("loaded"));
	}

	public boolean fileExists() {
		return loaded.get("loaded").exists();
	}

	private String presetDirs(String dir) {
		switch (dir) {
		case "settings":
			return "plugins/AntiAuraAddon/";
		case "notifications":
			return "plugins/AntiAuraAddon/notifications/";
		case "player-data":
			return "plugins/AntiAuraAddon/data/players/";
		default:
			return dir;
		}
	}
}
