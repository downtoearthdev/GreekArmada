package com.scorchedcode.darklust.GreekArmada;

import org.anhcraft.spaciouslib.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Armadas {
    private CopyOnWriteArrayList<Armada> registeredArmadas = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<ArmadaPlayer> registeredPlayers = new CopyOnWriteArrayList<>();
    public GreekWar war;

    public Armadas() {
        //init
        //CHANGE THIS
        for(File armada : new File(DarkInit.getPlugin().getDataFolder() + "/armadas/").listFiles()) {
            YamlConfiguration loadData = YamlConfiguration.loadConfiguration(armada);
            String name = loadData.getString("name");
            String fancyname = loadData.getString("fancyname");
            String desc = loadData.getString("description");
            Location claimx = (loadData.getString("claimx") != null) ? LocationUtils.str2loc(loadData.getString("claimx")) : null;
            Location claimy = (loadData.getString("claimy") != null) ? LocationUtils.str2loc(loadData.getString("claimy")) : null;

            List<Block> claim = (claimx != null && claimy != null) ? DarkInit.blocksFromTwoPoints(claimx, claimy) : null;

            Location home = (loadData.getString("home") != null) ? LocationUtils.str2loc(loadData.getString("home")) : null;
            God god = (loadData.getString("god") != null) ? God.valueOf(loadData.getString("god")) : null;
            String[] rules = loadData.getStringList("rules").toArray(new String[10]);
            int power = loadData.getInt("power");
            boolean invite = loadData.getBoolean("invite");
            int kills = loadData.getInt("kills");
            int deaths = loadData.getInt("deaths");
            long lastlogin = loadData.getLong("lastlogin");
            int warswon = loadData.getInt("warswon");
            registeredArmadas.add(new Armada(name, fancyname, desc, claim, home, god, rules, power, invite, kills, deaths, lastlogin, warswon));
        }

        for(File player : new File(DarkInit.getPlugin().getDataFolder() + "/players/").listFiles()) {
            YamlConfiguration loadData = YamlConfiguration.loadConfiguration(player);
            String name = loadData.getString("name");
            String fancyName = loadData.getString("fancyname");
            Armada armada = getArmada(loadData.getString("armada"));
            int unclaimed = loadData.getInt("unclaimed");
            God god = (loadData.isSet("god")) ? God.valueOf(loadData.getString("god")) : null;
            float fidelity = ((Double)loadData.getDouble("fidelity")).floatValue();
            int playtime = loadData.getInt("playtime");
            int kills = loadData.getInt("kills");
            int deaths = loadData.getInt("deaths");
            int ownerhp = loadData.getInt("ownerhp");
            ArmadaPlayer.Language lang = ArmadaPlayer.Language.valueOf(loadData.getString("language", "ENGLISH"));
            ArmadaPlayer.Rank rank = ArmadaPlayer.Rank.valueOf(loadData.getString("rank"));
            registeredPlayers.add(new ArmadaPlayer(name, fancyName, unclaimed, armada, god, fidelity, playtime, kills, deaths, ownerhp, rank, lang));
        }
    }

    public void addArmada(Armada armada) {
        registeredArmadas.add(armada);
    }

    public Armada getArmada(String name) {
        for(Armada a : registeredArmadas) {
            if(a.getFriendlyName().equals(name))
                return a;
        }
        return null;
    }

    public Armada getArmada(Location location) {
        for(Armada a : registeredArmadas) {
            if(a.isWithinClaim(location))
                return a;
        }
        return null;
    }

    public List<ArmadaPlayer> getArmadaPlayers() {
        return registeredPlayers;
    }

    public ArmadaPlayer getPlayer(String name) {
        for(ArmadaPlayer a : registeredPlayers) {
            if(a.getName().equals(name))
                return a;
        }
        ArmadaPlayer ap = new ArmadaPlayer(Bukkit.getPlayer(name));
        if(ap != null)
            registeredPlayers.add(ap);
        return ap;
    }

    public List<Armada> listArmadas() {
        return registeredArmadas;
    }

    public void disbandArmada(Armada armada) {
        Armada doomedArmada = null;
        for(Armada a : registeredArmadas) {
            if(a.equals(armada))
                doomedArmada = a;
        }
        if(doomedArmada != null) {
            for(ArmadaPlayer player : doomedArmada.getPlayers())
                player.leaveArmada();
            doomedArmada.getOwner().setRank(ArmadaPlayer.Rank.NONE);
            doomedArmada.getOwner().setArmada(null);
            new File(DarkInit.getPlugin().getDataFolder() + "/armadas/" + doomedArmada.getFriendlyName() + ".yml").delete();
            registeredArmadas.remove(doomedArmada);
        }
    }

    public List<Armada> calculateTopTenByFactionByKill() {
        ArrayList<Armada> copyList = new ArrayList<>();
        copyList.addAll(registeredArmadas);
        copyList.sort((armadaone, armadatwo) -> armadaone.getKills() - armadatwo.getKills());
        Collections.reverse(copyList);
        return copyList.size() < 11 ? copyList : copyList.subList(0, 10);
    }

    public List<Armada> calculateTopTenByFactionByWars() {
        ArrayList<Armada> copyList = new ArrayList<>();
        copyList.addAll(registeredArmadas);
        copyList.sort((armadaone, armadatwo) -> armadaone.getWarsWon() - armadatwo.getWarsWon());
        Collections.reverse(copyList);
        return copyList.size() < 11 ? copyList : copyList.subList(0, 10);
    }

    public List<ArmadaPlayer> calculateTopTenByPlayersByKill() {
        ArrayList<ArmadaPlayer> copyList = new ArrayList<>();
        copyList.addAll(registeredPlayers);
        copyList.sort((armadaone, armadatwo) -> armadaone.getKills() - armadatwo.getKills());
        Collections.reverse(copyList);
        return copyList.size() < 11 ? copyList : copyList.subList(0, 10);
    }

    public List<ArmadaPlayer> calculateTopTenByPlayersByFidelity() {
        ArrayList<ArmadaPlayer> copyList = new ArrayList<>();
        copyList.addAll(registeredPlayers);
        copyList.sort((armadaone, armadatwo) -> (armadaone.getFidelity() < armadatwo.getFidelity()) ? -1 : 1);
        Collections.reverse(copyList);
        return copyList.size() < 11 ? copyList : copyList.subList(0, 10);
    }

    public void save() {
        for(ArmadaPlayer a : registeredPlayers)
            a.save();
        for(Armada b : registeredArmadas)
            b.save();
    }
}
