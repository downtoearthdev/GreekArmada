package com.scorchedcode.darklust.GreekArmada.cmd;

import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RootCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        for(SubCommandBuilder sub : commandBuilder.getSubCommands()) {
            if(commandBuilder.getSubCommands().indexOf(sub) != 0)
                commandSender.sendMessage(ChatColor.YELLOW + "/f " + sub.getName());
        }
    }
}
