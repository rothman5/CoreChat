package me.rothman5.corechat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CoreChat extends JavaPlugin
{
    private static CoreChat plugin;
    public static FormatManager format_manager;

    @Override
    public void onEnable()
    {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
        {
            plugin = this;
            format_manager = new FormatManager("", "formats.yml");

            getCommand("corechat").setExecutor(this);
            getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " is now enabled.");
        }

        else
        {
            getLogger().warning("PlaceholderAPI is required for this plugin to work.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable()
    {
        plugin = null;
        getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " is now disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.YELLOW + "CoreChat v" + getDescription().getVersion() + " developed by rothman5.");
            return true;
        }

        if ((args.length == 1) && args[0].equalsIgnoreCase("reload"))
        {
            if (sender.hasPermission("corechat.reload"))
            {
                format_manager.reload();
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use that command.");
            }
            return true;
        }
        
        sender.sendMessage(ChatColor.RED + "Usage: /corechat <reload>");
        return false;
    }

    public static CoreChat get_instance()
    {
        return plugin;
    }
}
