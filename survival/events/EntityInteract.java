package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.managers.ChatManager;
import mc.server.survival.managers.DPlayerManager;
import mc.server.survival.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Objects;

public class EntityInteract implements Listener
{
    @EventHandler
    public void onEvent(PlayerInteractEntityEvent event)
    {
        Location location = event.getRightClicked().getLocation();

        if (location.equals(new Location(WorldUtil.getWorld("lobby"), 453.5, 65, 9.5, 90, 0)))
        {
            event.setCancelled(true);
            InventoryUtil.createNewInventory(event.getPlayer(), 45, ChatColor.translateAlternateColorCodes('&', "&c&lSKLEP"), "sklep");
        }
        else if (location.equals(new Location(WorldUtil.getWorld("lobby"), 446.5, 65, 14.5, 180, 0)))
        {
            event.setCancelled(true);
            WorldUtil.queueWorldChange(event.getPlayer(), WorldUtil.WorldType.SURVIVAL);
        }
        else if (location.equals(new Location(WorldUtil.getWorld("lobby"), 449.5, 65, 14.5, 180, 0)))
        {
            event.setCancelled(true);
            WorldUtil.teleportRandomly(event.getPlayer());
        }
        else if (location.equals(new Location(WorldUtil.getWorld("lobby"), 437.5, 65, 14.5, 180, 0)))
        {
            event.setCancelled(true);
            ChatManager.sendMessage(event.getPlayer(), Configuration.SERVER_FULL_PREFIX + "#8c8c8cTrwa otwieranie menu postaci... #fff203⌛");
            InventoryUtil.createNewInventory(event.getPlayer(), 54, ChatColor.translateAlternateColorCodes('&', "&c&lPOSTAC"), "postac");
        }
        else if (location.equals(new Location(WorldUtil.getWorld("lobby"), 401.5, 76, 17.5, 155, 0)))
        {
            event.setCancelled(true);
            InventoryUtil.createNewInventory(event.getPlayer(), 45, ChatColor.translateAlternateColorCodes('&', "&c&lQUESTY"), "questy");
        }
        else if (location.equals(new Location(WorldUtil.getWorld("lobby"), 430.5, 65, 21.5, 180, 0)))
        {
            event.setCancelled(true);

            long time = Objects.requireNonNull(WorldUtil.getWorld("lobby")).getTime();

            if (time > 14000 && time < 24000)
                InventoryUtil.createNewInventory(event.getPlayer(), 36, ChatColor.translateAlternateColorCodes('&', "&c&lMONOPOLOWY U STASIA"), "monopolowy");
            else
                ChatManager.sendMessage(event.getPlayer(), Configuration.SERVER_FULL_PREFIX + "#fc7474Wypierdalaj menelu! Moj sklep jest czynny tylko w nocnych godzinach!");
        }

        if (event.getRightClicked() instanceof Player && DPlayerManager.getInstance().getMarry(event.getPlayer()) != null && event.getRightClicked().equals(Bukkit.getPlayer(DPlayerManager.getInstance().getMarry(event.getPlayer()))))
            if (event.getRightClicked().getName().equalsIgnoreCase(DPlayerManager.getInstance().getMarry(event.getPlayer())))
                if (location.distance(event.getPlayer().getLocation()) < 1.5)
                {
                    int timexp = TimeUtil.getDifferenceInMinutes(DPlayerManager.getInstance().getMarryDate(event.getPlayer())) / 6;
                    int xp = DPlayerManager.getInstance().getMarryLevel(event.getPlayer()) + DPlayerManager.getInstance().getMarryLevel(DPlayerManager.getInstance().getMarry(event.getPlayer())) + timexp;
                    int level = xp / 100;

                    if (level >= 100)
                        QuestUtil.manageQuest(event.getPlayer(), 12);

                    event.getPlayer().getWorld().spawnParticle(Particle.HEART, location.add(0, 2, 0), 1, 1, 1, 1, 1);
                    if (MathUtil.chanceOf(12))
                        DPlayerManager.getInstance().setMarryLevel(event.getPlayer(), DPlayerManager.getInstance().getMarryLevel(event.getPlayer()) + 1);
                }
    }
}