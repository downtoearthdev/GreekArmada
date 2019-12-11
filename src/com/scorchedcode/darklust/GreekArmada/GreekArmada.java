package com.scorchedcode.darklust.GreekArmada;

import com.coloredcarrot.jsonapi.impl.JsonClickEvent;
import com.coloredcarrot.jsonapi.impl.JsonHoverEvent;
import com.coloredcarrot.jsonapi.impl.JsonMsg;
import com.scorchedcode.darklust.GreekArmada.cmd.*;
import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.entity.Hologram;
import org.anhcraft.spaciouslib.utils.Group;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.joda.time.Days;
import org.joda.time.LocalTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GreekArmada extends JavaPlugin {

    private Hologram[] spawnHologram = new Hologram[4];
    private ArrayList<Hologram> customHolograms = new ArrayList<>();
    private Armadas registeredArmadas;
    protected static ArrayList<Group<ArmadaPlayer.Language, ArrayList<Group<String, String>>>> langStrings = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public void onDisable() {
        DarkInit.getLog().info("[GreekArmada] Saving Data...");
        saveHolograms();
        saveCustomHolograms();
        registeredArmadas.save();
    }

    @Override
    public void onEnable() {
        new DarkInit(this);
        try {
            registerCommands().buildExecutor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadLocaleStrings();
        registeredArmadas = new Armadas();
        for(Armada arm : registeredArmadas.listArmadas())
            arm.loadFinalStep();
        getCommand("spawnhologram").setExecutor(new SpawnHologram());
        getCommand("greekfloating").setExecutor(new FloatingText());
        DarkInit.getPM().registerEvents(new GodListeners(), this);
        DarkInit.getPM().registerEvents(new SpawnHologram(), this);
        DarkInit.getPM().registerEvents(new FloatingText(), this);
        DarkInit.getPM().registerEvents(new TemporaryInviteListener(), this);
        DarkInit.getPM().registerEvents(new FlagClaimListener(), this);
        DarkInit.getPM().registerEvents(new PVPListener(), this);
        DarkInit.getPM().registerEvents(new NewPlayerListener(), this);
        DarkInit.getPM().registerEvents(new AtheniaReportCommand(), this);
        DarkInit.getPM().registerEvents(new GreekWar(), this);
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
            new File(getDataFolder() + "/players/").mkdir();
            new File(getDataFolder() + "/factions/").mkdir();
        }
        if(new File(getDataFolder() + "/holograms.yml").exists())
            loadHolograms();
        if(new File(getDataFolder() + "/customholograms.yml").exists())
            loadCustomHolograms();
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            //Registering placeholder will be use here
            new ArmadaPlaceholders().register();
        }
        new ArmadaDissolveRunner().runTaskTimerAsynchronously(DarkInit.getPlugin(), 20L, 1200L);
        new ModMailRunner().runTaskTimerAsynchronously(this, 20L, 6000L);
        new TopHologramRunner().runTaskTimerAsynchronously(this, 20L, 100L);
        new SaveRunner().runTaskTimerAsynchronously(this, 1000L, 1000L);
    }

    private CommandBuilder registerCommands() throws Exception {
        return new CommandBuilder("f", new RootCommand())
                .addSubCommand(new SubCommandBuilder("new", "Creates a new Armada or renames an existing one.", new RootNewCommand())
                .addArgument(new CommandArgument("armada", new RootNewCommand(), false), CommandArgument.Type.CUSTOM))
                .addSubCommand(new SubCommandBuilder("greekinfo", "Displays info on an Armada", new RootGreekInfoCommand()))
                .addSubCommand(new SubCommandBuilder("sethome", "Sets the faction home", new RootHomeSetCommand()))
                .addSubCommand(new SubCommandBuilder("delhome", "Deletes the faction home", new RootHomeSetCommand()))
                .addSubCommand(new SubCommandBuilder("home", "Teleport to faction home", new RootHomeSetCommand()))
                .addSubCommand(new SubCommandBuilder("athenajustice", "Bans player from the Armada", new AthenaJusticeCommand())
                .addArgument(new CommandArgument("ban", new AthenaJusticeCommand(), false), CommandArgument.Type.ONLINE_PLAYER))
                .addSubCommand(new SubCommandBuilder("glory", "Promotes player within an Armada", new RootChangeRankCommand())
                .addArgument(new CommandArgument("rankup", new RootChangeRankCommand(), false), CommandArgument.Type.ONLINE_PLAYER))
                .addSubCommand(new SubCommandBuilder("demote", "Demotes a player within an Armada", new RootChangeRankCommand())
                        .addArgument(new CommandArgument("rankdown", new RootChangeRankCommand(), false), CommandArgument.Type.ONLINE_PLAYER))
                .addSubCommand(new SubCommandBuilder("timetogo", "Leaves player Armada", new RootJoinLeaveCommand()))
                .addSubCommand(new SubCommandBuilder("writeyourstory", "Write the story for your Armada!", new RootStoryCommand()))
                .addSubCommand(new SubCommandBuilder("gods", "Select the god of your Armada", new RootGodsCommand())
                .addArgument(new CommandArgument("god", new RootGodsCommand(), false), CommandArgument.Type.CUSTOM))
                .addSubCommand(new SubCommandBuilder("atheniafactions", "List all Armadas", new RootArmadaList()))
                .addSubCommand(new SubCommandBuilder("i", "Invites player to this Armada", new RootInviteCommand())
                .addArgument(new CommandArgument("invite", new RootInviteCommand(), false), CommandArgument.Type.ONLINE_PLAYER))
                .addSubCommand(new SubCommandBuilder("greekrecruit", "Opens armada to everyone", new RootInviteCommand()))
                .addSubCommand(new SubCommandBuilder("joingreek", "Join an open Armada", new RootJoinLeaveCommand())
                .addArgument(new CommandArgument("join", new RootJoinLeaveCommand(), false), CommandArgument.Type.CUSTOM))
                .addSubCommand(new SubCommandBuilder("greekdeny", "Sets Armada to invitation only", new RootInviteCommand()))
                .addSubCommand(new SubCommandBuilder("gloryflag", "Claims land for your Armada", new RootGloryflagCommand()))
                .addSubCommand(new SubCommandBuilder("delflag", "Removes claim from your Armada", new RootGloryflagCommand()))
                .addSubCommand(new SubCommandBuilder("atheniareport", "Report a faction for breaking the rules", new AtheniaReportCommand())
                .addArgument(new CommandArgument("reportfaction", new AtheniaReportCommand(), false), CommandArgument.Type.CUSTOM))
                .addSubCommand(new SubCommandBuilder("atheniareportp", "Report a player for breaking the rules", new AtheniaReportCommand())
                .addArgument(new CommandArgument("reportplayer", new AtheniaReportCommand(), false), CommandArgument.Type.ONLINE_PLAYER)
                .addArgument(new CommandArgument("reportreport", new AtheniaReportCommand(), false), CommandArgument.Type.CUSTOM))
                .addSubCommand(new SubCommandBuilder("flaginfo", "See the land boundaries of your faction", new RootFlagInfoCommand()))
                .addSubCommand(new SubCommandBuilder("greekrules", "Sets or deletes rules for the Armada", new RootArmadaRulesCommand())
                .addArgument(new CommandArgument("rulenumber", new RootArmadaRulesCommand(), false), CommandArgument.Type.INTEGER)
                .addArgument(new CommandArgument("ruletext", new RootArmadaRulesCommand(), false), CommandArgument.Type.CUSTOM))
                .addSubCommand(new SubCommandBuilder("greekshowrules", "Shows all the rules for this Armada", new RootShowRulesCommand()))
                .addSubCommand(new SubCommandBuilder("greekhp", "See the remaining HP for the Armada", new RootHPCommand()))
                .addSubCommand(new SubCommandBuilder("discord", "Gets the Discord link", new RootServerInfoCommand()))
                .addSubCommand(new SubCommandBuilder("greeksite", "Gets the website link", new RootServerInfoCommand()))
                .addSubCommand(new SubCommandBuilder("athenialevel", "Random reward for gaining Fidelity!", new RootFidelityRewardCommand()))
                .addSubCommand(new SubCommandBuilder("greekpersonal", "Change your personal God", new RootGodsCommand())
                .addArgument(new CommandArgument("personalgold", new RootGodsCommand(), false), CommandArgument.Type.CUSTOM))
                .addSubCommand(new SubCommandBuilder("greekfrench", "Change language to French", new RootLanguageCommand()))
                .addSubCommand(new SubCommandBuilder("greekenglish", "Change language to English", new RootLanguageCommand()))
                .addSubCommand(new SubCommandBuilder("greekwar", "Declares war against another Armada", new RootWarCommand())
                .addArgument(new CommandArgument("warfaction", new RootWarCommand(), false), CommandArgument.Type.CUSTOM)
                .addArgument(new CommandArgument("debugtime", new RootWarCommand(), true), CommandArgument.Type.CUSTOM));


    }

    public Hologram getSpawnHologram(int index) {
        return spawnHologram[index];
    }

    public Hologram[] getSpawnHolograms() {
        return spawnHologram;
    }

    public void setSpawnHologram(Hologram spawnHologram, int index) {
        this.spawnHologram[index] = spawnHologram;
    }

    public ArrayList<Hologram> getCustomHolograms() {
        return customHolograms;
    }

    private void loadLocaleStrings() {
        YamlConfiguration eng = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/language/en.yml"));
        YamlConfiguration fre = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/language/fr.yml"));
        ArrayList<Group<String, String>> engGroup = new ArrayList<>();
        ArrayList<Group<String, String>> freGroup = new ArrayList<>();
        if(!new File(getDataFolder() + "/language/en.yml").exists()) {
            File languages = new File(getDataFolder() + "/language/");
            languages.mkdir();
            try {
                InputStream stream = this.getResource("language/en.yml");
                InputStream streamtwo = this.getResource("language/fr.yml");
                FileUtils.copyInputStreamToFile(stream, new File(languages,"en.yml"));
                FileUtils.copyInputStreamToFile(streamtwo, new File(languages,"fr.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        for(String stringSection : eng.getKeys(false)) {
            engGroup.add(new Group(stringSection, eng.getString(stringSection)));
        }
        for(String stringSection : fre.getKeys(false)) {
            freGroup.add(new Group(stringSection, fre.getString(stringSection)));
        }
        langStrings.add(new Group(ArmadaPlayer.Language.ENGLISH, engGroup));
        langStrings.add(new Group(ArmadaPlayer.Language.FRENCH, freGroup));
    }

    private void loadCustomHolograms() {
        YamlConfiguration loadHolograms = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/customholograms.yml"));
        for(String stringSection : loadHolograms.getKeys(false)) {
            ConfigurationSection section = loadHolograms.getConfigurationSection(stringSection);
            Location holoLocation = new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"));
            List<String> description = section.getStringList("description");
            Hologram newHolo = new Hologram(holoLocation);
            newHolo.addLines(description.toArray(new String[description.size()]));
            newHolo.buildPackets();
            for(Player p : Bukkit.getOnlinePlayers())
                newHolo.addViewer(p.getUniqueId());
            customHolograms.add(newHolo);
        }

    }
    private void loadHolograms() {
        YamlConfiguration loadHolograms = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/holograms.yml"));
        ConfigurationSection fkills = loadHolograms.getConfigurationSection("factionkills");
        ConfigurationSection pkills = loadHolograms.getConfigurationSection("playerkills");
        ConfigurationSection fwars = loadHolograms.getConfigurationSection("factionwars");
        ConfigurationSection plevels = loadHolograms.getConfigurationSection("playerlevels");
        Location fkillsLocation = null;
        Location pkillsLocation = null;
        Location fwarsLocation = null;
        Location plevelsLocation = null;
        if(fkills.getString("world") != null)
            fkillsLocation = new Location(Bukkit.getWorld(fkills.getString("world")), fkills.getDouble("x"), fkills.getDouble("y"), fkills.getDouble("z"));
        if(pkills.getString("world") != null)
            pkillsLocation = new Location(Bukkit.getWorld(pkills.getString("world")), pkills.getDouble("x"), pkills.getDouble("y"), pkills.getDouble("z"));
        if(fwars.getString("world") != null)
            fwarsLocation = new Location(Bukkit.getWorld(fwars.getString("world")), fwars.getDouble("x"), fwars.getDouble("y"), fwars.getDouble("z"));
        if(plevels.getString("world") != null)
            plevelsLocation = new Location(Bukkit.getWorld(plevels.getString("world")), plevels.getDouble("x"), plevels.getDouble("y"), plevels.getDouble("z"));
        if(fkillsLocation != null)
            Util.spawnHologram("factionkills", fkillsLocation);
        if(pkillsLocation != null)
            Util.spawnHologram("playerkills", pkillsLocation);
        if(fwarsLocation != null)
            Util.spawnHologram("factionwars", fwarsLocation);
        if(plevelsLocation != null)
            Util.spawnHologram("playerlevels", plevelsLocation);
    }

    private void saveCustomHolograms() {
        File save = new File(getDataFolder() + "/customholograms.yml");
        if(save.exists())
            save.delete();
        YamlConfiguration saveHolograms = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/customholograms.yml"));
        for(Hologram h : customHolograms) {
            ConfigurationSection section = saveHolograms.createSection(String.valueOf(h.getLines().get(0)));
            section.set("world", h.getLocation().getWorld().getName());
            section.set("x", h.getLocation().getX());
            section.set("y", h.getLocation().getY());
            section.set("z", h.getLocation().getZ());
            LinkedList<String> lines = h.getLines();
            Collections.reverse(lines);
            section.set("description", lines);
        }
        try {
            saveHolograms.save(new File(getDataFolder() + "/customholograms.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveHolograms() {
        YamlConfiguration saveHolograms = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/holograms.yml"));
        ConfigurationSection fkills = saveHolograms.createSection("factionkills");
        ConfigurationSection pkills = saveHolograms.createSection("playerkills");
        ConfigurationSection fwars = saveHolograms.createSection("factionwars");
        ConfigurationSection plevels = saveHolograms.createSection("playerlevels");
        if(spawnHologram[0] != null) {
            fkills.set("world", spawnHologram[0].getLocation().getWorld().getName());
            fkills.set("x", spawnHologram[0].getLocation().getX());
            fkills.set("y", spawnHologram[0].getLocation().getY());
            fkills.set("z", spawnHologram[0].getLocation().getZ());
        }
        if(spawnHologram[1] != null) {
            pkills.set("world", spawnHologram[1].getLocation().getWorld().getName());
            pkills.set("x", spawnHologram[1].getLocation().getX());
            pkills.set("y", spawnHologram[1].getLocation().getY());
            pkills.set("z", spawnHologram[1].getLocation().getZ());
        }
        if(spawnHologram[2] != null) {
            fwars.set("world", spawnHologram[2].getLocation().getWorld().getName());
            fwars.set("x", spawnHologram[2].getLocation().getX());
            fwars.set("y", spawnHologram[2].getLocation().getY());
            fwars.set("z", spawnHologram[2].getLocation().getZ());
        }
        if(spawnHologram[3] != null) {
            plevels.set("world", spawnHologram[3].getLocation().getWorld().getName());
            plevels.set("x", spawnHologram[3].getLocation().getX());
            plevels.set("y", spawnHologram[3].getLocation().getY());
            plevels.set("z", spawnHologram[3].getLocation().getZ());
        }
        try {
            saveHolograms.save(new File(getDataFolder() + "/holograms.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Armadas getRegisteredArmadas() {
        return registeredArmadas;
    }


    public class ArmadaDissolveRunner extends BukkitRunnable {

        @Override
        public void run() {
            for(Armada a : registeredArmadas.listArmadas()) {
                if(a.getLastlogin() != 0 && Days.daysBetween(LocalTime.fromMillisOfDay(a.getLastlogin()), LocalTime.now()).getDays() >=5) {
                    Bukkit.broadcastMessage("The Armada " + a.getName() + " has lost their claim due to inactivity!");
                    a.unsetClaim();
                    a.setLastlogin(0);
                }
            }
        }
    }

    public class ModMailRunner extends BukkitRunnable {

        @Override
        public void run() {
            File modDir = new File(getDataFolder() + "/modmail/");
            if(modDir.exists()) {
                if(modDir.listFiles().length != 0) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if(DarkInit.hasPerm(p, "greekarmada.modmail")) {
                            JsonMsg err = new JsonMsg("Player reports detected", ChatColor.RED).bold(true).hoverEvent(JsonHoverEvent.showText("Click to read")).clickEvent(JsonClickEvent.runCommand("/readthemail"));
                            err.send(p);
                            p.playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, 1.0f, 1.0f);
                        }
                    }
                }
            }
        }
    }

    public class TopHologramRunner extends BukkitRunnable {

        @Override
        public void run() {
            for(int x = 0; x < spawnHologram.length; x++) {
                ArrayList<String> newContents = new ArrayList<>();
                Location oldLoc;
                int z = 1;
                String title;
                switch(x) {
                    case 0:
                        if(spawnHologram[0] != null) {
                            oldLoc = spawnHologram[x].getLocation();
                            for (Armada a : registeredArmadas.calculateTopTenByFactionByKill()) {
                                newContents.add(" ");
                                newContents.add(z++ + ") " + a.getName() + " : " + a.getKills());
                            }
                            Collections.reverse(newContents);
                            newContents.add(spawnHologram[x].getLines().get(0).trim());
                            spawnHologram[x].remove();
                            spawnHologram[x] = new Hologram(oldLoc);
                            spawnHologram[x].addLines(newContents.toArray(new String[newContents.size()]));
                            spawnHologram[x].buildPackets();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                spawnHologram[x].addViewer(p.getUniqueId());
                            }
                        }
                        break;
                    case 1:
                        if(spawnHologram[x] != null) {
                            oldLoc = spawnHologram[x].getLocation();
                            for (ArmadaPlayer a : registeredArmadas.calculateTopTenByPlayersByKill()) {
                                newContents.add(" ");
                                newContents.add(z++ + ") " + a.getFancyName() + " : " + a.getKills());
                            }
                            Collections.reverse(newContents);
                            newContents.add(spawnHologram[x].getLines().get(0).trim());
                            spawnHologram[x].remove();
                            spawnHologram[x] = new Hologram(oldLoc);
                            spawnHologram[x].addLines(newContents.toArray(new String[newContents.size()]));
                            spawnHologram[x].buildPackets();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                spawnHologram[x].addViewer(p.getUniqueId());
                            }
                        }
                        break;
                    case 2:
                        if(spawnHologram[x] != null) {
                            oldLoc = spawnHologram[x].getLocation();
                            for (Armada a : registeredArmadas.calculateTopTenByFactionByWars()) {
                                newContents.add(" ");
                                newContents.add(z++ + ") " + a.getName() + " : " + a.getWarsWon());
                            }
                            Collections.reverse(newContents);
                            newContents.add(spawnHologram[x].getLines().get(0).trim());
                            spawnHologram[x].remove();
                            spawnHologram[x] = new Hologram(oldLoc);
                            spawnHologram[x].addLines(newContents.toArray(new String[newContents.size()]));
                            spawnHologram[x].buildPackets();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                spawnHologram[x].addViewer(p.getUniqueId());
                            }
                        }
                        break;
                    case 3:
                        if(spawnHologram[x] != null) {
                            oldLoc = spawnHologram[x].getLocation();
                            for (ArmadaPlayer a : registeredArmadas.calculateTopTenByPlayersByFidelity()) {
                                newContents.add(" ");
                                newContents.add(z++ + ") " + a.getFancyName() + " : " + a.getFidelity());
                            }
                            Collections.reverse(newContents);
                            newContents.add(spawnHologram[x].getLines().get(0).trim());
                            spawnHologram[x].remove();
                            spawnHologram[x] = new Hologram(oldLoc);
                            spawnHologram[x].addLines(newContents.toArray(new String[newContents.size()]));
                            spawnHologram[x].buildPackets();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                spawnHologram[x].addViewer(p.getUniqueId());
                            }
                        }
                        break;

                }
            }
        }
    }

    public class SaveRunner extends BukkitRunnable {

        @Override
        public void run() {
            DarkInit.getPlugin().getRegisteredArmadas().save();
        }
    }
}
