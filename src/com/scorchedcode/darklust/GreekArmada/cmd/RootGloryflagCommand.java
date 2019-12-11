package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.FlagClaimListener;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.inventory.ItemManager;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RootGloryflagCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("gloryflag")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap.hasFaction() && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
                if(!ap.getArmada().hasClaim()) {
                    if (ap.getPlayer().getInventory().firstEmpty() != -1) {
                        ItemManager selectionDevice = new ItemManager(ChatColor.YELLOW + "Claim Selection Tool", Material.END_CRYSTAL, 1);
                        ((Player)commandSender).getInventory().setItemInMainHand(selectionDevice.getItem());
                        commandSender.sendMessage(ChatColor.AQUA + "Select two corners with left and right click!");
                        FlagClaimListener.armadaPlayers.put(ap, new Group<>(null, null));
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "You don't have enough room in your inventory!");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You already have claimed land! Use /f delflag to remove it!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must be the owner of an Armada to use this command!");

            }
        }

        if(subCommandBuilder.getName().equals("delflag")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap.hasFaction() && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
                if(ap.getArmada().hasClaim()) {
                    if(ap.getArmada().getPower() == 1000) {
                        ap.getArmada().unsetClaim();
                        commandSender.sendMessage(ChatColor.AQUA + "Your Armada's claim has been released!");
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "Your Armada does not have enough power! Current power: " + ap.getArmada().getPower());
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "Your Armada does not have a claim set!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must be the owner of an Armada to use this command!");
            }
        }
    }
}
