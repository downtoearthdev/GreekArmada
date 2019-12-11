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

public class RootNewCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(commandSender instanceof Player) {
          if(strings.length == 1) {
              ArmadaPlayer p = (DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName()));
            if(!p.hasFaction()) {
                DarkInit.getPlugin().getRegisteredArmadas().addArmada(new Armada(strings[0], p));
                Bukkit.broadcastMessage("" + ChatColor.YELLOW + ChatColor.BOLD + commandSender.getName() + " created the new Armada " + DarkInit.getPlugin().getRegisteredArmadas().getArmada(strings[0].replaceAll("&[\\d\\D]", "")).getName() + ChatColor.RESET + ChatColor.YELLOW + " in Athenia, the 12 gods are proud of him, we wish him to be victorious and filled with glory !");
                return;
            }
            else {
                if(p.getArmada().getOwner().equals(p)) {
                    String oldName = p.getArmada().getName();
                    p.getArmada().setName(strings[0]);
                    Bukkit.broadcastMessage(oldName + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + " has changed their name to " + ChatColor.RESET + p.getArmada().getName() + ChatColor.YELLOW + ChatColor.BOLD + "!");
                    return;
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "You must be the owner of your Armada to change the name!");
                }
            }
          }
        }
        commandSender.sendMessage(ChatColor.AQUA + "/f new ARMADANAME");
    }


}
