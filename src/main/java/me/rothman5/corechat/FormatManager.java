package me.rothman5.corechat;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class FormatManager extends AbstractFile implements Listener
{
    private static CoreChat plugin;
    public HashMap<String, ChatFormat> formats;

    public FormatManager(String path, String name)
    {
        super(path, name);
        plugin = CoreChat.get_instance();
        formats = new HashMap<String, ChatFormat>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        reload();
    }

    @Override
    public void load_content()
    {
        if (!formats.isEmpty())
        {
            formats.clear();
        }

        for (String format_name : yaml.getKeys(false))
        {
            ConfigurationSection format_section = yaml.getConfigurationSection(format_name);
            if (format_section == null)
            {
                plugin.getLogger().log(Level.SEVERE, "Failed to load the " + format_name + " format.");
                return;
            }

            ConfigurationSection prefix_section = format_section.getConfigurationSection("prefix");
            ConfigurationSection name_section = format_section.getConfigurationSection("name");
            ConfigurationSection suffix_section = format_section.getConfigurationSection("suffix");
            if ((prefix_section == null) || (name_section == null) || (suffix_section == null))
            {
                plugin.getLogger().log(Level.SEVERE, "Failed to load the " + format_name + " format.");
                return;
            }

            ChatSection prefix = new ChatSection(
                prefix_section.getString("format", "%luckperms_prefix%"), 
                prefix_section.getString("click-command", ""),
                prefix_section.getStringList("hover-message")
            );

            ChatSection name = new ChatSection(
                name_section.getString("format", "%player_name% "), 
                name_section.getString("click-command", ""),
                name_section.getStringList("hover-message")
            );

            ChatSection suffix = new ChatSection(
                suffix_section.getString("format", "%luckperms_suffix%"), 
                suffix_section.getString("click-command", ""),
                suffix_section.getStringList("hover-message")
            );
            
            ChatSection separator = new ChatSection(
                format_section.getString("chat-prefix", "&8&l» ") + format_section.getString("chat-color", "&r&7")
            );

            ChatFormat new_format = new ChatFormat(prefix, name, suffix, separator);
            new_format.format_name = format_name;
            new_format.priority = format_section.getInt("priority", 1);
            new_format.permission = format_section.getString("permission", "corechat." + format_name);
            formats.put(format_name, new_format);
        }
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if ((player == null) || !player.isValid())
        {
            return;
        }

        event.setCancelled(true);
        for (Player recipient : event.getRecipients())
        {
            ChatFormat format = formats.get(select_format(player));
            recipient.spigot().sendMessage(format.assemble_format(player, event.getMessage()));
        }
        plugin.getLogger().info(player.getName() + " » " + event.getMessage());
    }

    public String select_format(Player p)
    {
        int highest_priority = 1;
        String selected_format_name = "default";
        for (Map.Entry<String, ChatFormat> entry : formats.entrySet())
        {
            String format_name = entry.getKey();
            ChatFormat format = entry.getValue();
            if (p.hasPermission(format.permission) && (format.priority > highest_priority))
            {
                highest_priority = format.priority;
                selected_format_name = format_name;
            }
        }
        return selected_format_name;
    }
}
