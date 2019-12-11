package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.Armada;
import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RootGreekInfoCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        Armada arma = DarkInit.getPlugin().getRegisteredArmadas().getArmada(((Player)commandSender).getLocation());
        if(arma != null) {
            String lengthDetermine = "=====" + arma.getFriendlyName() + "=====";
            commandSender.sendMessage(ChatColor.DARK_PURPLE + "=====" + arma.getName() + ChatColor.RESET + ChatColor.DARK_PURPLE + "=====");
            commandSender.sendMessage(ChatColor.RED + "HealPoint: " + ChatColor.GREEN + arma.getPower());
            commandSender.sendMessage(ChatColor.RED + "Owner: " + ChatColor.LIGHT_PURPLE + arma.getOwner().getName());
            commandSender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "God: " + ((arma.getGod() != null) ? arma.getGod().toString() : "None"));
            commandSender.sendMessage(ChatColor.RED + "Size: " + arma.getClaimSize());
            commandSender.sendMessage(ChatColor.RED + "Story:");
            commandSender.sendMessage(arma.getDescription());
            String endString = ChatColor.GREEN + "";
            for(int x = 0; x < lengthDetermine.length(); x++)
                endString = endString + "=";
            commandSender.sendMessage(endString);
        }
        else {
            commandSender.sendMessage(ChatColor.DARK_PURPLE + "=====AtheniaWorlds=====");
        }
    }
}
