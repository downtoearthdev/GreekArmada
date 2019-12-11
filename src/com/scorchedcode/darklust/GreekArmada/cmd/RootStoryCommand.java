package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import com.scorchedcode.darklust.GreekArmada.Util;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class RootStoryCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
        if(ap != null && ap.getRank() == ArmadaPlayer.Rank.OWNER) {
            if(((Player)commandSender).getInventory().getItemInMainHand().getType() != Material.BOOK_AND_QUILL) {
                commandSender.sendMessage(ChatColor.AQUA + "Write your Armada's story in a book!");
                commandSender.sendMessage(ChatColor.AQUA + "Once you're done, hold the book and type this command!");
            }
            else {
                BookMeta bm = (BookMeta)((Player)commandSender).getInventory().getItemInMainHand().getItemMeta();
                //BookManager bm = new BookManager(((Player)commandSender).getInventory().getItemInMainHand());
                ap.getArmada().setDescription(ChatColor.RESET + Util.parseColorString(ChatColor.stripColor(bm.getPage(1))));
                commandSender.sendMessage(ChatColor.AQUA + "New story recorded!");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You must be the owner of an Armada to use this command!");
        }
    }
}
