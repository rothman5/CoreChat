package me.rothman5.corechat;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

public class ChatFormat
{
    public int priority;
    public String permission;
    public String format_name;

    private ChatSection prefix;
    private ChatSection name;
    private ChatSection suffix;
    private ChatSection separator;
    
    public ChatFormat(ChatSection p, ChatSection n, ChatSection s, ChatSection sep)
    {
        prefix = p;
        name = n;
        suffix = s;
        separator = sep;
    }

    public BaseComponent[] assemble_format(Player p, String text)
    {
        if (p.hasPermission("corechat.placeholders"))
        {
            text = PlaceholderAPI.setPlaceholders(p, text);
        }

        if (p.hasPermission("corechat.colors"))
        {
            text = ChatColor.translateAlternateColorCodes('&', text);
        }
        
        ComponentBuilder format = new ComponentBuilder();
        format.append(prefix.assemble_section(p), FormatRetention.NONE);
        format.append(name.assemble_section(p), FormatRetention.NONE);
        format.append(suffix.assemble_section(p), FormatRetention.NONE);
        format.append(ChatColor.translateAlternateColorCodes('&', separator.content) + text, FormatRetention.NONE);
        return format.create();
    }
}
