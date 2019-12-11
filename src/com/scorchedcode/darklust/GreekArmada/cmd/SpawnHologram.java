package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.Util;
import org.anhcraft.spaciouslib.entity.Hologram;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.apache.logging.log4j.core.net.Priority;
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

public class SpawnHologram implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && DarkInit.hasPerm(((Player) commandSender), "greekarmada.hologram")) {
            if (strings.length == 1) {
                if (strings[0].equals("factionkills")) {
                    Util.spawnHologram("factionkills", ((Player) commandSender).getLocation().add(0, 2, 0));
                    commandSender.sendMessage(ChatColor.AQUA + "Faction Kill hologram set!");
                    return true;
                }
                if (strings[0].equals("playerkills")) {
                    Util.spawnHologram("playerkills", ((Player) commandSender).getLocation().add(0, 2, 0));
                    commandSender.sendMessage(ChatColor.AQUA + "Player Kill hologram set!");
                    return true;

                }
                if (strings[0].equals("factionwars")) {
                    Util.spawnHologram("factionwars", ((Player) commandSender).getLocation().add(0, 2, 0));
                    commandSender.sendMessage(ChatColor.AQUA + "Faction War hologram set!");
                    return true;

                }
                if (strings[0].equals("playerlevels")) {
                    Util.spawnHologram("playerlevels", ((Player) commandSender).getLocation().add(0, 2, 0));
                    commandSender.sendMessage(ChatColor.AQUA + "Player Level hologram set!");
                    return true;

                }
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!e.getPlayer().isValid())
            return;
        for (int x = 0; x < DarkInit.getPlugin().getSpawnHolograms().length; x++) {
            if (DarkInit.getPlugin().getSpawnHolograms()[x] != null)
                DarkInit.getPlugin().getSpawnHolograms()[x].addViewer(e.getPlayer().getUniqueId());

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(!e.getPlayer().isValid())
            return;
        for (int x = 0; x < DarkInit.getPlugin().getSpawnHolograms().length; x++) {
            if (DarkInit.getPlugin().getSpawnHolograms()[x] != null)
                DarkInit.getPlugin().getSpawnHolograms()[x].addViewer(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerWorldChange(PlayerChangedWorldEvent e) {
        if(!e.getPlayer().isValid())
            return;
        for (int x = 0; x < DarkInit.getPlugin().getSpawnHolograms().length; x++) {
            if (DarkInit.getPlugin().getSpawnHolograms()[x] != null)
                DarkInit.getPlugin().getSpawnHolograms()[x].addViewer(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if(!e.getPlayer().isValid())
            return;
        for (int x = 0; x < DarkInit.getPlugin().getSpawnHolograms().length; x++) {
            if (DarkInit.getPlugin().getSpawnHolograms()[x] != null)
                DarkInit.getPlugin().getSpawnHolograms()[x].removeViewer(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if(!e.getPlayer().isValid())
            return;
        for (int x = 0; x < DarkInit.getPlugin().getSpawnHolograms().length; x++) {
            if (DarkInit.getPlugin().getSpawnHolograms()[x] != null)
                DarkInit.getPlugin().getSpawnHolograms()[x].addViewer(e.getPlayer().getUniqueId());
        }
    }
}
