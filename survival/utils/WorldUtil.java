package mc.server.survival.utils;

import mc.server.Logger;
import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.managers.ChatManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WorldUtil 
{
	public static final Location LOBBY_SPAWN = new Location(getWorld("lobby"), 410, 68.5, -20, 0, 0);
	public static final Location LOBBY_CHURCH = new Location(getWorld("lobby"), 455, 70, -25, 0, 0);
	public static final Location LOBBY_DUNGEON = new Location(getWorld("lobby"), 373, 45, -53, -90, 0);
	public static final Location SURVIVAL_SPAWN = new Location(getWorld("survival"), 0, 120, 0, 0, 0);

	private static final String[] worlds = {"lobby", "survival", "survival_nether", "survival_the_end"};
	private static int preparedWorlds = 0;
	private static int loadedWorlds = 0;

	public static World getWorld(String name)
	{
		return Bukkit.getServer().createWorld(new WorldCreator(name));
	}

	public static void loadWorlds()
	{
		for (String world : worlds)
			loadWorld(world);
	}

    public static void loadWorld(String name)
	{
		if (preparedWorlds++ >= worlds.length - 1 /* Due to lobby world */)
			Logger.asyncLog("&7Zaladowano swiaty: " + loadedWorlds + "/" + worlds.length);

		WorldCreator worldCreator = new WorldCreator(name);
		worldCreator.createWorld();

		if (Bukkit.getWorld(name) == null)
			Logger.asyncLog("&c(!) &7Nie udalo sie zaladowac swiata: " + name);

		applyGameRules(name);
		loadedWorlds++;
	}

	private static void applyGameRules(String name)
	{
		World world = getWorld(name);

		world.setGameRule(GameRule.MOB_GRIEFING, false);
		world.setGameRule(GameRule.DO_PATROL_SPAWNING, !name.equalsIgnoreCase("lobby"));
		world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 50);
		world.setGameRule(GameRule.DO_TRADER_SPAWNING, true);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
		world.setGameRule(GameRule.KEEP_INVENTORY, false);
		world.setGameRule(GameRule.RANDOM_TICK_SPEED, 2);
	}

	public static void teleportRandomly(Player player)
	{
		teleportRandomly(player, player.getWorld().getName());
	}

	public static void teleportRandomly(Player player, String world)
	{
		int x = 2000 - new Random().nextInt(4000);
		int z = 2000 - new Random().nextInt(4000);
		int y = player.getWorld().getHighestBlockYAt(x, z);

		player.teleport(new Location(getWorld(world), x, y, z));
	}

	public enum WorldType
	{
		LOBBY,
		SURVIVAL,
		DIMENSIONABLE;
	}

	public static void queueWorldChange(Player player, WorldType worldType)
	{
		if (worldType == WorldType.LOBBY)
		{
			ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja na lobby serwera... #fff203⌛");
			player.teleport(LOBBY_SPAWN);
		}
		else if (worldType == WorldType.SURVIVAL)
		{
			ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa teleportacja na swiat survivalowy... #fff203⌛");
			teleportRandomly(player, "survival");
		}
		else if (worldType == WorldType.DIMENSIONABLE)
		{
			if (Configuration.SERVER_BLOCK_NETHER)
			{
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#fc7474Wrota do tego swiata sa obecnie zamkniete!");
				VisualUtil.showDelayedTitle(player, "&c✖", "", 0, 20, 20);
			}
		}

		new BukkitRunnable() { @Override public void run() {
			VisualUtil.showServerChangeTitle(player);
			ServerUtil.reloadContents(player);
			if (worldType != WorldType.DIMENSIONABLE)
				VisualUtil.spawnFirework(player.getLocation());
		} }.runTaskLater(Main.getInstance(), 20L);
	}
}