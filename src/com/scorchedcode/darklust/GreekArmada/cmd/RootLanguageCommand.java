package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RootLanguageCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(subCommandBuilder.getName().equals("greekfrench")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            ap.getPlayer().sendMessage(ChatColor.AQUA + "Votre langue a été mise en français!`");
            ap.setLanguage(ArmadaPlayer.Language.FRENCH);
            ap.scoreBoardRefresh();
        }

        if(subCommandBuilder.getName().equals("greekenglish")) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            ap.getPlayer().sendMessage(ChatColor.AQUA + "Your language has been set to English!");
            ap.setLanguage(ArmadaPlayer.Language.ENGLISH);
            ap.scoreBoardRefresh();
        }
    }
}
