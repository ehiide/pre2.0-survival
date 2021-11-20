package mc.server.survival.utils;

import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ServerUtil 
{
	public static void reloadContents(Player player)
	{
		ScoreboardUtil.showScoreboard(player);
		TablistUtil.showTablist(player);
		TablistUtil.showPlayerTag(player);
	}
	
	public static int getPing(Player player)
	{
		return player.getPing();
	}

	public static boolean getPremiumState(String name)
	{
		try
		{
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			StringBuilder result = new StringBuilder();

			while ((line = reader.readLine()) != null)
				result.append(line);

			return !result.toString().equals("");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}
}