package mc.server.survival.managers;

import mc.server.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class FileManager 
{
	private FileManager() {}

	static FileManager instance = new FileManager();
		
	public static FileManager getInstance() 
	{
		return instance;
	}
	
	FileConfiguration dataConfiguration, settingsConfiguration;
	
	public FileConfiguration data() { return dataConfiguration; }
	public FileConfiguration settings() { return settingsConfiguration; }
	
	File dataFile, settingsFile;
	
	public void start(Plugin plugin)
	{
		dataFile = new File(plugin.getDataFolder(), "data.yml");
		settingsFile = new File(plugin.getDataFolder(), "settings.yml");

		dataConfiguration = plugin.getConfig();
		settingsConfiguration = new YamlConfiguration();

		/*
			Main plugin folder.
		 */

		if (!plugin.getDataFolder().exists()) 
		{
			plugin.getDataFolder().mkdir();
			Logger.log("Pomyslnie utworzono katalog plikow plug-inu!");
		}

		/*
			Files.
		 */
		
		try 
		{
			dataConfiguration.load(dataFile);
			Logger.log("Pomyslnie zaladowano plik data.yml!");
		} 
		catch (IOException | InvalidConfigurationException e1) 
		{
			Logger.log("Nie udalo sie odnalezc pliku data.yml, trwa jego synteza...");
		}

		try
		{
			settingsConfiguration.load(settingsFile);
			Logger.log("Pomyslnie zaladowano plik settings.yml!");
		}
		catch (IOException | InvalidConfigurationException e1)
		{
			Logger.log("Nie udalo sie odnalezc pliku settings.yml, trwa jego synteza...");
		}

		/*
			Exists of each file.
		 */
		
		if (!dataFile.exists()) 
		{
			try 
			{
				dataFile.createNewFile();
				Logger.log("Pomyslnie stworzono plik data.yml!");
				
				try
				{
					dataConfiguration.load(dataFile);
					Logger.log("Pomyslnie zaladowano plik data.yml!");
				}
				catch (InvalidConfigurationException e) 
				{
					Logger.log("Nie udalo sie zaladowac pliku data.yml. Prosimy o zresetowanie pliku!");
				}
				
				this.save();
			}
			catch (IOException e) { Logger.log("Nie udalo sie stworzyc pliku data.yml!"); }
		}

		if (!settingsFile.exists())
		{
			plugin.saveResource("settings.yml", false);
			Logger.log("Pomyslnie stworzono plik settings.yml!");

			InputStream inputStream = plugin.getResource("settings.yml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			settingsConfiguration = YamlConfiguration.loadConfiguration(reader);
			Logger.log("Pomyslnie zaladowano plik settings.yml!");
		}
	}
		
	public void save()
	{
		try { dataConfiguration.save(dataFile); }
		catch (IOException e) { Logger.log("Nie udalo sie zapisac bazy danych!"); }
	}

	public Object getConfigValue(String path) { return settingsConfiguration.get(path); }
}