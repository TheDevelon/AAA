package de.thedevelon.aaa.bot;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Level;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;

import de.thedevelon.aaa.bot.task.BotQueue;
import de.thedevelon.aaa.util.filemanager.FileManager;
import de.thedevelon.aaa.util.filemanager.FileType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

public class Bot {

	private static ArrayList<String> queue = new ArrayList<>();

	public void init() throws LoginException, IOException {
		FileManager bot = new FileManager();
		//load file
		bot.loadFile(FileType.BOT);
		// build JDA
		// JDA api = JDABuilder.createDefault((String)fm.getValue("token")).build();
		// test token NDc0NzAyMTIyMDcyNTM5MTM2.XpMuhQ.y2J18PWe-mkPaLs6CzD5tNwsPWw
		if (bot.getBoolean("activate")) {
			JDA api = JDABuilder.createDefault(bot.getString("token")).build();
			Presence presence = api.getPresence();
			api.getPresence().setStatus(OnlineStatus.ONLINE);
			presence.setActivity(Activity.playing(bot.getString("activity")));

			Timer timer = new Timer();
			int startAfter = 1000;
			// ms * sec = seconds
			long period = bot.getLong("repeatInMilliseconds");
			BotQueue sched = new BotQueue(timer, queue, api, bot.getString("channelid"));
			timer.schedule(sched, startAfter, period);
		} else {
			Bukkit.getLogger().log(Level.INFO, "[AntiAuraAddon] Info: Bot deactivated.");
		}
	}

	public static void sendMessageToQueue(String message) {
		queue.add(message);
	}

}
