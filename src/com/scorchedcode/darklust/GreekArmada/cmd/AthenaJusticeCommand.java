package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.Armada;
import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.Util;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AthenaJusticeCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if (strings.length == 1) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
            if (ap.hasFaction() && (ap.getRank() == ArmadaPlayer.Rank.OWNER || ap.getRank() == ArmadaPlayer.Rank.MODERATOR)) {
                ArmadaPlayer offender = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(strings[0]);
                if (offender != null && ap.getArmada().getPlayer(strings[0]) != null) {
                    if (offender.getRank() == ArmadaPlayer.Rank.MODERATOR || offender.getRank() == ArmadaPlayer.Rank.OWNER) {
                        commandSender.sendMessage(ChatColor.RED + Util.getLocaleString("armada-atheniajustice-remove-error-msg", ap));
                        return;
                    }
                    offender.getPlayer().sendMessage(ChatColor.RED + Util.getLocaleString("armada-atheniajustice-remove-msg", offender));
                    commandSender.sendMessage(ChatColor.AQUA + offender.getPlayer().getName() + " " + Util.getLocaleString("armada-atheniajustice-remove-msgtwo", ap));
                    offender.leaveArmada();
                } else {
                    commandSender.sendMessage(ChatColor.RED + Util.getLocaleString("armada-member-not-found-msg", ap));
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + Util.getLocaleString("armada-must-be-opormod-msg", ap));
            }
        }
        else {
            commandSender.sendMessage(ChatColor.AQUA + "/f atheniajustice PLAYER");
        }
    }
}
