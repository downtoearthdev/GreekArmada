package com.scorchedcode.darklust.GreekArmada;

import org.anhcraft.spaciouslib.entity.Hologram;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Util {

    public static void sendLocaleMessage(String message, ArmadaPlayer player) {

    }

    public static void broadcastLocaleMessage(String message) {

    }

    public static String getLocaleString(String string, ArmadaPlayer player) {
        ArmadaPlayer.Language lang = player.getLanguage();
        for(Group group : GreekArmada.langStrings) {
            if(group.getA() == lang) {
                for(Group s : (ArrayList<Group<String, String>>)group.getB()) {
                    if(s.getA().equals(string)) {
                        return (String)s.getB();
                    }
                }
            }
        }
        return "Not Found";
    }

    public static String toProperUppercaseString(String string) {
        String upperCase = string.toLowerCase().substring(0, 1).toUpperCase();
        return upperCase + string.substring(1).toLowerCase();
    }
    public static String parseColorString(String string) {
        return string.replaceAll("&", "§");
    }
    public static void spawnHologram(String type, Location loc) {
        String headerTest = null;
        int index = -1;
        switch (type) {
            case "factionkills":
                headerTest = "" + ChatColor.GREEN + "Top 10 Factions By Kill";
                index = 0;
                break;
            case "playerkills":
                headerTest = "" + ChatColor.GREEN + "Top 10 Players By Kill";
                index = 1;
                break;
            case "factionwars":
                headerTest = "" + ChatColor.GREEN + "Top 10 Factions By Wars Won";
                index = 2;
                break;
            case "playerlevels":
                headerTest = "" + ChatColor.GREEN + "Top 10 Players By Level";
                index = 3;
                break;
            default:
                headerTest = "UNDEFINED";
        }
        Hologram spawn = new Hologram(loc);
        spawn.addLine(headerTest);
        spawn.buildPackets();
        if(DarkInit.getPlugin().getSpawnHologram(index) != null)
            DarkInit.getPlugin().getSpawnHologram(index).remove();
        DarkInit.getPlugin().setSpawnHologram(spawn, index);
        for(Player p : Bukkit.getOnlinePlayers())
            spawn.addViewer(p.getUniqueId());
    }

}
