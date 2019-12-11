package com.scorchedcode.darklust.GreekArmada;

import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;

public class FlagClaimListener implements Listener {

    public static HashMap<ArmadaPlayer, Group<Location, Location>> armadaPlayers = new HashMap<>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClaimCornerInteract(PlayerInteractEvent e) {
        ArmadaPlayer p = null;
        if(e.getAction() == Action.LEFT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL) {
            for (ArmadaPlayer armadaPlayer : armadaPlayers.keySet()) {
                if (e.getPlayer().getName().equals(armadaPlayer.getPlayer().getName())) {
                    armadaPlayers.get(armadaPlayer).setA(e.getClickedBlock().getLocation());
                    armadaPlayer.getPlayer().sendMessage(ChatColor.AQUA + "First corner selected!");
                    if(armadaPlayers.get(armadaPlayer).getA() != null && armadaPlayers.get(armadaPlayer).getB() != null) {
                        Location a = armadaPlayers.get(armadaPlayer).getA();
                        Location b = armadaPlayers.get(armadaPlayer).getB();
                        if(Math.abs(a.getX() - b.getX()) <= 100 && Math.abs(a.getZ() - b.getZ()) <= 100) {
                            for(Block z : DarkInit.blocksFromTwoPoints(a, b)) {
                                if(DarkInit.getPlugin().getRegisteredArmadas().getArmada(z.getLocation()) != null) {
                                    armadaPlayer.getPlayer().sendMessage(ChatColor.RED + "You cannot claim in another Armada's territory!");
                                    armadaPlayers.get(armadaPlayer).setA(null);
                                    armadaPlayers.get(armadaPlayer).setB(null);
                                    return;
                                }
                            }
                            armadaPlayer.getArmada().setClaim(a, b);
                            armadaPlayer.getPlayer().sendMessage("" + ChatColor.AQUA + ChatColor.BOLD + "Armada claim set!");
                            armadaPlayer.getPlayer().getInventory().remove(Material.END_CRYSTAL);
                            p = armadaPlayer;
                        }
                        else {
                            armadaPlayer.getPlayer().sendMessage(ChatColor.RED + "The region must be equal to or smaller than 100x100! Try again!");
                            armadaPlayers.get(armadaPlayer).setA(null);
                            armadaPlayers.get(armadaPlayer).setB(null);
                        }
                    }
                }
            }
            if(p != null)
                armadaPlayers.remove(p);
        }
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL) {
            for (ArmadaPlayer armadaPlayer : armadaPlayers.keySet()) {
                if (e.getPlayer().getName().equals(armadaPlayer.getPlayer().getName())) {
                    armadaPlayers.get(armadaPlayer).setB(e.getClickedBlock().getLocation());
                    armadaPlayer.getPlayer().sendMessage(ChatColor.AQUA + "Second corner selected!");
                    if(armadaPlayers.get(armadaPlayer).getA() != null && armadaPlayers.get(armadaPlayer).getB() != null) {
                        Location a = armadaPlayers.get(armadaPlayer).getA();
                        Location b = armadaPlayers.get(armadaPlayer).getB();
                        if(Math.abs(a.getX() - b.getX()) <= 100 && Math.abs(a.getZ() - b.getZ()) <= 100) {
                            for(Block z : DarkInit.blocksFromTwoPoints(a, b)) {
                                if(DarkInit.getPlugin().getRegisteredArmadas().getArmada(z.getLocation()) != null) {
                                    armadaPlayer.getPlayer().sendMessage(ChatColor.RED + "You cannot claim in another Armada's territory!");
                                    armadaPlayers.get(armadaPlayer).setA(null);
                                    armadaPlayers.get(armadaPlayer).setB(null);
                                    return;
                                }
                            }
                            armadaPlayer.getArmada().setClaim(a, b);
                            armadaPlayer.getPlayer().sendMessage("" + ChatColor.AQUA + ChatColor.BOLD + "Armada claim set!");
                            armadaPlayer.getPlayer().getInventory().remove(Material.END_CRYSTAL);
                            p = armadaPlayer;
                        }
                        else {
                            armadaPlayer.getPlayer().sendMessage(ChatColor.RED + "The region must be equal to or smaller than 100x100! Try again!");
                            armadaPlayers.get(armadaPlayer).setA(null);
                            armadaPlayers.get(armadaPlayer).setB(null);
                        }
                    }
                }
            }
            if(p != null)
                armadaPlayers.remove(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayersMoveThrough(PlayerMoveEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        if(ap != null) {
            Armada arm = DarkInit.getPlugin().getRegisteredArmadas().getArmada(e.getTo());
            Armada arma = DarkInit.getPlugin().getRegisteredArmadas().getArmada(e.getFrom());
            if(!ap.isInterritory()) {
                if (arm != null) {
                    e.getPlayer().sendTitle( arm.getName(), ChatColor.DARK_PURPLE + "Tread with reverence!", 10, 70, 20);
                    ap.setInTerritory(true);
                }
            }
            else {
                if(arm == null && arma != null) {
                    ap.setInTerritory(false);
                    e.getPlayer().sendTitle(arma.getName(), ChatColor.DARK_PURPLE + "Go with honor!", 10, 70, 20);
                }
            }
        }
    }
}
