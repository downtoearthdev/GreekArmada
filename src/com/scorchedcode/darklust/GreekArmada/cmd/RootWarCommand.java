package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.Armada;
import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.GreekWar;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joda.time.Hours;
import org.joda.time.Instant;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;

import java.math.BigDecimal;

public class RootWarCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());

            if (ap.hasFaction() && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
                if(strings.length == 1 || strings.length == 2) {
                    BigDecimal time = (strings.length == 2 && DarkInit.hasPerm((Player)commandSender, "greekarmada.time")) ? new BigDecimal(strings[1]).setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0.0).setScale(2, BigDecimal.ROUND_HALF_UP);
                    Armada opposing = DarkInit.getPlugin().getRegisteredArmadas().getArmada(strings[0]);
                    if(opposing != null) {
                        if (!GreekWar.instance) {
                            if(ap.getArmada().getGod() == null) {
                                commandSender.sendMessage(ChatColor.RED + "Please choose a god for your Armada before going to war!");
                                return;
                            }
                            if(ap.getArmada().getName().equals(opposing.getName())) {
                                commandSender.sendMessage(ChatColor.RED + "Please choose a different Armada for battle!");
                                return;
                            }
                            if(ap.getWarCoolDown() == 0 || Hours.hoursBetween(new Instant(ap.getWarCoolDown()), Instant.now()).getHours() > 24) {
                                new GreekWar(ap.getArmada(), opposing, time);
                                ap.setWarCoolDown(Instant.now().getMillis());
                            }
                            else {
                                MutableDateTime mutey = new MutableDateTime(ap.getWarCoolDown());
                                mutey.addHours(24);
                                commandSender.sendMessage(ChatColor.RED + "You must wait " + Hours.hoursBetween(new Instant(ap.getWarCoolDown()), mutey).getHours() + " more hourss before you can use this command again!");
                            }
                        }
                        else {
                            commandSender.sendMessage(ChatColor.RED + "There is already a war being fought!");
                        }
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + "No Armada with that name found!");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.AQUA + "/f greekwar ARMADANAME");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "You must be the owner of an Armada to use this command!");
            }
    }
}
