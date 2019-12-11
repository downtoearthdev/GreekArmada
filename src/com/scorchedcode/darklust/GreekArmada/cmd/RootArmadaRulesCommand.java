package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.Util;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RootArmadaRulesCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
        if(ap.hasFaction() && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
            if (strings.length >= 1) {
                if (Integer.parseInt(strings[0]) <= 10) {
                    if(strings[1].equals("delete")) {
                        ap.getArmada().getRules()[Integer.valueOf(strings[0])] = "";
                        commandSender.sendMessage(ChatColor.AQUA + "Rule deleted!");
                        return;
                    }
                    String fullRule = "";
                    for(int x = 1; x < strings.length; x++)
                        fullRule = fullRule + " " + strings[x];
                    ap.getArmada().getRules()[Integer.valueOf(strings[0])] = Util.parseColorString(fullRule);
                    commandSender.sendMessage(ChatColor.AQUA + "Rule set!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You can only have up to 10 rules set!");
                }
            } else {
                commandSender.sendMessage(ChatColor.AQUA + "/f greekrules # (delete | custom rules text)");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You must own an Armada to use this command!");
        }
    }
}
