package me.rothman.corechat;

import me.rothman.corechat.chat.ChatHandler;
import me.rothman.corechat.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoreChat extends JavaPlugin {

    private static CoreChat main;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            main = this;
            FileManager.loadConfig("config.yml", "");
            FileManager.loadFormats();
            getServer().getPluginManager().registerEvents(new ChatHandler(), this);
            getLogger().info(getDescription().getName() + " " + getDescription().getVersion() + " is now enabled!");

        } else {
            getLogger().warning("Placeholders won't work without the missing dependency: PlaceholderAPI");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(getDescription().getName() + " " + getDescription().getVersion() + " is now disabled!");
        main = null;
    }

    public static CoreChat getInstance() {
        return main;
    }

}
