package me.rothman5.corechat;

import java.util.List;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ChatSection
{
    public String content;
    public String command;
    public String message;

    public ChatSection(String cont)
    {
        content = cont;
        command = "";
        message = "";
    }

    public ChatSection(String cont, String cmd, List<String> lines)
    {
        content = cont;
        command = cmd;
        message = "";
        if (!lines.isEmpty())
        {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < lines.size(); row++)
            {
                sb.append(lines.get(row));
                if (row != lines.size())
                {
                    sb.append("\n");
                }
            }
            message = sb.toString().trim();
        }
    }

    public BaseComponent[] assemble_section(Player p)
    {
        ComponentBuilder section = new ComponentBuilder();
        if (content.length() == 0)
        {
            return section.create();
        }
        
        String text_content = PlaceholderAPI.setPlaceholders(p, content);
        text_content = ChatColor.translateAlternateColorCodes('&', text_content);
        section.append(text_content);

        if (command.length() != 0)
        {
            String click_command = PlaceholderAPI.setPlaceholders(p, command);
            ClickEvent click_event = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, click_command);
            section.event(click_event);
        }

        if (message.length() != 0)
        {
            String hover_message = PlaceholderAPI.setPlaceholders(p, message);
            hover_message = ChatColor.translateAlternateColorCodes('&', hover_message);
            HoverEvent hover_event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover_message));
            section.event(hover_event);
        }
        return section.create();
    }
}
