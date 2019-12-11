package com.scorchedcode.darklust.GreekArmada.cmd;

import com.coloredcarrot.jsonapi.impl.JsonClickEvent;
import com.coloredcarrot.jsonapi.impl.JsonColor;
import com.coloredcarrot.jsonapi.impl.JsonHoverEvent;
import com.coloredcarrot.jsonapi.impl.JsonMsg;
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

public class RootArmadaList extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        if(DarkInit.getPlugin().getRegisteredArmadas().listArmadas().size() != 0) {
            commandSender.sendMessage(""+ ChatColor.YELLOW + ChatColor.BOLD + "Armadas:");
            for(Armada a : DarkInit.getPlugin().getRegisteredArmadas().listArmadas()) {
                JsonMsg msg = new JsonMsg(a.getName());
                msg.hoverEvent(JsonHoverEvent.showText(new JsonMsg("Owner: " + a.getOwner().getName(), JsonColor.YELLOW)));
                msg.clickEvent(JsonClickEvent.suggestCommand("/msg " + a.getOwner().getName()));
                msg.send((Player)commandSender);
            }
        }
        else {
            commandSender.sendMessage(ChatColor.YELLOW + "There are no Armadas to list!");
        }
        /*Bukkit.broadcastMessage("" +DarkInit.getPlugin().getRegisteredArmadas().getArmadaPlayers().size());
        for(ArmadaPlayer a : DarkInit.getPlugin().getRegisteredArmadas().getArmadaPlayers())
            Bukkit.broadcastMessage(a.getName());*/
    }
}
