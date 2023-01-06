package me.rothman.corechat.chat;

import me.rothman.corechat.utils.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        ChatFormat playerFormat = new ChatFormat();
        int curPriority = 0;
        for (String format : FileManager.getFormats().keySet()) {
            ChatFormat curFormat = FileManager.getFormats().get(format);
            if (e.getPlayer().hasPermission(curFormat.getPermission()) && curPriority < curFormat.getPriority()) {
                curPriority = curFormat.getPriority();
                playerFormat = curFormat;
            }
        }
        TextComponent prefix = playerFormat.buildSection(e.getPlayer(), "prefix");
        TextComponent name = playerFormat.buildSection(e.getPlayer(), "name");
        TextComponent suffix = playerFormat.buildSection(e.getPlayer(), "suffix");
        String msg = ChatColor.translateAlternateColorCodes('&', " " + playerFormat.getChatColor() + e.getMessage());
        TextComponent message = new TextComponent(msg);
        e.getPlayer().spigot().sendMessage(prefix, name, suffix, message);
    }

}
