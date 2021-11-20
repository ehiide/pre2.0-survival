package mc.server;

import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Logger 
{
	public static void log(String log) 
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Configuration.CONSOLE_FULL_PREFIX + log));
	}
	
	public static void asyncLog(String log) 
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				log(log);
			}
		}.runTaskAsynchronously(Main.getInstance());
	}
}