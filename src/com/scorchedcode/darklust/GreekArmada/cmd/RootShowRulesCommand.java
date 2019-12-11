package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RootShowRulesCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
        if(ap.hasFaction()) {
            commandSender.sendMessage(ChatColor.YELLOW + "=====" + ChatColor.RESET + ap.getArmada().getName() + ChatColor.YELLOW + " Rules=====");
            for(int x = 0; x < ap.getArmada().getRules().length; x++) {
                if(ap.getArmada().getRules()[x] != null && !ap.getArmada().getRules()[x].equals(""))
                    commandSender.sendMessage("" + ChatColor.GOLD + x + ") " + ap.getArmada().getRules()[x]);
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You must belong to an Armada to use this command!");
        }
    }
}
