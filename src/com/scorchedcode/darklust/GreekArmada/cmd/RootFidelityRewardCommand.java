package com.scorchedcode.darklust.GreekArmada.cmd;

import com.scorchedcode.darklust.GreekArmada.ArmadaPlayer;
import com.scorchedcode.darklust.GreekArmada.DarkInit;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RootFidelityRewardCommand extends CommandRunnable {
    @Override
    public void run(CommandBuilder commandBuilder, SubCommandBuilder subCommandBuilder, CommandSender commandSender, String[] strings, String s) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(commandSender.getName());
        if(ap.getUnclaimedRewards() > 0) {
            for(int x = 0; x < ap.getUnclaimedRewards(); x++){
                ItemStack random = new ItemStack(Material.AIR);
                while(!random.getType().isItem())
                    random = new ItemStack(Material.values()[new Random().nextInt(Material.values().length-1)]);
                commandSender.sendMessage("" + ChatColor.AQUA + ChatColor.BOLD + "Congratulations! You received 1x " + random.getType().toString() + "!");
                ((Player)commandSender).getInventory().addItem(random);
            }
            ap.zeroUnclaimedRewards();

        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You have no fidelity rewards to claim!");
        }
    }
}
