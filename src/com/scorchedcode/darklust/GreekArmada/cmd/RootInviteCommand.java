package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.Armada;
import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.TemporaryInviteListener;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RootInviteCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("i")) {
            if(strings.length == 1) {
                ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(strings[0]);
                ArmadaPlayer op = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
                if(op != null && op.getRank() == ArmadaPlayer.Rank.OWNER) {
                    if (ap != null && ap.hasFaction()) {
                        commandSender.sendMessage(ChatColor.RED + "This player already belongs to an Armada");
                        return;
                    }
                    commandSender.sendMessage(ChatColor.AQUA + "You have invited " + strings[0] + " to join the Armada.");
                    Bukkit.getPlayer(strings[0]).sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "You have been invited to join " + op.getArmada().getName());
                    Bukkit.getPlayer(strings[0]).sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Type /accept to accept this invitation!");
                    TemporaryInviteListener.invites.add(new Group(Bukkit.getPlayer(strings[0]), op.getArmada()));
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You must be the owner of an Armada to use this command!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.AQUA + "/f i PLAYER");
            }
        }

        if(subCommandBuilder.getName().equals("greekrecruit")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap != null && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
                ap.getArmada().setInvite(false);
                commandSender.sendMessage(ChatColor.AQUA + "Armada is open to the public for joining");
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must be the owner of an Armada to use this command!");
            }
        }

        if(subCommandBuilder.getName().equals("greekdeny")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if(ap != null && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
                ap.getArmada().setInvite(true);
                commandSender.sendMessage(ChatColor.AQUA + "Armada is private and can only be joined by invite");
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must be the owner of an Armada to use this command!");
            }
        }
    }
}
