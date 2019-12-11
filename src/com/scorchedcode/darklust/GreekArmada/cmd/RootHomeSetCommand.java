package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RootHomeSetCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("sethome")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap != null && ap.hasFaction() && (ap.getRank() == ArmadaPlayer.Rank.OWNER || ap.getRank() == ArmadaPlayer.Rank.MODERATOR)) {
                if(!ap.getArmada().hasHome()) {
                    if(ap.getArmada().isWithinClaim(ap.getPlayer().getLocation())) {
                        ap.getArmada().setHome(ap.getPlayer().getLocation());
                        commandSender.sendMessage(ChatColor.AQUA + "Armada home set!");
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "You must be within your Armada's claim to use this");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "Your Armada already has a home! Use /f delhome to remove it!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must be the moderator or owner of an Armada to use this command!");
            }
        }
        else if (subCommandBuilder.getName().equals("delhome")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap != null && ap.hasFaction() && (ap.getRank() == ArmadaPlayer.Rank.OWNER || ap.getRank() == ArmadaPlayer.Rank.MODERATOR)) {
                if(ap.getArmada().hasClaim()) {
                    if (ap.getArmada().hasHome()) {
                        ap.getArmada().setHome(null);
                        commandSender.sendMessage(ChatColor.AQUA + "Armada home removed!");
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Your Armada must have a home first! Use /f sethome to create it!");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You must have a claim set first! Use /f gloryflag");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must be the moderator or owner of an Armada to use this command!");
            }

        }
        else if (subCommandBuilder.getName().equals("home")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap != null && ap.hasFaction()) {
                if(ap.getArmada().hasHome()) {
                    ap.getPlayer().teleport(ap.getArmada().getHome());
                    commandSender.sendMessage(ChatColor.AQUA + "Teleported to Armada home");
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "Your Armada does not have a home set!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must belong to an Armada to use this command");
            }
        }
    }
}
