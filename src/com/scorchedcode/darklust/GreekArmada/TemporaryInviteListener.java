package com.scorchedcode.darklust.GreekArmada;

import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.CopyOnWriteArrayList;

public class TemporaryInviteListener implements Listener {
    public static CopyOnWriteArrayList<Group<Player, Armada>> invites = new CopyOnWriteArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAcceptInvite(PlayerCommandPreprocessEvent e) {
        for(Group g : invites) {
            if (e.getPlayer().getName().equals(((Player)g.getA()).getName())) {
                if (e.getMessage().equals("/accept")) {
                    Armada group = (Armada)g.getB();
                    ArmadaPlayer arma = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
                    group.addMember(arma);
                    e.getPlayer().sendMessage(ChatColor.AQUA + "You have joined " + group.getName() + "!");
                    group.getOwner().getPlayer().sendMessage("" + ChatColor.AQUA + e.getPlayer() + "has joined your Armada!");
                    Bukkit.broadcastMessage("" + ChatColor.YELLOW + ChatColor.BOLD + e.getPlayer().getName()+ " joined the Armada " + group.getName() + ChatColor.RESET + ChatColor.YELLOW + " good luck to them, stay glorious and with honor with your enemies !");
                    invites.remove(g);
                    e.setCancelled(true);
                }
            }
        }
    }
}
