package com.scorchedcode.darklust.GreekArmada;

import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PVPListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmadaPlayerMurder(EntityDeathEvent e) {
        if(e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
            ArmadaPlayer killed = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName());
            ArmadaPlayer killer = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getKiller().getName());
            killed.increaseDeaths();
            killer.increaseKills();
            if(killer.hasFaction() && killed.hasFaction()) {
                if(killer.getArmada().equals(killed.getArmada()))
                    return;
            }
            if(killer.hasFaction()) {
                for(ArmadaPlayer p : killer.getArmada().getPlayers()) {
                    p.getPlayer().sendMessage(ChatColor.AQUA + killer.getName() + " has gained 2 HealthPoint for the Armada!");
                }
                killer.getArmada().gainPower();
                killer.getArmada().increaseKills();
            }
            if(killed.hasFaction()) {
                for(ArmadaPlayer p : killed.getArmada().getPlayers()) {
                    p.getPlayer().sendMessage(ChatColor.RED + killed.getName() + " has lost 5 HealthPoint from the Armada.");
                }
                killed.getArmada().losePower();
                killed.getArmada().increaseDeath();
                if(killed.getArmada().getPower() == 0) {
                    killed.getArmada().unsetClaim();
                    Bukkit.broadcastMessage(ChatColor.RED + "The Armada " + killed.getArmada().getName() + " has lost all HealthPoint and has lost their claim!");
                }
            }
        }
        else if(!(e.getEntity() instanceof Player) && e.getEntity().getKiller() instanceof Player) {
            ArmadaPlayer killer = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getKiller().getName());
            killer.scoreBoardRefresh();
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakInArmada(BlockBreakEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        Armada treadOn = DarkInit.getPlugin().getRegisteredArmadas().getArmada(e.getBlock().getLocation());
        if(treadOn != null) {
            if(ap.hasFaction() && !ap.getArmada().equals(treadOn)) {
                e.setCancelled(true);
                ap.getPlayer().sendMessage(ChatColor.RED + "You do not belong to this Armada!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlaceInArmada(BlockPlaceEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        Armada treadOn = DarkInit.getPlugin().getRegisteredArmadas().getArmada(e.getBlock().getLocation());
        if(treadOn != null) {
            if(ap.hasFaction() && !ap.getArmada().equals(treadOn)) {
                e.setCancelled(true);
                ap.getPlayer().sendMessage(ChatColor.RED + "You do not belong to this Armada!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplodeInArmada(EntityExplodeEvent e) {
        for(Block b : e.blockList()) {
            if (DarkInit.getPlugin().getRegisteredArmadas().getArmada(b.getLocation()) != null) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractInArmada(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
            Armada treadOn = DarkInit.getPlugin().getRegisteredArmadas().getArmada(e.getPlayer().getLocation());
            if (treadOn != null) {
                if (ap.hasFaction() && !ap.getArmada().equals(treadOn)) {
                    e.setCancelled(true);
                    ap.getPlayer().sendMessage(ChatColor.RED + "You do not belong to this Armada!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrachmaeAccrue(UserBalanceUpdateEvent e) {
        new BukkitRunnable() {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
            @Override
            public void run() {
                ap.scoreBoardRefresh();
            }
        }.runTaskLaterAsynchronously(DarkInit.getPlugin(), 60L);
    }
    /*@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamageInArmada(EntityDamageByEntityEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getDamager().getName());
        Armada treadOn = DarkInit.getPlugin().getRegisteredArmadas().getArmada(e.getDamager().getLocation());
        if(treadOn != null) {
            if(ap.hasFaction() && !ap.getArmada().equals(treadOn)) {
                e.setCancelled(true);
                ap.getPlayer().sendMessage(ChatColor.RED + "You do not belong to this Armada!");
            }
        }
    }*/
}
