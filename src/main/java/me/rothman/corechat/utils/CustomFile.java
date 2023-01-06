package me.rothman.corechat.utils;

import me.rothman.corechat.CoreChat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomFile {

    private final CoreChat plugin;
    private final File file;
    private FileConfiguration config;

    public CustomFile(String filename, String filepath) {
        plugin = CoreChat.getInstance();
        file = new File(plugin.getDataFolder(), filepath + File.separator + filename);
        create();
        reloadConfig();
    }

    private void create() {
        if (!file.exists()) {
            assert file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
        }
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        if (config.getKeys(false).isEmpty()) {
            plugin.saveResource(file.getName(), true);
            config = YamlConfiguration.loadConfiguration(file);
        }
    }

    public void reload() {
        reloadConfig();
        saveConfig();
    }

    public FileConfiguration getConfig() {
        if (config == null) reloadConfig();
        return config;
    }

}
