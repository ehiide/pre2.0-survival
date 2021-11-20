package mc.server.survival.events;

import mc.server.survival.files.Configuration;
import mc.server.survival.items.*;
import mc.server.survival.managers.DPlayerManager;
import mc.server.survival.managers.FileManager;
import mc.server.survival.managers.NeuroManager;
import mc.server.survival.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerInteract 
implements Listener
{
	@EventHandler
	public void onEvent(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.PHYSICAL && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.FARMLAND)
			event.setCancelled(true);

		if ((boolean) FileManager.getInstance().getConfigValue("function.sitting.status"))
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
				if (event.getClickedBlock() != null && event.getClickedBlock().getType().toString().contains("STAIRS"))
				{
					if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby") && Configuration.SERVER_TERRAIN_PROTECTION)
						event.setCancelled(true);

					if (!event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType().isAir()) return;

					if (event.getPlayer().getVehicle() != null) return;

					if (event.getPlayer().isSneaking()) return;

					if ((boolean) FileManager.getInstance().getConfigValue("function.sitting.height-fix"))
						if (Math.abs(event.getPlayer().getLocation().getBlockY() - event.getClickedBlock().getLocation().getBlockY()) > 1.5)
							return;

					if (event.getPlayer().getInventory().getItemInMainHand().getType()!= Material.AIR
							|| event.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR) return;

					if (event.getClickedBlock().getType().toString().contains("STAIRS"))
					{
						Stairs stairs = (Stairs) event.getClickedBlock().getBlockData();

						if (stairs.getHalf() == Bisected.Half.TOP) return;

						if (stairs.getShape() != Stairs.Shape.STRAIGHT) return;

						if (NPCUtil.getChairRelative(stairs) == 404) return;

						ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(event.getPlayer().getWorld()).spawnEntity(new Location(event.getClickedBlock().getWorld(),
								event.getClickedBlock().getBoundingBox().getCenter().getX(),
								event.getClickedBlock().getBoundingBox().getCenter().getY() - 0.2,
								event.getClickedBlock().getBoundingBox().getCenter().getZ(),
										NPCUtil.getChairRelative(stairs),
										0),
								EntityType.ARMOR_STAND);

						event.getPlayer().teleport(new Location(event.getClickedBlock().getWorld(),
								event.getClickedBlock().getBoundingBox().getCenter().getX(),
								event.getClickedBlock().getBoundingBox().getCenter().getY() - 0.2,
								event.getClickedBlock().getBoundingBox().getCenter().getZ(),
										NPCUtil.getChairRelative(stairs),
										0));

						armorStand.setCustomName("CHAIR");
						armorStand.setCustomNameVisible(false);
						armorStand.setMarker(true);
						armorStand.setInvulnerable(true);
						armorStand.setCollidable(false);
						armorStand.setGravity(false);
						armorStand.setRemoveWhenFarAway(false);
						armorStand.setBasePlate(false);
						armorStand.setArms(true);
						armorStand.setVisible(false);
						armorStand.setPassenger(event.getPlayer());
					}
				}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			if (event.getClickedBlock() != null)
			{
				if (event.getClickedBlock().getType() != Material.BREWING_STAND) return;

				if (event.getPlayer().getInventory().getItemInMainHand().getType()!= Material.AIR
						|| event.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR) return;

				if (event.getClickedBlock().getLocation().add(0, -1, 0).getBlock().getType() == Material.LAVA_CAULDRON)
				{
					event.setCancelled(true);
					InventoryUtil.createNewInventory(event.getPlayer(), 54, ChatColor.translateAlternateColorCodes('&', "&c&lSTATYW ALCHEMICZNY &0&1"), "drug_table_amina");
				}
			}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby") &&
				event.getClickedBlock().getType() == Material.POLISHED_BLACKSTONE_BUTTON && event.getClickedBlock().getLocation().distance(WorldUtil.LOBBY_CHURCH) < 45)
			if (DPlayerManager.getInstance().getMoney(event.getPlayer()) >= 100)
			{
				DPlayerManager.getInstance().setMoney(event.getPlayer(), DPlayerManager.getInstance().getMoney(event.getPlayer()) - 100);
				ServerUtil.reloadContents(event.getPlayer());
				event.getPlayer().getWorld().dropItemNaturally(event.getClickedBlock().getLocation().add(-1, 0, 0),
						MathUtil.chanceOf(50) ? Monster.getItem() : MathUtil.chanceOf(50) ? Monster2.getItem() : MathUtil.chanceOf(50) ? Monster3.getItem() : MathUtil.chanceOf(50) ? Monster4.getItem() : Monster5.getItem());
			}

		if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby") && Configuration.SERVER_TERRAIN_PROTECTION)
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getLocation().distance(WorldUtil.LOBBY_SPAWN) < 256)
				{
					event.setCancelled(true);
					return;
				}

		if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby"))
			if (event.getPlayer().getItemInHand().isSimilar(new ItemStack(Material.ENDER_PEARL)) || event.getPlayer().getItemInHand().isSimilar(new ItemStack(Material.CHORUS_FRUIT)))
				if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
					if (event.getPlayer().getLocation().distance(WorldUtil.LOBBY_SPAWN) < 256)
					{
						event.setCancelled(true);
						return;
					}

		if (event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			if (event.getItem() == null) return;

			if (event.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR) return;

			boolean isKnownDrug = Chemistries.getInstance().isKnown(event.getPlayer().getInventory().getItemInMainHand());
			boolean isNotOnCooldown = !event.getPlayer().hasCooldown(Material.SUGAR) && !event.getPlayer().hasCooldown(Material.GUNPOWDER);
			boolean haveDrug = event.getItem().getType() == Material.SUGAR || event.getItem().getType() == Material.GUNPOWDER;

			if (isKnownDrug && isNotOnCooldown && haveDrug)
			{
				String itemName = event.getItem().hasItemMeta() ? event.getItem().getItemMeta().getDisplayName() : null;
				ChemistryItem chemistryItem = Chemistries.getInstance().byName(itemName);
				ItemStack itemStack = ChemistryDrug.getDrug(chemistryItem);

				Inventory.removeItem(event.getPlayer(), itemStack, 1);

				if (chemistryItem.getAffinity().isOpioidic())
					NeuroManager.normalize(event.getPlayer(),
						chemistryItem.getAffinity().getOpioidic());
				else if (chemistryItem.getAffinity().isAmine())
					NeuroManager.modify(event.getPlayer(),
						chemistryItem.getAffinity().getSerotonine(),
						chemistryItem.getAffinity().getDopamine(),
						chemistryItem.getAffinity().getNoradrenaline(),
						chemistryItem.getAffinity().getGABA());

				event.getPlayer().setCooldown(Material.SUGAR, 120); event.getPlayer().setCooldown(Material.GUNPOWDER, 120);
			}
		}
	}
}