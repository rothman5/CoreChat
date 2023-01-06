package me.rothman.corechat.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rothman.corechat.utils.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFormat {

    private int priority;
    private String permission;
    private String chatColor;
    private String rawFormat;
    private Map<String, Object> options;
    private ConfigurationSection section;

    public ChatFormat() {
        priority = 1;
        permission = "";
        chatColor = "&7";
        rawFormat = "%player_name% &8&8&l»";
        options = new HashMap<>();
    }

    public ChatFormat(String formatName) {
        section = FileManager.getConfig("config.yml").getConfigurationSection("chat-formats." + formatName);
        loadSection();
    }

    public void loadSection() {
        if (section != null && !section.getKeys(false).isEmpty()) {
            priority = section.getInt("priority", 1);
            permission = section.getString("permission", "corechat.default");
            chatColor = section.getString("chat-color", "&7");
            rawFormat = section.getString("format", "%player_displayname% &8&l»");
            ConfigurationSection optionSection = section.getConfigurationSection("options");
            if (optionSection != null && !optionSection.getKeys(false).isEmpty())
                options = optionSection.getValues(false);
        }
    }

    public TextComponent buildSection(Player p, String type) {
        String text = "";
        switch (type) {
            case "prefix":
                text = rawFormat.split("%player_(name|displayname)%")[0];
                break;
            case "name":
                Pattern pattern = Pattern.compile("%player_(name|displayname)%");
                Matcher matcher = pattern.matcher(rawFormat);
                if (matcher.find()) text = matcher.group();
                break;
            case "suffix":
                text = rawFormat.split("%player_(name|displayname)%")[1];
                break;
        }
        text = PlaceholderAPI.setPlaceholders(p, text);
        text = ChatColor.translateAlternateColorCodes('&', text);
        TextComponent tc = new TextComponent(text);

        String clickCommand = (String) options.get(type + "-click-command");
        if (!clickCommand.isEmpty()) {
            clickCommand = PlaceholderAPI.setPlaceholders(p, clickCommand);
            clickCommand = ChatColor.translateAlternateColorCodes('&', clickCommand);
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickCommand));
        }

        @SuppressWarnings("unchecked")
        List<String> messageList = (List<String>) options.get(type + "-hover-message");
        if (!messageList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String line : messageList) sb.append(line).append("\n");
            String hoverMessage = PlaceholderAPI.setPlaceholders(p, sb.toString().trim());
            hoverMessage = ChatColor.translateAlternateColorCodes('&', hoverMessage);
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverMessage)));
        }
        return tc;
    }

    public String getPermission() {
        return permission;
    }

    public int getPriority() {
        return priority;
    }

    public String getChatColor() {
        return chatColor;
    }

}
