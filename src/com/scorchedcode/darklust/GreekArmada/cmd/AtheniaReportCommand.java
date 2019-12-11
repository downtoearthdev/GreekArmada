package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.Util;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.io.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AtheniaReportCommand extends CommandRunnable implements Listener {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("atheniareport")) {
            if(strings.length >= 2) {
                String report = "";
                for(String st : strings)
                    report = report + " " + st;
                sendModMail(commandSender.getName() + " has reported: " + report);
                commandSender.sendMessage(ChatColor.AQUA + Util.getLocaleString("armada-report-thankyou-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName())));
            }
            else {
                commandSender.sendMessage(ChatColor.AQUA + "/f atheniareport <armadaname> <name of cheat>");
            }
        }

        if(subCommandBuilder.getName().equals("atheniareportp")) {
            if(strings.length >= 2) {
                String report = "";
                for(String st : strings)
                    report = report + " " + st;
                sendModMail(commandSender.getName() + " has reported " + strings[0] + " for " + report);
                commandSender.sendMessage(ChatColor.AQUA + Util.getLocaleString("armada-report-thankyou-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName())));
            }
            else {
                commandSender.sendMessage(ChatColor.AQUA + "/f atheniareportP <playername> <reason for report>");
            }
        }
    }

    private void sendModMail(String report) {
        if(!new File(DarkInit.getPlugin().getDataFolder() + "/modmail/").exists())
            new File(DarkInit.getPlugin().getDataFolder() + "/modmail/").mkdir();
        FileManager newReport = new FileManager(new File(DarkInit.getPlugin().getDataFolder() + "/modmail/" + UUID.randomUUID() + ".txt"));
        try {
            newReport.create().write(report);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onModMailRead(PlayerCommandPreprocessEvent e) {
        if(DarkInit.hasPerm(e.getPlayer(), "greekarmada.modmail") && e.getMessage().equals("/readthemail")) {
            for(File f : new File(DarkInit.getPlugin().getDataFolder() + "/modmail/").listFiles()) {
                try {
                    e.getPlayer().sendMessage(ChatColor.RED + new FileManager(f).readAsString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            File dir = new File(DarkInit.getPlugin().getDataFolder() + "/modmail/");
            for(File f : dir.listFiles())
                f.delete();
            e.setCancelled(true);
        }
    }
}
