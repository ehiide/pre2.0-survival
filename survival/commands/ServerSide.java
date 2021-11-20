package mc.server.survival.commands;

import mc.server.survival.items.Chemistries;
import mc.server.survival.items.ChemistryDrug;
import mc.server.survival.utils.InventoryUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerSide
implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("world")) {
                player.teleport(new Location(Bukkit.getWorld(args[1].toString()), 0, 90, 0, 0, 0));
                return true;
            }
            else if (args[0].equalsIgnoreCase("tp")) {
                player.teleport(new Location(player.getWorld(), (double) Integer.parseInt(args[1].toString()), Integer.parseInt(args[2].toString()), Integer.parseInt(args[3].toString()), 0, 0));
                return true;
            }
            else if (args[0].equalsIgnoreCase("gm")) {
                player.setGameMode(args[1].equalsIgnoreCase("0") ? GameMode.SURVIVAL : GameMode.CREATIVE);
                return true;
            }
            else if (args[0].equalsIgnoreCase("item"))
            {
                String itemName = args[1].toString();
                player.getInventory().addItem(ChemistryDrug.getDrug(Chemistries.getInstance().byName(itemName)));
                return true;
            }
            else if (args[0].equalsIgnoreCase("rl")) {
                Bukkit.reload();
                player.sendMessage("Reloaded.");
                return true;
            }
            else if (args[0].equalsIgnoreCase("gui")) {
                InventoryUtil.createNewInventory(player, 54, "non-reference opened.", args[1].toString());
                return true;
            }
        }

        return false;
    }
}