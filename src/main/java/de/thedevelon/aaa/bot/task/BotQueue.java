package de.thedevelon.aaa.bot.task;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class BotQueue extends TimerTask {

	Timer timer;
	ArrayList<String> queue;
	JDA jda;
	String channelID;

	public BotQueue(Timer timer, ArrayList<String> queue, JDA jda, String channelID) {

		this.timer = timer;
		this.queue = queue;
		this.jda = jda;
		this.channelID = channelID;

	}

	@Override // 529648449520533504 for testing with id
	public void run() {
		if (!queue.isEmpty()) {
			TextChannel channel = (TextChannel) jda.getTextChannelById(String.valueOf(channelID));
			channel.sendMessage(queue.get(0)).queue();
			queue.remove(0);
			
		}

	}

}
