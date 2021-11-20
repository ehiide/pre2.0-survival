package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.utils.VisualUtil;
import mc.server.survival.utils.WorldUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Portal 
implements Listener
{
	@EventHandler
	public void onEvent(PlayerPortalEvent event)
	{
		Player player = event.getPlayer();
		World world = event.getFrom().getWorld();
		World sworld = event.getTo().getWorld();

		PlayerTeleportEvent.TeleportCause cause = event.getCause();

		if (world.getName().equalsIgnoreCase("lobby"))
		{
			ChatManager.sendMessage(event.getPlayer(), Configuration.SERVER_FULL_PREFIX + "#fc7474Nie mozesz uzywac systemow teleportacji na spawnie serwera!");
			VisualUtil.showDelayedTitle(event.getPlayer(), "&c✖", "", 0, 20, 20);
			event.setCancelled(true);
			return;
		}

		if (cause.equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL))
		{
			if (Configuration.SERVER_BLOCK_NETHER)
				event.setCancelled(true);

			if (sworld.getName().equalsIgnoreCase("survival_nether"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.DIMENSIONABLE);

			if (sworld.getName().equalsIgnoreCase("survival"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.SURVIVAL);
		}

		if (cause.equals(PlayerTeleportEvent.TeleportCause.END_PORTAL))
		{
			if (Configuration.SERVER_BLOCK_THE_END)
				event.setCancelled(true);

			if (sworld.getName().equalsIgnoreCase("survival_the_end"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.DIMENSIONABLE);

			if (sworld.getName().equalsIgnoreCase("survival"))
				WorldUtil.queueWorldChange(player, WorldUtil.WorldType.SURVIVAL);
		}
	}
}