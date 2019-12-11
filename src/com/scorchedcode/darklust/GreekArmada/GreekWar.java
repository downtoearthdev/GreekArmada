package com.scorchedcode.darklust.GreekArmada;

import org.anhcraft.spaciouslib.entity.bossbar.BossBar;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.VaultUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class GreekWar implements Listener {

    public static Armada firstContestant;
    public static Armada secondContestant;
    public static BossBar timeLeft;
    public static  Group<Integer, Integer> kills = new Group<>(0, 0);
    public static BigDecimal time = new BigDecimal(1.0).setScale(2, BigDecimal.ROUND_HALF_UP);
    public static boolean instance = false;
    public static List<Block> arena = DarkInit.blocksFromTwoPoints(new Location(Bukkit.getWorld("world"), 300, 90, 30), new Location(Bukkit.getWorld("world"), 100, 83, 250));
    public static List<Block> damageZone = DarkInit.blocksFromTwoPoints(new Location(Bukkit.getWorld("world"), 350, 200, 10), new Location(Bukkit.getWorld("world"), 118, 83, 270));

    public GreekWar() {

    }

    public GreekWar(Armada firstContestant, Armada secondContestant, BigDecimal timess) {
        if(instance)
            return;
        instance = !instance;
        this.firstContestant = firstContestant;
        this.secondContestant = secondContestant;
        time = (timess.floatValue() ==  0.0) ? GreekWar.time : timess;
        GreekWar.timeLeft = new BossBar(firstContestant.getName() + ChatColor.YELLOW+ " vs. " + secondContestant.getName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS, time.floatValue());
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(firstContestant.getPlayer(p.getName()) != null || secondContestant.getPlayer(p.getName()) != null) {
                teleportWithinArena(p);
                GreekWar.timeLeft.addViewer(p.getPlayer().getUniqueId());
            }
        }
        //Bukkit.broadcastMessage("" +ChatColor.AQUA + ChatColor.BOLD + "A war between " + firstContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + " and " + secondContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + " has begun!");
        for(Player p : Bukkit.getOnlinePlayers())
            p.sendMessage("" +ChatColor.AQUA + ChatColor.BOLD + Util.getLocaleString("armada-war-begun-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName()))+" " + firstContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + " "+ Util.getLocaleString("armada-war-begun-msgtwo", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())) + " " + secondContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + " " + Util.getLocaleString("armada-war-begun-msgthree", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())));
        new TimeRunner().runTaskTimerAsynchronously(DarkInit.getPlugin(), 20L, 1200L);
    }

    public class TimeRunner extends BukkitRunnable {

        @Override
        public void run() {
            time = (time.floatValue() - 0.02 >= 0.0) ? new BigDecimal(time.floatValue() - 0.02).setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0.0);
            //Bukkit.broadcastMessage(kills.getA() + "/" + kills.getB());
            try {
                timeLeft.setHealth(time.floatValue());
                //timeLeft.
            }
            catch(ConcurrentModificationException e) {

            }
            if(time.floatValue() == 0.0) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(firstContestant.getPlayer(p.getName()) != null || secondContestant.getPlayer(p.getName()) != null)
                        timeLeft.removeViewer(p.getPlayer().getUniqueId());
                }
                Armada winningArmada = (kills.getA() > kills.getB()) ? firstContestant : secondContestant;
                Armada losingArmada = (kills.getA() < kills.getB()) ? firstContestant : secondContestant;
                if(winningArmada.equals(losingArmada)) {
                    //Bukkit.broadcastMessage(firstContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + " has tied in glorious combat with " + secondContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + "! They honor their gods today!");
                    for(Player p : Bukkit.getOnlinePlayers())
                        p.sendMessage(firstContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + " " + Util.getLocaleString("armada-war-tie-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())) + " " + secondContestant.getName() + ChatColor.AQUA + ChatColor.BOLD + "! " +  Util.getLocaleString("armada-war-end-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())));
                }
                else {
                    //Bukkit.broadcastMessage(winningArmada.getName() + ChatColor.AQUA + ChatColor.BOLD + " has won in glorious combat with " + losingArmada.getName() + ChatColor.AQUA + ChatColor.BOLD + "! They honor their gods today!");
                    for(Player p : Bukkit.getOnlinePlayers())
                        p.sendMessage(winningArmada.getName() + ChatColor.AQUA + ChatColor.BOLD + " " + Util.getLocaleString("armada-war-won-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())) + " " + losingArmada.getName() + ChatColor.AQUA + ChatColor.BOLD + "! " +  Util.getLocaleString("armada-war-end-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())));

                    winningArmada.setWarsWon(winningArmada.getWarsWon()+1);
                }
                instance = !instance;
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(winningArmada.getPlayer(p.getName()) != null && !winningArmada.equals(losingArmada)) {
                        p.teleport((winningArmada.getHome() != null) ? winningArmada.getHome() : p.getWorld().getSpawnLocation());
                        p.sendTitle(ChatColor.RED + Util.getLocaleString("armada-war-title-won-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())), ChatColor.GOLD + Util.getLocaleString("armada-war-title-won-msgtwo", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())), 10, 100, 20);
                        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 1.0f);
                        VaultUtils.deposit(p, 2500.00);
                        p.getInventory().addItem(new ItemStack(Material.IRON_SWORD), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_CHESTPLATE),
                                new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLDEN_APPLE, 3));
                    }
                    if(losingArmada.getPlayer(p.getName()) != null && !winningArmada.equals(losingArmada)) {
                        p.teleport((losingArmada.getHome() != null) ? losingArmada.getHome() : p.getWorld().getSpawnLocation());
                        p.sendTitle(ChatColor.GREEN + Util.getLocaleString("armada-war-title-lost-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())), ChatColor.GOLD + Util.getLocaleString("armada-war-title-lost-msgtwo", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())), 10, 100, 20);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_FLUTE, 1.0f, 1.0f);
                        VaultUtils.deposit(p, 950.00);
                        p.getInventory().addItem(new ItemStack(Material.STONE_SWORD), new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_CHESTPLATE),
                                new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.APPLE, 3));
                    }
                    if(winningArmada.equals(losingArmada) && (firstContestant.getPlayer(p.getName()) != null || secondContestant.getPlayer(p.getName()) != null)) {
                        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName());
                        p.teleport((ap.getArmada().getHome() != null) ? ap.getArmada().getHome() : p.getWorld().getSpawnLocation());
                        p.sendTitle(ChatColor.GRAY + Util.getLocaleString("armada-war-title-draw-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())), ChatColor.GOLD + Util.getLocaleString("armada-war-title-lost-msgtwo", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())), 10, 100, 20);
                        VaultUtils.deposit(p, 950.00);
                        p.getInventory().addItem(new ItemStack(Material.STONE_SWORD), new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_CHESTPLATE),
                                new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.APPLE, 3));

                    }
                }
                timeLeft.remove();
                firstContestant = null;
                secondContestant = null;
                time = new BigDecimal(1.0).setScale(2, BigDecimal.ROUND_HALF_UP);
                timeLeft = null;
                DarkInit.getPlugin().getRegisteredArmadas().war = null;
                cancel();
            }

        }
    }

    private void teleportWithinArena(Player p) {
        boolean notsettled = true;
        Random randomSeed = new Random();
        while(notsettled) {
            Block randomBlock = arena.get(randomSeed.nextInt(arena.size()-1));
            if(randomBlock.getType() == Material.AIR && randomBlock.getRelative(BlockFace.UP).getType() == Material.AIR && randomBlock.getRelative(BlockFace.DOWN).getType().isSolid() &&
            randomBlock.getRelative(BlockFace.NORTH).getType() == Material.AIR && randomBlock.getRelative(BlockFace.WEST).getType() == Material.AIR && randomBlock.getRelative(BlockFace.EAST).getType() == Material.AIR && randomBlock.getRelative(BlockFace.SOUTH).getType() == Material.AIR &&
            randomBlock.getX() != arena.get(0).getX() && randomBlock.getZ() != arena.get(arena.size()-1).getZ()) {
                ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName());
                notsettled = !notsettled;
                p.teleport(randomBlock.getLocation());
                p.sendTitle(ChatColor.DARK_AQUA + Util.getLocaleString("armada-war-title-join-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName())), ChatColor.GOLD + Util.getLocaleString("armada-war-title-join-msgtwo", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName()))+" " + ChatColor.BOLD + Util.toProperUppercaseString(ap.getArmada().getGod().toString()) + "!", 10, 70, 20);
                timeLeft.removeViewer(p.getUniqueId());
                timeLeft.addViewer(p.getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(GreekWar.firstContestant != null && GreekWar.secondContestant != null && instance) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
            if (GreekWar.firstContestant.getPlayer(e.getPlayer().getName()) != null || GreekWar.secondContestant.getPlayer(e.getPlayer().getName()) != null) {
                teleportWithinArena(e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if(GreekWar.firstContestant != null && GreekWar.secondContestant != null && instance) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
            if (GreekWar.firstContestant.getPlayer(e.getPlayer().getName()) != null || GreekWar.secondContestant.getPlayer(e.getPlayer().getName()) != null) {
                boolean notsettled = true;
                Random randomSeed = new Random();
                Block randomo = null;
                while(notsettled) {
                    Block randomBlock = arena.get(randomSeed.nextInt(arena.size()-1));
                    if(randomBlock.getType() == Material.AIR && randomBlock.getRelative(BlockFace.UP).getType() == Material.AIR && randomBlock.getRelative(BlockFace.DOWN).getType().isSolid()) {
                        notsettled = !notsettled;
                        randomo = randomBlock;
                        e.getPlayer().sendTitle(ChatColor.DARK_AQUA + Util.getLocaleString("armada-war-title-join-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName())), ChatColor.GOLD + Util.getLocaleString("armada-war-title-join-msgtwo", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName()))+" " + ChatColor.BOLD + Util.toProperUppercaseString(ap.getArmada().getGod().toString()) + "!", 10, 70, 20);
                        /*timeLeft.removeViewer(e.getPlayer().getUniqueId());
                        timeLeft.addViewer(e.getPlayer().getUniqueId());*/
                    }
                }
                e.setRespawnLocation(randomo.getLocation());

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(GreekWar.firstContestant != null && GreekWar.secondContestant != null && instance) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
            if (GreekWar.firstContestant.getPlayer(e.getPlayer().getName()) != null || GreekWar.secondContestant.getPlayer(e.getPlayer().getName()) != null) {
                for (Block b : GreekWar.arena) {
                    if (b.getLocation().getBlock() == e.getTo().getBlock())
                        return;
                }
                boolean notsettled = true;
                Random randomSeed = new Random();
                Block randomo = null;
                while(notsettled) {
                    Block randomBlock = arena.get(randomSeed.nextInt(arena.size()-1));
                    if(randomBlock.getType() == Material.AIR && randomBlock.getRelative(BlockFace.UP).getType() == Material.AIR && randomBlock.getRelative(BlockFace.DOWN).getType().isSolid()) {
                        notsettled = !notsettled;
                        randomo = randomBlock;
                        e.getPlayer().sendTitle(ChatColor.DARK_AQUA + Util.getLocaleString("armada-war-title-join-msg", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName())), ChatColor.GOLD + Util.getLocaleString("armada-war-title-join-msgtwo", DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName()))+" " + ChatColor.BOLD + Util.toProperUppercaseString(ap.getArmada().getGod().toString()) + "!", 10, 70, 20);
                        /*timeLeft.removeViewer(e.getPlayer().getUniqueId());
                        timeLeft.addViewer(e.getPlayer().getUniqueId());*/
                    }
                }
                e.setTo(randomo.getLocation());
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTryToLeave(PlayerMoveEvent e) {
        if (GreekWar.firstContestant != null && GreekWar.secondContestant != null && instance) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
            if (GreekWar.firstContestant.getPlayer(e.getPlayer().getName()) != null || GreekWar.secondContestant.getPlayer(e.getPlayer().getName()) != null) {
                if(!damageZone.contains(e.getTo().getBlock())) {
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_CAT_HISS, 1.0f, 1.0f);
                    e.getPlayer().playEffect(EntityEffect.WITCH_MAGIC);
                    e.getPlayer().sendTitle(ChatColor.DARK_RED + Util.getLocaleString("armada-war-title-deserter-msg", ap), ChatColor.DARK_RED + Util.getLocaleString("armada-war-title-deserter-msgwtwo", ap), 10, 70, 20);
                    e.getPlayer().setHealth(0.0);
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (GreekWar.firstContestant != null && GreekWar.secondContestant != null && instance) {
            ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getPlayer().getName());
            if (GreekWar.firstContestant.getPlayer(e.getPlayer().getName()) != null || GreekWar.secondContestant.getPlayer(e.getPlayer().getName()) != null) {
                GreekWar.timeLeft.removeViewer(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKill(EntityDeathEvent e) {
        if(GreekWar.firstContestant != null && GreekWar.secondContestant != null && instance) {
            if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
                ArmadaPlayer killed = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getName());
                ArmadaPlayer killer = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(e.getEntity().getKiller().getName());
                if ((GreekWar.firstContestant.getPlayer(killed.getPlayer().getName()) != null || GreekWar.secondContestant.getPlayer(killed.getPlayer().getName()) != null) &&
                        (GreekWar.firstContestant.getPlayer(killer.getPlayer().getName()) != null || GreekWar.secondContestant.getPlayer(killer.getPlayer().getName()) != null)) {
                    if (GreekWar.firstContestant.getPlayer(killed.getPlayer().getName()) != null)
                        GreekWar.kills.setB(GreekWar.kills.getB() + 1);
                    if (GreekWar.secondContestant.getPlayer(killed.getPlayer().getName()) != null)
                        GreekWar.kills.setA(GreekWar.kills.getA() + 1);
                }
            }
        }
    }
}
