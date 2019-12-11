package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.Armada;
import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RootJoinLeaveCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("timetogo")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap.hasFaction()) {
                if(ap.getRank() == ArmadaPlayer.Rank.OWNER) {
                    Bukkit.broadcastMessage(ap.getArmada().getName() + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + " has disbanded!");
                    DarkInit.getPlugin().getRegisteredArmadas().disbandArmada(ap.getArmada());
                    return;
                }
                ap.getArmada().getOwner().getPlayer().sendMessage(ChatColor.GOLD + ap.getName() + " has left your Armada.");
                commandSender.sendMessage(ChatColor.AQUA + "You have left " + ap.getArmada().getName());
                ap.leaveArmada();
            }
            else
                commandSender.sendMessage(ChatColor.RED + "You must belong to an Armada to leave it!");
        }

        if(subCommandBuilder.getName().equals("joingreek")) {
            if(strings.length == 1) {
                Armada arm = DarkInit.getPlugin().getRegisteredArmadas().getArmada(strings[0]);
                ArmadaPlayer p = (DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName()) != null) ? DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName()) : new ArmadaPlayer(((Player)commandSender));
                if(arm != null) {
                    if(!arm.isInvite()) {
                        if(!p.hasFaction()) {
                            arm.addMember(p);
                            Bukkit.broadcastMessage("" + ChatColor.YELLOW + ChatColor.BOLD + commandSender.getName() + " joined the Armada " + arm.getName() + ChatColor.RESET + ChatColor.YELLOW + " good luck to them, stay glorious and with honor with your enemies!");
                            return;
                        }
                        else {
                            commandSender.sendMessage(ChatColor.RED + "You must leave your Armada before joining a new one!");
                        }
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "That armada is invite only!");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "Unrecognized armada");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.AQUA + "/f joingreek ARMADA");
            }
        }
    }
}
