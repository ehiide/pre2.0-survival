package mc.server.survival.managers;

import org.bukkit.entity.Player;
import java.util.HashMap;

public class DPlayerCloudManager 
{
	public static HashMap<Player, Player> tpa_queue = new HashMap<Player, Player>();
	public static HashMap<Player, Player> slub_queue = new HashMap<Player, Player>();
	public static HashMap<Player, Player> gang_queue = new HashMap<Player, Player>();
	public static HashMap<Player, Player> reply_queue = new HashMap<Player, Player>();
	
	public static Player getTPA(Player player)
	{
		return tpa_queue.get(player);
	}
	
	public static void setTPA(Player player, Player address)
	{
		tpa_queue.put(player, address);
	}
	
	public static Player getMarry(Player player)
	{
		return slub_queue.get(player);
	}
	
	public static void setMarry(Player player, Player address)
	{
		slub_queue.put(player, address);
	}

	public static Player getGang(Player player)
	{
		return gang_queue.get(player);
	}

	public static void setGang(Player player, Player address)
	{
		gang_queue.put(player, address);
	}

	public static Player getReply(Player player)
	{
		return reply_queue.get(player);
	}

	public static void setReply(Player player, Player address)
	{
		reply_queue.put(player, address);
	}
}