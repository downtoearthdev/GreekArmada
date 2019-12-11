package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RootChangeRankCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if (subCommandBuilder.getName().equals("glory")) {
            if(strings.length == 1) {
                ArmadaPlayer owner = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
                if(owner != null && owner.getRank() == ArmadaPlayer.Rank.OWNER) {
                    ArmadaPlayer rankPlayer = owner.getArmada().getPlayer(strings[0]);
                    if(rankPlayer != null) {
                        if(rankPlayer.getRank() == ArmadaPlayer.Rank.MODERATOR || rankPlayer.getRank() == ArmadaPlayer.Rank.OWNER) {
                            commandSender.sendMessage(ChatColor.RED + "You cannot rank this member any higher!");
                            return;
                        }

                        owner.getArmada().rankMember(rankPlayer, false);
                        commandSender.sendMessage(ChatColor.AQUA + "Member ranked up to " + rankPlayer.getRank());
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "Member not found in your Armada.");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You must own an Armada to rank members!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.AQUA + "/f glory MEMBERNAME");
            }
        }

        if (subCommandBuilder.getName().equals("demote")) {
            if(strings.length == 1) {
                ArmadaPlayer owner = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
                if(owner != null && owner.getRank() == ArmadaPlayer.Rank.OWNER) {
                    ArmadaPlayer rankPlayer = owner.getArmada().getPlayer(strings[0]);
                    if(rankPlayer != null) {
                        if(rankPlayer.getRank() == ArmadaPlayer.Rank.WARRIOR) {
                            commandSender.sendMessage(ChatColor.RED + "You cannot rank this member any lower!");
                            return;
                        }

                        owner.getArmada().rankMember(rankPlayer, true);
                        commandSender.sendMessage(ChatColor.AQUA + "Member ranked down to " + rankPlayer.getRank());
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "Member not found in your Armada.");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You must own an Armada to rank members!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.AQUA + "/f demote MEMBERNAME");
            }
        }
    }
}