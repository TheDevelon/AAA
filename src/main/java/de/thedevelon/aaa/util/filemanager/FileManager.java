package de.thedevelon.aaa.util.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import de.thedevelon.aaa.main.Main;

/**
 * @author Florian
 *
 */
public class FileManager {

	private Main plugin = Main.getInstance();

	private FileType type;
	private String[] paths;
	private Object[] values;
	private static File player;

	public FileManager loadFile(FileType file) {
		Validate.notNull(file, "FileType cannot be null");
		type = file;
		return this;
	}

	public void createFile() {
		File f = type.getFile();
		File path;

		if (!f.exists()) {
			switch (type) {
			case CONFIG:
				path = new File("plugins/AntiAuraAddon");
				break;
			case BOT:
				path = new File("plugins/AntiAuraAddon");
				break;
			case STRINGS:
				path = new File("plugins/AntiAuraAddon");
				break;
			case HOVERABLE:
				path = new File("plugins/AntiAuraAddon/notifications");
				break;
			case CLICKABLE:
				path = new File("plugins/AntiAuraAddon/notifications");
				break;
			case COMBINED:
				path = new File("plugins/AntiAuraAddon/notifications");
				break;
			default:
				path = new File("plugins/AntiAuraAddon");
				plugin.getLogger().log(Level.SEVERE, "Could not apply correct file path!");
				break;
			}

			try {
				if (!path.exists()) {
					boolean path_created = path.mkdirs();
					if (!path_created)
						plugin.getLogger().log(Level.SEVERE,
								"Could not mkdir file path...\nPath: " + f.getCanonicalFile());
					return;
				}
				boolean file_created = f.createNewFile();
				if (!file_created)
					plugin.getLogger().log(Level.SEVERE, "Could not create file...\nPath: " + f.getCanonicalFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param path
	 * @param value
	 * @return
	 */
	public void setValue(String path, Object value) {
		Validate.notNull(path, "Path cannot be null");
		Validate.notNull(value, "Value cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		if (f.exists()) {
			if (!cfg.isSet(path)) {
				cfg.set(path, value);
				try {
					cfg.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setHeader(String header) {
		Validate.notNull(header, "Header cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		cfg.options().header(header);
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileManager setPaths(String... paths) {
		this.paths = paths;
		return this;
	}

	public FileManager setValues(Object... values) {
		this.values = values;
		return this;
	}

	public void apply() {
		Validate.notNull(paths, "Found no paths");
		Validate.notNull(values, "Found no values");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		if (paths.length != values.length) {
			plugin.getLogger().log(Level.WARNING,
					"Paths and values does not have the same length!\nThis results in wrong value placement.\nProccess stopped, please inform the developer.");
		}
		for (int i = 0; i != paths.length; i++) {
			if (!cfg.isSet(paths[i]))
				cfg.set(paths[i], values[i]);
			try {
				cfg.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void change() {
		Validate.notNull(paths, "Found no paths");
		Validate.notNull(values, "Found no values");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		if (paths.length != values.length) {
			plugin.getLogger().log(Level.WARNING,
					"Paths and values does not have the same length!\nThis results in wrong value placement.\nProccess stopped, please inform the developer");
		}
		for (int i = 0; i != paths.length; i++) {
			cfg.set(paths[i], values[i]);
			try {
				cfg.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void changeValue(String path, Object value) {
		Validate.notNull(path, "Path cannot be null");
		Validate.notNull(value, "Value cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		if (f.exists()) {
			cfg.set(path, value);
			try {
				cfg.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void reloadFile() {
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createPlayerFile(Player p) {
		try {
			File f = player;
			File path = new File("plugins/AntiAuraAddon/data/players/" + p.getUniqueId().toString());
			if (!f.exists()) {
				if (!path.exists()) {
					boolean path_created = path.mkdirs();
					if (!path_created) {
						plugin.getLogger().log(Level.SEVERE,
								"Could not mkdir file path...\nPath: " + f.getCanonicalFile());
						return;
					}
					boolean file_created = f.createNewFile();
					if (!file_created) {
						plugin.getLogger().log(Level.SEVERE, "Could not create file...\nPath: " + f.getCanonicalFile());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileManager loadPlayerFile(Player p) {
		try {
			Validate.notNull(p, "Player cannot be null");
			String prepared = FileType.PLAYER.getFile().getCanonicalPath().replaceAll("%uuid%", p.getUniqueId().toString());
			File f = new File(prepared);
			player = f;
			return this;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setPlayerFileHeader(String header) {
		Validate.notNull(header, "Header cannot be null");
		File f = player;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		cfg.options().header(header);
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object returnPlayerValue(String path) {
		Validate.notNull(path, "Path cannot be null");
		File p = player;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(p);
		if (cfg.get(path) instanceof Boolean) {
			return cfg.getBoolean(path);
		}
		if (cfg.get(path) instanceof String) {
			return cfg.getString(path);
		}
		if (cfg.get(path) instanceof Integer) {
			return cfg.getInt(path);
		}
		if (cfg.get(path) instanceof Double) {
			return cfg.getDouble(path);
		}
		if (cfg.get(path) instanceof Long) {
			return cfg.getLong(path);
		}
		if (cfg.get(path) instanceof List<?>) {
			return cfg.getList(path);
		}
		// another object
		return cfg.get(path);
	}

	public void setPlayerValue(String path, Object value) {
		Validate.notNull(path, "Path cannot be null");
		Validate.notNull(value, "Value cannot be null");
		File p = player;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(p);
		if (!p.exists())
			return;
		if (cfg.isSet(path))
			return;
		cfg.set(path, value);
		try {
			cfg.save(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void changePlayerValue(String path, Object value) {
		Validate.notNull(path, "Path cannot be null");
		Validate.notNull(value, "Value cannot be null");
		File p = player;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(p);
		if (!p.exists())
			return;
		cfg.set(path, value);
		try {
			cfg.save(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public boolean getBoolean(@NotNull String path) {
		Validate.notNull(path, "Path cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		return cfg.getBoolean(path);
	}

	public String getString(@NotNull String path) {
		Validate.notNull(path, "Path cannot be null");
		File f = type.getFile();
		System.out.println(type.getFile());
		try {
			System.out.println(f.getCanonicalPath());
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		System.out.println(cfg.getStringList("prefix"));
		return cfg.getString(path);
	}

	public int getInt(@NotNull String path) {
		Validate.notNull(path, "Path cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		return cfg.getInt(path);
	}

	public double getDouble(@NotNull String path) {
		Validate.notNull(path, "Path cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		return cfg.getDouble(path);
	}

	public long getLong(@NotNull String path) {
		Validate.notNull(path, "Path cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		return cfg.getLong(path);
	}

	public List<?> getList(@NotNull String path) {
		Validate.notNull(path, "Path cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		return cfg.getList(path);
	}

	public Object get(@NotNull String path) {
		Validate.notNull(path, "Path cannot be null");
		File f = type.getFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		return cfg.get(path);
	}

}
