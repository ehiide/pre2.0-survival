package mc.server.survival.utils;

import mc.server.survival.managers.DPlayerManager;
import org.bukkit.entity.Player;

public class TablistUtil
{
	public static void showTablist(Player player)
	{
		String worldName = player.getWorld().getName();

		if (worldName.equalsIgnoreCase("lobby"))
			player.setPlayerListHeaderFooter(
					ColorUtil.formatHEX("\n&7--{ &c&lL O B B Y &7}-- \n\n "
							+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
							"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
		else if (worldName.equalsIgnoreCase("survival"))
			player.setPlayerListHeaderFooter(
			ColorUtil.formatHEX("\n&7--{ &c&lS U R V I V A L &7}-- \n\n "
					+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
					"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
		else if (worldName.equalsIgnoreCase("survival_nether"))
			player.setPlayerListHeaderFooter(
					ColorUtil.formatHEX("\n&7--{ &c&lN E T H E R &7}-- \n\n "
							+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
							"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
		else
			player.setPlayerListHeaderFooter(
					ColorUtil.formatHEX("\n&7--{ &c&lT H E   E N D &7}-- \n\n "
							+ "&c» &aZyczymy milej i przyjemnej gry, " + player.getName() +
							"! &c«\n &7&o(Spis wszystkich komend znajdziesz pod komenda /pomoc)\n"),
					ColorUtil.formatHEX("\n&c» &7------------------------ &c«\n"));
	}

	public static void showPlayerTag(Player player)
	{
		player.setPlayerListName(ColorUtil.formatHEX(ChatUtil.getGangInChat(DPlayerManager.getInstance().getGang(player)) + ChatUtil.returnMarryPrefix(player) + ChatUtil.returnPlayerColor(player) + player.getName()));
	}
}