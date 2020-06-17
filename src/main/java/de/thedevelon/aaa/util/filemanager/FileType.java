package de.thedevelon.aaa.util.filemanager;

import java.io.File;

public enum FileType {
	
	CONFIG(new File("plugins/AntiAuraAddon/config.yml")),
	STRINGS(new File("plugins/AntiAuraAddon/strings.yml")),
	BOT(new File("plugins/AntiAuraAddon/bot.yml")),
	HOVERABLE(new File("plugins/AntiAuraAddon/notifications/hoverable.yml")),
	CLICKABLE(new File("plugins/AntiAuraAddon/notifications/clickable.yml")),
	COMBINED(new File("plugins/AntiAuraAddon/notifications/mixed.yml")),
	PLAYER(new File("plugins/AntiAuraAddon/data/players/%uuid%/%uuid%.yml"));
	
	private File type;
	
	FileType(File file){
		this.type = file;
	}

	public File getFile() {
		return type;
	}

}
