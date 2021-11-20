package mc.server.survival.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtil
{
    public static void playPlayerSound(Player player, Sound sound, int pitch, int volume)
    {
        player.playSound(player.getLocation(), sound, pitch, volume);
    }
}