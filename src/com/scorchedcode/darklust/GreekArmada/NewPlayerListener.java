package com.scorchedcode.darklust.GreekArmada;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.joda.time.Instant;

import java.math.BigDecimal;

public class NewPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        if(!ap.hasScoreboard())
            ap.setupScoreboard();
        else
            ap.getScoreboard().showTo(e.getPlayer());
            //ap.startScoreBoardRunner();
        if(ap.hasFaction())
            ap.getArmada().setLastlogin(0);
        new FidelityGainRunner(e.getPlayer()).runTaskTimerAsynchronously(DarkInit.getPlugin(), 1200L, 1200L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        ap.getScoreboard().hideFrom(e.getPlayer());
        if(ap.hasFaction()) {
            for (ArmadaPlayer a : ap.getArmada().getPlayers()) {
                if(a.getPlayer() != null && a.getPlayer().isOnline() && !a.getPlayer().equals(ap.getPlayer()))
                    return;
            }
            ap.getArmada().setLastlogin(Instant.now().getMillis());
        }
    }

    public class FidelityGainRunner extends BukkitRunnable {

        Player player;
        public FidelityGainRunner(Player player) {
            this.player = player;
        }
        @Override
        public void run() {
            if(!player.isOnline()) {
                cancel();
                return;
            }
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(player.getName());
            ap.setTimePlayed(ap.getTimePlayed()+1);
            if(ap.getTimePlayed() % 60 == 0) {
                BigDecimal bd = new BigDecimal(ap.getFidelity() + 0.001f);
                bd.setScale(3, BigDecimal.ROUND_HALF_UP);
                ap.setFidelity(bd.floatValue());
                player.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + Util.getLocaleString("armada-gain-fidelity-msg", ap));
            }
        }
    }
}
