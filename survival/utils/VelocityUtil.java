package mc.server.survival.utils;

import mc.server.survival.files.Main;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class VelocityUtil 
{
	public static void resetVelocity(Entity entity)
	{
		entity.setVelocity(new Vector(0, 0, 0));
	}
	
	public static void convertVelocity(Entity entity, double X, double Y, double Z)
	{
		entity.setVelocity(new Vector(X, Y, Z));
	}
	
	public static void applyVelocity(Entity entity, double strength)
	{
		new BukkitRunnable() 
		{
			@Override
			public void run()
			{
				double motX = entity.getVelocity().getX() * strength;
				double motZ = entity.getVelocity().getZ() * strength;
				double motY = entity.getVelocity().getY();
				resetVelocity(entity);
				convertVelocity(entity, motX, motY, motZ);
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 1L);
	}
}