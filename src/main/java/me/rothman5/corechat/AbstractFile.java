package me.rothman5.corechat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class AbstractFile
{
    private CoreChat plugin;
    private String file_path;
    private String file_name;

    private File file;
    public FileConfiguration yaml;

    public AbstractFile(String path, String name)
    {
        file_path = path;
        file_name = name;
        plugin = CoreChat.get_instance();
    }

    public abstract void load_content();

    public FileConfiguration get_content()
    {
        if (yaml == null)
        {
            load_yaml();
        }
        return yaml;
    }

    public void reload()
    {
        load_file();
        load_yaml();
        load_content();
    }

    public void save()
    {
        try
        {
            yaml.save(file);
        }
        catch (FileNotFoundException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Couldn't find " + file.getName(), e);
        }
        catch (IOException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Couldn't save " + file.getName(), e);
        }
        finally
        {
            plugin.getLogger().info(file.getName() + " saved successfully.");
        }
    }

    public void load_file()
    {
        if (file != null)
        {
            return;
        }

        try
        {
            file = new File(plugin.getDataFolder(), file_path + File.separator + file_name);
            if (!file.exists())
            {
                assert file.createNewFile();
                plugin.saveResource(file.getName(), false);
            }
        }
        catch (IOException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Couldn't load " + file.getName(), e);
        }
    }

    public void load_yaml()
    {
        yaml = new YamlConfiguration();
        try
        {
            yaml.load(file);
            if (yaml.getKeys(false).isEmpty())
            {
                plugin.saveResource(file.getName(), true);
                yaml.load(file);
            }
        }
        catch (FileNotFoundException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Couldn't find " + file.getName(), e);
        }
        catch (InvalidConfigurationException | IOException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Couldn't load " + file.getName(), e);
        }
        finally
        {
            plugin.getLogger().info(file.getName() + " file loaded successfully.");
        }
    } 
}
