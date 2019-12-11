package com.scorchedcode.darklust.GreekArmada;

import org.anhcraft.spaciouslib.events.ArmorEquipEvent;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.protocol.ActionBar;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GodListeners implements Listener {

    //Zeus
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && (((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD ||
                ((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.IRON_SWORD ||
                ((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.STONE_SWORD ||
                ((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.WOOD_SWORD ||
                ((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.GOLD_SWORD) &&
                DarkInit.getPlugin().getRegisteredArmadas().getPlayer(((Player) e.getDamager()).getName()) != null &&
                DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getDamager().getName()).hasFaction() &&
                (DarkInit.getPlugin().getRegisteredArmadas().getPlayer(((Player) e.getDamager()).getName()).getArmada().getGod() == God.ZEUS ||
                        DarkInit.getPlugin().getRegisteredArmadas().getPlayer(((Player) e.getDamager()).getName()).getGod() == God.ZEUS)) {

            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getDamager().getName());
            int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
            e.setDamage(e.getDamage() + mod*(e.getDamage() - (e.getDamage() * .5)));
            ActionBar.create(ChatColor.YELLOW + "The power of Zeus grants you more damage!").sendPlayer((Player) e.getDamager());
        }
    }

    //Athena
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAthenaDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName()).getGod() != null &&
                (DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName()).getGod() == God.ATHENA ||
                        DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName()).getArmada().getGod() == God.ATHENA)) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName());
            int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
            e.setDamage(e.getDamage() - mod*(e.getDamage() - (e.getDamage() * .5)));
            ActionBar.create(ChatColor.AQUA + "The power of Athena shields some damage!").sendPlayer((Player) e.getEntity());
        }
    }

    //Apollo
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onApolloBless(PlayerMoveEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        if (ap != null && ap.hasFaction()) {
            if ((ap.getGod() == God.APOLLO || ap.getArmada().getGod() == God.APOLLO)) {
                if (e.getPlayer().getInventory().getBoots() != null && e.getPlayer().getInventory().getBoots().getType() == Material.DIAMOND_BOOTS) {
                    if (!ap.isApolloHasted()) {
                        ap.setApolloHasted(true);
                        int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                        ActionBar.create(ChatColor.RED + "The power of Apollo hastens your speed!").sendPlayer(e.getPlayer());
                        e.getPlayer().setWalkSpeed(e.getPlayer().getWalkSpeed() + mod*(float) (e.getPlayer().getWalkSpeed() * 0.5));
                    }
                    e.getPlayer().spawnParticle(Particle.FLAME, e.getPlayer().getLocation().subtract(0, 1, 0), 5);
                }
            }
            if(ap.isHermesHasted())
                e.getPlayer().spawnParticle(Particle.FLAME, e.getPlayer().getLocation().subtract(0, 1, 0), 5); //Don't mind me...

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onApolloDequip(ArmorEquipEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        if (ap != null && ap.hasFaction()) {
            if ((ap.getGod() == God.APOLLO || ap.getArmada().getGod() == God.APOLLO)) {
                if (e.getSlot() == EquipSlot.FEET && e.getOldArmor() != null && e.getOldArmor().getType() == Material.DIAMOND_BOOTS && ap.isApolloHasted()) {
                    ap.setApolloHasted(false);
                    e.getPlayer().setWalkSpeed(0.2f);
                }
            }
        }
    }

    //Hestia
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHestiaLuck(BlockBreakEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        if (ap != null && ap.hasFaction()) {
            if ((ap.getGod() == God.HESTIA || ap.getArmada().getGod() == God.HESTIA)) {
                if (e.getBlock().getType().toString().contains("ORE")) {
                    ArrayList<ItemStack> newDrops = (ArrayList)e.getBlock().getDrops();
                    ArrayList<ItemStack> allDrops = (ArrayList)newDrops.clone();
                    int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                    for(int x = 0; x < mod; x++)
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), allDrops.get(0));

                    ActionBar.create(ChatColor.GOLD + "The power of Hestia grants you mining luck!").sendPlayer(e.getPlayer());
                }
            }
        }
    }

    //Hades
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHadesKill(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player) && e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getKiller().getName());
            if(ap != null && ap.hasFaction()) {
                if ((ap.getGod() == God.HADES || ap.getArmada().getGod() == God.HADES)) {
                    if (e.getDrops().size() > 0) {
                        int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                        ArrayList<ItemStack> newDrops = (ArrayList) ((ArrayList) e.getDrops()).clone();
                        for(int x = 0; x < mod; x++)
                            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), newDrops.get(0));
                        //e.getDrops().addAll(e.getDrops());
                        ActionBar.create(ChatColor.DARK_PURPLE + "The power of Hades grants you extra loot!").sendPlayer(e.getEntity().getKiller());
                    }
                }
            }
        }

    }

    //Demeter
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDemeterNom(FoodLevelChangeEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName());
        if (e.getEntity() instanceof Player && ap != null && ap.hasFaction()) {
            if ((ap.getGod() == God.DEMETER || ap.getArmada().getGod() == God.DEMETER) && e.getFoodLevel() > ((Player) e.getEntity()).getFoodLevel()) {
                int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                e.setFoodLevel((int) (e.getFoodLevel() + mod*(e.getFoodLevel() - e.getFoodLevel() * .5)));
                ActionBar.create(ChatColor.GREEN + "The power of Demeter grants you extra regen!").sendPlayer((Player) e.getEntity());

            }
        }

    }

    //Artemis
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArtemisShoot(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(((Player)((Arrow)e.getDamager()).getShooter()).getName());
            if (ap != null && ap.hasFaction()) {
                if ((ap.getGod() == God.ARTEMIS || ap.getArmada().getGod() == God.ARTEMIS)) {
                    if (ap.getPlayer().getInventory().getItemInMainHand().getType() == Material.BOW) {
                        int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                        e.setDamage(e.getDamage() + mod*(e.getDamage() - (e.getDamage() * .5)));
                        ActionBar.create(ChatColor.BLUE + "The power of Artemis grants you extra damage!").sendPlayer(ap.getPlayer());
                    }
                }
            }
        }
    }

    //Hephaestus
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHeppyCraft(CraftItemEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getViewers().get(0).getName());
        if(ap != null && ap.hasFaction()) {
            if ((ap.getGod() == God.HEPHAESTUS || ap.getArmada().getGod() == God.HEPHAESTUS)) {
                Random rando = new Random();
                int rnd = rando.nextInt(100);
                if(rnd <= 5) {
                    int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                    ItemStack modResult = e.getRecipe().getResult();
                    modResult.setAmount(modResult.getAmount()+mod*1);
                    e.setCurrentItem(modResult);//;getCursor().setAmount(e.getRecipe().getResult().getAmount() + 1);
                    ap.getPlayer().playSound(ap.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                    ActionBar.create(ChatColor.DARK_RED + "The power of Hephaestus grants you extra items!").sendPlayer(ap.getPlayer());
                }
            }
        }
    }

    //Hermes
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHermesRide(EntityMountEvent e) {
        if (e.getEntity() instanceof Player && e.getMount() instanceof Horse) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName());
            if (ap != null && ap.hasFaction()) {
                if ((ap.getGod() == God.HERMES || ap.getArmada().getGod() == God.HERMES)) {
                    if (!ap.isHermesHasted()) {
                        ap.setHermesHasted(true);
                        int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                        ActionBar.create(ChatColor.DARK_AQUA + "The power of Hermes hastens your speed!").sendPlayer(((Player) e.getEntity()));
                        ((Player) e.getEntity()).setWalkSpeed(((Player) e.getEntity()).getWalkSpeed() + mod*(float) (((Player) e.getEntity()).getWalkSpeed() * 0.5));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHermesStop(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player && e.getDismounted() instanceof Horse) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName());
            if (ap != null && ap.hasFaction()) {
                    if (ap.isHermesHasted()) {
                        ap.setHermesHasted(false);
                        ((Player) e.getEntity()).setWalkSpeed(0.2f);
                    }
            }
        }
    }

    //Aphrodite
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAphroditeRegen(EntityRegainHealthEvent e) {
        if(e.getEntity() instanceof Player && e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName());
            if (ap != null && ap.hasFaction()) {
                if ((ap.getGod() == God.APHRODITE || ap.getArmada().getGod() == God.APHRODITE)) {
                    int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                    e.setAmount(e.getAmount() + mod*e.getAmount());
                    ActionBar.create(ChatColor.DARK_BLUE + "The power of Aphrodite heals you faster!").sendPlayer(((Player) e.getEntity()));
                }
            }

        }
    }

    //Poseidon
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPoseidonSwim(PlayerMoveEvent e) {
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
        if (ap != null && ap.hasFaction()) {
            if ((ap.getGod() == God.POSEIDON || ap.getArmada().getGod() == God.POSEIDON)) {
                if (e.getPlayer().getLocation().add(0, 2, 0).getBlock().getType() == Material.STATIONARY_WATER) {
                    if (!ap.isPoseidonHasted()) {
                        ap.setPoseidonHasted(true);
                        int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                        ActionBar.create(ChatColor.BLUE + "The power of Poseidon helps you swim!").sendPlayer(e.getPlayer());
                        e.getPlayer().setWalkSpeed(e.getPlayer().getWalkSpeed() + mod*(float) (e.getPlayer().getWalkSpeed() * 0.5));
                    }
                    e.getPlayer().spawnParticle(Particle.WATER_BUBBLE, e.getPlayer().getLocation().subtract(0, 1, 0), 5);
                }
                else {
                    if(ap.isPoseidonHasted()) {
                        ap.setPoseidonHasted(false);
                        e.getPlayer().setWalkSpeed(0.2f);
                    }
                }
            }
        }
    }

    //Ares
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAresAttack(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getDamager().getName());
            if (ap != null && ap.hasFaction()) {
                if ((ap.getGod() == God.ARES || ap.getArmada().getGod() == God.ARES)) {
                    int mod = (ap.getGod() != null && ap.hasFaction() && ap.getArmada().getGod() != null && ap.getGod() == ap.getArmada().getGod()) ? 2 : 1;
                    e.setDamage(e.getDamage() + mod*(e.getDamage() - (e.getDamage() * .5)));
                    ActionBar.create(ChatColor.DARK_GREEN + "The power of Ares grants you more damage!").sendPlayer((Player) e.getDamager());
                }
            }
        }
    }
}
