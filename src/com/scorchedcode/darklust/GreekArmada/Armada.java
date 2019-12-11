package com.scorchedcode.darklust.GreekArmada;

import com.earth2me.essentials.utils.LocationUtil;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Armada {
    private String name = "";
    private String friendlyName = "";
    private String description = "";
    private ArmadaPlayer owner = null;
    private List<Block> claim = null;
    private Location home = null;
    private God god = null;
    private String[] rules = new String[10];
    private CopyOnWriteArrayList<ArmadaPlayer> members = new CopyOnWriteArrayList<>();
    private int power = 1000;
    private boolean invite = false;
    private int kills = 0;
    private int deaths = 0;
    private long lastlogin = 0;
    private int warswon = 0;

    public Armada(String name, ArmadaPlayer owner) {
        this.friendlyName = name.replaceAll("&[\\d\\D]", "");
        this.name = Util.parseColorString(name);
        this.owner = owner;
        this.members.add(owner);
        this.power = owner.getOwnerHP();
        owner.setArmada(this);
        owner.setRank(ArmadaPlayer.Rank.OWNER);
        save();
        //Bukkit.broadcastMessage(this.owner.);

    }

    public Armada(String name, String fancyname, String desc, List<Block> claim, Location home, God god, String[] rules, int power, boolean invite, int kills, int deaths, long lastlogin, int warswon) {
        this.name = fancyname;
        this.friendlyName = name;
        this.description = desc;
        this.claim = claim;
        this.home = home;
        this.god = god;
        this.rules = rules;
        this.power = power;
        this.invite = invite;
        this.kills = kills;
        this.deaths = deaths;
        this.lastlogin = lastlogin;
        this.warswon = warswon;
    }

    public ArmadaPlayer getOwner() {
        return owner;
    }

    public CopyOnWriteArrayList<ArmadaPlayer> getPlayers() {
        return members;
    }

    public ArmadaPlayer getPlayer(String name) {
        for(ArmadaPlayer p : members) {
            if(p.getName().equals(name)) {
                return p;
            }
        }
        if(owner.getName().equals(name))
            return owner;
        return null;
    }

    public String getClaimSize() {
        if(claim != null) {
            Block a = claim.get(0);
            Block b = claim.get(claim.size()-1);
            String length = String.valueOf(Math.abs(a.getX() - b.getX()));
            String width = String.valueOf(Math.abs(a.getZ() - b.getZ()));
            return length + "x" + width;
        }
        return "0x0";
    }

    public void setClaim(Location a, Location b) {
        this.claim = DarkInit.blocksFromTwoPoints(a, b);
    }

    public void unsetClaim() {
        this.claim = null;
        this.home = null;
    }

    public boolean isWithinClaim(Location a) {
        if(claim != null) {
            for (Block b : claim) {
                //Location fusedLoc = new Location(b.getWorld(), b.getLocation().getX(), a.getY(), b.getLocation().getZ());
                if (b.getWorld().getBlockAt(a).getX() == b.getX() && b.getWorld().getBlockAt(a).getZ() == b.getZ())
                    return true;
            }
        }
        return false;
    }

    public boolean hasClaim() {
        return claim != null;
    }

    public long getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(long log) {
        this.lastlogin = log;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public God getGod() {
        return god;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setName(String name) {
        File fm = new File(DarkInit.getPlugin().getDataFolder() + "/armadas/" + friendlyName + ".yml");
        this.friendlyName = name.replaceAll("&[\\d\\D]", "");
        this.name = Util.parseColorString(name);
        fm.renameTo(new File(DarkInit.getPlugin().getDataFolder() + "/armadas/" + friendlyName + ".yml"));
        updateMemberScoreboards();
    }

    public boolean addMember(ArmadaPlayer p) {
        if(!p.hasFaction()) {
            p.setArmada(this);
            p.setRank(ArmadaPlayer.Rank.WARRIOR);
            members.add(p);
            return true;
        }
        return false;
    }
    public void increaseKills() {
        kills++;
    }

    public void increaseDeath() {
        deaths++;
    }

    public int getWarsWon() {
        return warswon;
    }

    public void setWarsWon(int won) {
        warswon = won;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKDRatio() {
        return (kills == 0 || deaths == 0) ? 0 : kills/deaths;
    }

    public String[] getRules() {
        return rules;
    }

    public boolean removeMember(ArmadaPlayer p) {
        if(members.contains(p)) {
            p.setRank(ArmadaPlayer.Rank.NONE);
            members.remove(p);
            return true;
        }
        return false;
    }

    public boolean rankMember(ArmadaPlayer ap, boolean downRank) {
        if(members.contains(ap)) {
            if(ap.getRank() == ArmadaPlayer.Rank.WARRIOR) {
                if(downRank)
                    return true;
                ap.setRank(ArmadaPlayer.Rank.POWERFULWARRIOR);
                return true;
            }
            else if(ap.getRank() == ArmadaPlayer.Rank.POWERFULWARRIOR) {
                if(downRank) {
                    ap.setRank(ArmadaPlayer.Rank.WARRIOR);
                }
                else {
                    ap.setRank(ArmadaPlayer.Rank.MODERATOR);
                }
            }
            else if(ap.getRank() == ArmadaPlayer.Rank.MODERATOR) {
                if(downRank) {
                    ap.setRank(ArmadaPlayer.Rank.POWERFULWARRIOR);
                }
            }
            return true;
        }
        return false;
    }

    public int getPower() {
        return power;
    }

    public void losePower() {
        if(power - 5 < 0)
            power = 0;
        else
            power-=5;
        getOwner().setOwnerHP(power);
        updateMemberScoreboards();
    }

    public void gainPower() {
        if(power + 2 > 1000)
            power = 1000;
        else
            power+=2;
        getOwner().setOwnerHP(power);
        updateMemberScoreboards();
    }

    public void setGod(God god) {
        this.god = god;
        updateMemberScoreboards();
    }

    public void setHome(Location loc) {
        this.home = loc;
    }

    public Location getHome() {
        return home;
    }

    public boolean hasHome() {
        return home != null;
    }
    public void setInvite(boolean invite) {
        this.invite = invite;
    }

    public boolean isInvite() {
        return invite;
    }

    private void updateMemberScoreboards() {
        for(ArmadaPlayer a : members)
            a.scoreBoardRefresh();
    }

    public void loadFinalStep() {
        for(ArmadaPlayer p : DarkInit.getPlugin().getRegisteredArmadas().getArmadaPlayers()) {
            if(p.hasFaction() && p.getArmada().equals(this)) {
                members.add(p);
                if(p.getRank() == ArmadaPlayer.Rank.OWNER)
                    owner = p;
            }
        }
    }

    public void save() {
        File saveFile = new File(DarkInit.getPlugin().getDataFolder() + "/armadas/" + getFriendlyName() + ".yml");
        YamlConfiguration saveData = YamlConfiguration.loadConfiguration(saveFile);
        saveData.set("name", getFriendlyName());
        saveData.set("fancyname", getName());
        saveData.set("description", description);
        saveData.set("claimx", (claim != null) ? LocationUtils.loc2str(claim.get(0).getLocation()) : null);
        saveData.set("claimy", (claim != null) ? LocationUtils.loc2str(claim.get(claim.size()-1).getLocation()) : null);
        saveData.set("home", (home != null) ? LocationUtils.loc2str(home) : null);
        saveData.set("god", (god != null) ? god.toString() : null   );
        saveData.set("rules", rules);
        ArrayList<String> memberNames = new ArrayList<>();
        for(ArmadaPlayer a : members)
            memberNames.add(a.getName());
        saveData.set("power", power);
        saveData.set("invite", invite);
        saveData.set("kills", kills);
        saveData.set("deaths", deaths);
        saveData.set("lastlogin", lastlogin);
        saveData.set("warswon", warswon);
        try {
            saveData.save(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
