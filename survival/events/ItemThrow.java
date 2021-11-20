package mc.server.survival.events;

import mc.server.survival.files.Main;
import mc.server.survival.managers.FileManager;
import mc.server.survival.utils.ColorUtil;
import mc.server.survival.utils.MathUtil;
import mc.server.survival.utils.NPCUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;

public class ItemThrow implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(EntityDamageEvent event)
    {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
        {
            LivingEntity damagedEntity = (LivingEntity) event.getEntity();

            if (damagedEntity.getHealth() - event.getFinalDamage() <= 0)
            {
                event.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK, event.getEntity().getLocation().add(0, damagedEntity.getEyeHeight(), 0), 50, 0.02, 0.02, 0.02, Material.REDSTONE_BLOCK.createBlockData());

                if ((boolean) FileManager.getInstance().getConfigValue("visuals.damageIndicator"))
                    NPCUtil.createTempHologram(ColorUtil.formatHEX("&c-" + ((int) damagedEntity.getHealth()) + "❤"), damagedEntity.getLocation().add(0, damagedEntity.getEyeHeight(), 0));
            }
            else
                if ((boolean) FileManager.getInstance().getConfigValue("visuals.damageIndicator"))
                    NPCUtil.createTempHologram(ColorUtil.formatHEX("&c-" + ((int) event.getFinalDamage()) + "❤"), damagedEntity.getLocation().add(0, damagedEntity.getEyeHeight(), 0));

            new BukkitRunnable() { @Override public void run() {
                damagedEntity.setNoDamageTicks((int) FileManager.getInstance().getConfigValue("internal.hit-delay"));
            } }.runTaskLaterAsynchronously(Main.getInstance(), 1L);
        }
    }

    /*
        Used for cool-down player's projectiles for better visuals applying.
     */

    static BukkitTask runnable;
    
    @EventHandler
    public void onEvent(ProjectileLaunchEvent event)
    {
        Projectile projectile = event.getEntity();

        if (projectile instanceof Snowball || projectile instanceof Egg)
            if (event.getEntity().getShooter() instanceof Player)
            {
                Player player = (Player) event.getEntity().getShooter();
                player.setCooldown(((ThrowableProjectile) projectile).getItem().getType(), 4);
            }

        if ((boolean) FileManager.getInstance().getConfigValue("visuals.projectiles"))
            if (projectile instanceof Snowball || projectile instanceof Egg || projectile instanceof Arrow ||
                projectile instanceof SpectralArrow || projectile instanceof Trident || projectile instanceof EnderPearl)
            {
                runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (event.getEntity().hasMetadata("destroyed")) this.cancel();

                        World world = projectile.getWorld();
                        Particle.DustTransition transition;

                        if (projectile instanceof Snowball) transition = new Particle.DustTransition(Color.AQUA, Color.WHITE, 1);
                        else if (projectile instanceof Egg) transition = new Particle.DustTransition(Color.fromBGR(207, 248, 249), Color.WHITE, 1);
                        else if (projectile instanceof Arrow)
                            if (((Arrow) projectile).getBasePotionData().getType().equals(PotionType.UNCRAFTABLE))
                                transition = new Particle.DustTransition(Color.RED, Color.WHITE, 1);
                            else
                                transition = new Particle.DustTransition(((Arrow) projectile).getColor(), Color.WHITE, 1);
                        else if (projectile instanceof SpectralArrow) transition = new Particle.DustTransition(Color.YELLOW, Color.WHITE, 1);
                        else if (projectile instanceof Trident) transition = new Particle.DustTransition(Color.GREEN, Color.AQUA, 1);
                        // Ender-Pearl
                        else transition = new Particle.DustTransition(Color.TEAL, Color.AQUA, 1);

                        world.spawnParticle(Particle.DUST_COLOR_TRANSITION, projectile.getLocation().add(0, 0, 0), 5, 0.1, 0.1, 0.1, 1, transition);
                    }
                }.runTaskTimerAsynchronously(Main.getInstance(), 1, 1);
            }
    }

    @EventHandler
    public void onEvent(ProjectileHitEvent event)
    {
        Projectile projectile = event.getEntity();
        Entity hitEntity = null;
        Block hitBlock = null;

        event.getEntity().setMetadata("destroyed", new FixedMetadataValue(Main.getInstance(), "0"));

        if (event.getHitEntity() != null) hitEntity = event.getHitEntity();
        if (event.getHitBlock() != null) hitBlock = event.getHitBlock();

        if (projectile instanceof Arrow || projectile instanceof SpectralArrow || projectile instanceof Trident) if (hitBlock != null)
            projectile.getWorld().spawnParticle(Particle.BLOCK_DUST, projectile.getLocation(), 20, 0.4, 0, 0.4, hitBlock.getType().createBlockData());

        if ((boolean) FileManager.getInstance().getConfigValue("visuals.snow"))
            if (projectile instanceof Snowball) if (hitBlock != null)
            {
                projectile.getWorld().spawnParticle(Particle.SNOW_SHOVEL, projectile.getLocation(), 20, 0.75, 0, 0.75);

                if (hitBlock.getType() != Material.SNOW_BLOCK && hitBlock.getType() != Material.SNOW &&
                        !hitBlock.getType().toString().contains("STAIRS") && !hitBlock.getType().toString().contains("SLAB") &&
                        !hitBlock.getType().toString().contains("TRAPDOOR") && !hitBlock.getType().toString().contains("DOOR") &&
                        !hitBlock.getType().toString().contains("FENCE") && !hitBlock.getType().toString().contains("GATE")

                && hitBlock.getLocation().add(0, 1, 0).getBlock().getType().isSolid() | hitBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR

                )
                {
                    Material hitBlockMaterial = hitBlock.getType();
                    hitBlock.setType(Material.SNOW_BLOCK);

                    Block finalHitBlock = hitBlock;
                    finalHitBlock.setMetadata("unbreakable", new FixedMetadataValue(Main.getInstance(), "0"));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            finalHitBlock.setType(hitBlockMaterial);
                            if (finalHitBlock.hasMetadata("unbreakable"))
                                finalHitBlock.removeMetadata("unbreakable", Main.getInstance());
                        }
                    }.runTaskLater(Main.getInstance(), 20);

                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (hitBlock.getWorld().getBlockAt(hitBlock.getLocation().add(x, y, z)).getType() == Material.AIR)
                                {
                                    Block selectedPos = hitBlock.getWorld().getBlockAt(hitBlock.getLocation().add(x, y, z));

                                    if (selectedPos.getWorld().getBlockAt(selectedPos.getLocation().add(0, -1, 0)).getType() == Material.SNOW) return;

                                    if (MathUtil.chanceOf(50))
                                    {
                                        if (!selectedPos.getWorld().getBlockAt(selectedPos.getLocation().add(0, -1, 0)).getType().isSolid()) return;

                                        selectedPos.setType(Material.SNOW);
                                        selectedPos.setMetadata("unbreakable", new FixedMetadataValue(Main.getInstance(), "0"));

                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                selectedPos.setType(Material.AIR);
                                                if (selectedPos.hasMetadata("unbreakable"))
                                                    selectedPos.removeMetadata("unbreakable", Main.getInstance());
                                            }
                                        }.runTaskLater(Main.getInstance(), 20 + ((long) (int) (new Random().nextDouble() * 10)));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        if (projectile instanceof Egg) if (hitBlock != null)
            projectile.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, projectile.getLocation(), 20, 0.75, 0, 0.75);

        if (hitEntity != null)
            projectile.getWorld().spawnParticle(Particle.SWEEP_ATTACK, projectile.getLocation(), 1, 0, 0, 0);

        if (projectile instanceof Egg || projectile instanceof Snowball)
            if (hitEntity instanceof Player)
            {
                Player player = (Player) hitEntity;
                player.damage(0.5, projectile);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 0));
                player.setVelocity(new Vector(projectile.getVelocity().getX() / 3, 0.25, projectile.getVelocity().getZ() / 3));
            }
            else
                if (hitEntity != null && !hitEntity.isDead() && hitEntity.getTicksLived() > 20)
                    ((LivingEntity) hitEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 0));

        if (!(projectile instanceof FishHook) && !(projectile instanceof EnderPearl) && !(projectile instanceof Trident)) event.getEntity().remove();
    }
}