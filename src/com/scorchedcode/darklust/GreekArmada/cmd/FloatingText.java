package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.entity.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Collections;

public class FloatingText implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1 && strings[0].equals("list")) {
            int x = 0;
            for(Hologram h : DarkInit.getPlugin().getCustomHolograms()) {
                x++;
                commandSender.sendMessage(x + ") " + h.getLines().get(0));
            }
            return true;
        }
        if(strings.length >= 2) {
            if(strings.length == 2 && strings[0].equals("delete")) {
                if(Integer.valueOf(strings[1])-1 < DarkInit.getPlugin().getCustomHolograms().size()) {
                    DarkInit.getPlugin().getCustomHolograms().get(Integer.valueOf(strings[1])-1).remove();
                    DarkInit.getPlugin().getCustomHolograms().remove(Integer.valueOf(strings[1])-1);
                }
                commandSender.sendMessage(ChatColor.AQUA + "Hologram removed!");
                return true;
            }
            Hologram newHolo = new Hologram(((Player)commandSender).getLocation().add(0, 3, 0));
            String[] description = strings;
            ArrayList<String> finalFormat = new ArrayList<>();
            //Color color = ColorUtils.toBukkitColor(java.awt.Color.getColor(strings[1]));
            ChatColor color = ChatColor.valueOf(strings[strings.length-1]);
            //String [] deconstructed = description.split(" ");
            for(int x = 0; x < description.length-1; x+=5) {
                String temp = "";
                for(int y = 0; y < 5; y++) {
                    if(x+y < description.length-1)
                        temp = temp + " " + description[x+y];
                }
                finalFormat.add((color + temp).trim());
                //newHolo.addLine("" + ChatColor.BOLD + color + temp);
            }
            Collections.reverse(finalFormat);
            String[] finalArray = finalFormat.toArray(new String[finalFormat.size()]);
            newHolo.addLines(finalArray);
            newHolo.buildPackets();
            for(Player p : Bukkit.getOnlinePlayers())
                newHolo.addViewer(p.getUniqueId());
            DarkInit.getPlugin().getCustomHolograms().add(newHolo);
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!e.getPlayer().isValid())
            return;
        for (Hologram hologram : DarkInit.getPlugin().getCustomHolograms())
            hologram.addViewer(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(!e.getPlayer().isValid())
        return;
        for (Hologram hologram : DarkInit.getPlugin().getCustomHolograms())
            hologram.addViewer(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerWorldChange(PlayerChangedWorldEvent e) {
        if (!e.getPlayer().isValid())
            return;
        for (Hologram hologram : DarkInit.getPlugin().getCustomHolograms())
            hologram.addViewer(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if(!e.getPlayer().isValid())
            return;
            for (Hologram hologram : DarkInit.getPlugin().getCustomHolograms())
                hologram.removeViewer(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!e.getPlayer().isValid())
            return;
        for (Hologram hologram : DarkInit.getPlugin().getCustomHolograms())
            hologram.addViewer(e.getPlayer().getUniqueId());
    }
}
