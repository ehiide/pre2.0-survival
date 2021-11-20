package mc.server.survival.utils;

import mc.server.Logger;
import mc.server.survival.files.Main;
import mc.server.survival.items.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class RecipeUtil 
{
	private RecipeUtil() {}

	static RecipeUtil instance = new RecipeUtil();

	public static RecipeUtil getInstance()
	{
		return instance;
	}

	private static int loadedBasicRecipes = 0;
	private static int loadedAdvancedRecipes = 0;

	public void addRecipes()
	{
		createBasicRecipe(Material.SADDLE, "null", "saddle_crafting");
		createBasicRecipe(Material.NAME_TAG, "null", "nametag_crafting");
		createBasicRecipe(Material.ELYTRA, "null", "elytra_crafting");
		createBasicRecipe(Material.DIAMOND_HORSE_ARMOR, "null", "diamond_horse_armor_crafting");
		createBasicRecipe(Material.EXPERIENCE_BOTTLE, "null", "experience_bottle_crafting");

		Logger.asyncLog("&7Zaladowano wewnetrzne receptury: " + loadedBasicRecipes + "/" + loadedBasicRecipes);

		createAdvancedRecipe(Piwo.getRecipe());
		createAdvancedRecipe(Wino.getRecipe());
		createAdvancedRecipe(Bimber.getRecipe());

		Logger.asyncLog("&7Zaladowano zewnetrzne receptury: " + loadedAdvancedRecipes + "/" + loadedAdvancedRecipes);
	}
	
	public static void createBasicRecipe(Material material, String name, String id)
	{
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (!name.equalsIgnoreCase("null"))
        	Objects.requireNonNull(meta).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r" + name));
        
        item.setItemMeta(meta);
 
        NamespacedKey key = new NamespacedKey(Main.getInstance(), id);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        
        if (id.equalsIgnoreCase("saddle_crafting"))
        {
	        recipe.shape("LLL", "SOS", "I0I");
	        recipe.setIngredient('L', Material.LEATHER);
	        recipe.setIngredient('S', Material.STRING);
	        recipe.setIngredient('I', Material.IRON_INGOT);
	        recipe.setIngredient('0', Material.AIR);
        }
        else if (id.equalsIgnoreCase("nametag_crafting"))
        {
	        recipe.shape("SIS", "PPP", "SIS");
	        recipe.setIngredient('P', Material.PAPER);
	        recipe.setIngredient('S', Material.STRING);
	        recipe.setIngredient('I', Material.IRON_INGOT);
        }
        else if (id.equalsIgnoreCase("elytra_crafting"))
        {
	        recipe.shape("TGT", "IAI", "I0I");
	        recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
	        recipe.setIngredient('G', Material.GHAST_TEAR);
	        recipe.setIngredient('A', Material.ARMOR_STAND);
	        recipe.setIngredient('I', Material.NETHERITE_INGOT);
	        recipe.setIngredient('0', Material.AIR);
        }
        else if (id.equalsIgnoreCase("diamond_horse_armor_crafting"))
        {
	        recipe.shape("DDD", "DDD", "D0D");
	        recipe.setIngredient('D', Material.DIAMOND);
	        recipe.setIngredient('0', Material.AIR);
        }
        else if (id.equalsIgnoreCase("experience_bottle_crafting"))
        {
	        recipe.shape("0G0", "IBI", "0G0");
	        recipe.setIngredient('G', Material.GLOWSTONE_DUST);
	        recipe.setIngredient('I', Material.IRON_NUGGET);
	        recipe.setIngredient('B', Material.GLASS_BOTTLE);
	        recipe.setIngredient('0', Material.AIR);
        }
        
        Bukkit.addRecipe(recipe);
        
        loadedBasicRecipes++;
	}
	
	public static void createAdvancedRecipe(ShapedRecipe recipe)
	{
		Bukkit.addRecipe(recipe);

		loadedAdvancedRecipes++;
	}
}