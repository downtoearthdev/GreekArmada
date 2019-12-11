package com.scorchedcode.darklust.GreekArmada.cmd;

import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RootServerInfoCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("discord")) {
            commandSender.sendMessage(ChatColor.DARK_PURPLE + "Come join our discord! https://discord.gg/EQAdag9");
        }

        if(subCommandBuilder.getName().equals("greeksite")) {
            commandSender.sendMessage(ChatColor.DARK_AQUA + "Join your fellow warriors at https://athenia.online/index.php/");
        }

        if(subCommandBuilder.getName().equals("devofathenia")) {

        }
    }
}
