package me.rothman.corechat.utils;

import me.rothman.corechat.chat.ChatFormat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class FileManager {

    private static final HashMap<String, CustomFile> configs = new HashMap<>();
    private static final HashMap<String, ChatFormat> formats = new HashMap<>();

    public static void loadConfig(String filename, String filepath) {
        configs.put(filename, new CustomFile(filename, filepath));
    }

    public static void loadFormats() {
        ConfigurationSection chatSection = configs.get("config.yml").getConfig().getConfigurationSection("chat-formats");
        if (chatSection != null && !chatSection.getKeys(false).isEmpty())
            for (String formatName : chatSection.getKeys(false)) formats.put(formatName, new ChatFormat(formatName));
    }

    public static FileConfiguration getConfig(String filename) {
        return configs.get(filename).getConfig();
    }

    public static HashMap<String, CustomFile> getConfigs() {
        return configs;
    }

    public static HashMap<String, ChatFormat> getFormats() {
        return formats;
    }

}
