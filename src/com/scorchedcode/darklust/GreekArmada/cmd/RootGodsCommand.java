package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.God;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RootGodsCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("gods")) {
            if(strings.length == 1) {
                ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
                if(ap != null && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
                    if(God.valueOf(strings[0]) != null) {
                        ap.getArmada().setGod(God.valueOf(strings[0]));
                        Bukkit.broadcastMessage(ChatColor.AQUA + "The power of mighty " + ChatColor.RED + ChatColor.BOLD + ap.getArmada().getGod().toString() + ChatColor.RESET + ChatColor.AQUA + " is with " + ChatColor.RESET + ap.getArmada().getName());

                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "This god was not recognized!");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You must be owner of an Armada to use this command!");
                }
            }
        }

        if(subCommandBuilder.getName().equals("greekpersonal")) {
            if(strings.length == 1) {
                ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
                if(ap != null) {
                    if(God.valueOf(strings[0]) != null) {
                        ap.setGod(God.valueOf(strings[0]));
                        Bukkit.broadcastMessage(ChatColor.AQUA + "The power of mighty " + ChatColor.RED + ChatColor.BOLD + ap.getGod().toString() + ChatColor.RESET + ChatColor.AQUA + " is with " + ChatColor.RESET + ap.getFancyName());
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "This god was not recognized!");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You must be owner of an Armada to use this command!");
                }
            }
        }
    }
}
