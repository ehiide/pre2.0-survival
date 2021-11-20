package mc.server.survival.commands;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DPlayerCloudManager;
import mc.server.survival.utils.ChatUtil;
import mc.server.survival.utils.SoundUtil;
import mc.server.survival.utils.VisualUtil;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Wiadomosc implements CommandExecutor
{
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if (args.length == 0) 
			{
				ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz nazwe gracza! Wzor komendy: #ffc936/wiadomosc <gracz> <wiadomosc>");
				return true;
			}
			else
			{
				String potencial_player = args[0];
				
				for (Player dplayer : Bukkit.getOnlinePlayers())
					if (dplayer.getName().equalsIgnoreCase(potencial_player))
						if (args.length == 1)
						{
							ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#8c8c8cWprowadz swoja wiadomosc! Wzor komendy: #ffc936/wiadomosc <gracz> <wiadomosc>");
							return true;
						}
						else
						{
							StringBuilder message = new StringBuilder();

							for (int word = 1; word < args.length; word++)
								if (word + 1 >= args.length)
									message.append(args[word].toLowerCase());
								else
									message.append(args[word].toLowerCase()).append(" ");

							ChatManager.sendMessage(player, "#f83044&l» &cDo " + ChatUtil.returnPlayerColor(dplayer) + dplayer.getName() + ": &r&7" + ChatUtil.applyCorrection(ChatUtil.getPlaceholder(player, message.toString())));
							ChatManager.sendMessage(dplayer, "#f83044&l» &cWiadomosc od " + ChatUtil.returnPlayerColor(player) + player.getName() + ": &r&7" + ChatUtil.applyCorrection(ChatUtil.getPlaceholder(player, message.toString())));
							VisualUtil.showDelayedTitle(dplayer, "&7od: " + player.getName(), "#faff26✉", 0, 10, 10);
							DPlayerCloudManager.setReply(dplayer, player);
							return true;
						}
				
				if (!Bukkit.getOfflinePlayer(potencial_player).isOnline()) 
				{
					ChatManager.sendMessage(player, Configuration.SERVER_FULL_PREFIX + "#fc7474Podany gracz nie jest online na serwerze!");
					VisualUtil.showDelayedTitle(player, "#fc7474✖", "", 0, 20, 20);
					VisualUtil.showPlayerParticle(player, Particle.FLAME);
					SoundUtil.playPlayerSound(player, Sound.BLOCK_ANVIL_DESTROY, 4, 4);
					return true;
				}
			}
		}
		
		return false;
	}
}