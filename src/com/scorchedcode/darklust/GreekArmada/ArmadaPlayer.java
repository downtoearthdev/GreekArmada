package com.scorchedcode.darklust.GreekArmada;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import me.clip.deluxechat.DeluxeChat;
import me.clip.deluxechat.objects.DeluxeFormat;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArmadaPlayer {
    private String playerName = "";
    private Armada faction = null;
    private Rank armadaRank = null;
    private God god = null;
    private Sidebar scoreboard = null;
    private Language lang = Language.ENGLISH;
    private boolean hasted = false;
    private boolean running = false;
    private boolean splashing = false;
    private boolean interritory = false;
    private float fidelity = 0.000f;
    private long timePlayed = 0;
    private int kills = 0;
    private int deaths = 0;
    private int ownerHP = 1000;
    private int unclaimedRewards = 0;
    private String fancyName = "";
    private long warCoolDown = 0;

    public ArmadaPlayer(Player player) {
        for(DeluxeFormat f : DeluxeChat.getFormats().values()) {
            if(VaultUtils.getPrimaryPermissionGroup(player).equals(f.getIdentifier())) {
                fancyName = f.getNameColor() + player.getName();
                break;
            }
            else
                fancyName = player.getName();
        }
        this.playerName = player.getName();
        this.armadaRank = Rank.NONE;
        save();
    }

    public ArmadaPlayer(String name, String fancyName, int unclaimed, Armada armada, God god, float fidelity, int playtime, int kills, int deaths, int ownerHP, Rank rank, Language lang) {
        this.playerName = name;
        this.fancyName = fancyName;
        this.unclaimedRewards = unclaimed;
        this.faction = armada;
        this.god = god;
        this.fidelity = fidelity;
        this.timePlayed = playtime;
        this.kills = kills;
        this.deaths = deaths;
        this.ownerHP = ownerHP;
        this.armadaRank = rank;
        this.lang = lang;

    }

    public boolean hasFaction() {
        return !(faction == null);
    }

    public String getName() {
        return playerName;
    }

    public void setArmada(Armada armada) {
        this.faction = armada;
        scoreBoardRefresh();
    }

    public Armada getArmada() {
        return faction;
    }

    public void setLanguage(Language language) {
        this.lang = language;
    }

    public Language getLanguage() {
        return this.lang;
    }
    public God getGod() {
        return god;
    }

    public void setGod(God god) {
        this.god = god;
        scoreBoardRefresh();
    }

    public void setRank(Rank rank) {
        this.armadaRank = rank;
        scoreBoardRefresh();
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public Rank getRank() {
        return armadaRank;
    }

    public float getFidelity() {
        BigDecimal bd = new BigDecimal(fidelity).setScale(3, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public void setFidelity(float fi) {
        BigDecimal bd = new BigDecimal(fi).setScale(3, BigDecimal.ROUND_HALF_UP);;
        this.fidelity = bd.floatValue();
        if((int)this.fidelity == this.fidelity)
            unclaimedRewards++;
        scoreBoardRefresh();
    }
    public void leaveArmada() {
        if(!hasFaction())
            return;
        faction.removeMember(this);
        armadaRank = Rank.NONE;
        faction = null;
        scoreBoardRefresh();
    }

    public long getWarCoolDown() {
        return warCoolDown;
    }

    public void setWarCoolDown(long cool) {
        this.warCoolDown = cool;
    }

    public int getUnclaimedRewards() {
        return unclaimedRewards;
    }

    public void zeroUnclaimedRewards() {
        unclaimedRewards = 0;
    }

    public String getFancyName() {
        return fancyName;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(long time) {
        this.timePlayed = time;
    }

    public void setApolloHasted(boolean haste) {
        this.hasted = haste;
    }

    public boolean isApolloHasted() {
        return hasted;
    }

    public void setHermesHasted(boolean haste) {
        this.running = haste;
    }

    public boolean isHermesHasted() {
        return running;
    }

    public boolean isPoseidonHasted() {
        return splashing;
    }

    public void setPoseidonHasted(boolean haste) {
        this.splashing = haste;
    }

    public void setInTerritory(boolean flag) {
        interritory = flag;
    }

    public boolean isInterritory() {
        return interritory;
    }

    public int getOwnerHP() {
        return ownerHP;
    }

    public void setOwnerHP(int hp) {
        this.ownerHP = hp;
    }
    public void increaseKills() {
        this.kills++;
        scoreBoardRefresh();
    }

    public void increaseDeaths() {
        this.deaths++;
        scoreBoardRefresh();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerName);
    }

    public Sidebar getScoreboard() {
        return scoreboard;
    }
    public boolean hasScoreboard() {
        return scoreboard != null;
    }
    public void setupScoreboard() {
        //Bukkit.broadcastMessage("\u26A1");
        //SidebarString title = new SidebarString(""+ChatColor.YELLOW + "\u26A1" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "ATHENIA" +  ChatColor.BLUE + "\u2646", ""+ChatColor.YELLOW + "\u26A1" + ChatColor.GREEN + ChatColor.BOLD + "ATHENIA" + ChatColor.BLUE + "\u2646", ""+ChatColor.YELLOW + "\u26A1" +ChatColor.DARK_GRAY + ChatColor.BOLD + "ATHENIA" + ChatColor.BLUE + "\u2646");
        SidebarString divider = new SidebarString(ChatColor.RED + "--------------------------", ChatColor.GOLD + "--------------------------",
                ChatColor.YELLOW + "--------------------------", ChatColor.GREEN + "--------------------------", ChatColor.BLUE + "--------------------------", ChatColor.DARK_PURPLE + "--------------------------");
        SidebarString godLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-personal-god", this) + " : " + ChatColor.AQUA + "%greekarmada_personalgod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-personal-god", this) + " : " + ChatColor.BLUE + "%greekarmada_personalgod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-personal-god", this) + " : " +  ChatColor.DARK_BLUE + "%greekarmada_personalgod%");
        SidebarString armadaLine = new SidebarString(getPlayer(), ""+ ChatColor.DARK_RED + ChatColor.DARK_RED + "Armada : " + ChatColor.DARK_AQUA + "%greekarmada_farmada%");
        SidebarString armadaGodLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-armada-god", this) + " :   "  + ChatColor.AQUA + "%greekarmada_agod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-armada-god", this) + " :   " + ChatColor.BLUE + "%greekarmada_agod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-armada-god", this) + " :   " +  ChatColor.DARK_BLUE + "%greekarmada_agod%");
        SidebarString armadaHPLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-healpoint", this) + "\u2764 : " + ChatColor.DARK_AQUA + "%greekarmada_armadahp%");
        SidebarString killLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Kills\u2694 : " + ChatColor.DARK_AQUA + "%greekarmada_pkills%");
        SidebarString deathLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-deaths", this) + "\u2620 : " + ChatColor.DARK_AQUA + "%greekarmada_pdeaths%");
        SidebarString fidelityLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-fidelity", this) + " : " + ChatColor.DARK_AQUA + "%greekarmada_fidelity%");
        SidebarString drachmaeLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Drachmae\u0394 : " + ChatColor.DARK_AQUA + "%greekarmada_bal%");
        scoreboard = new Sidebar("        " +ChatColor.YELLOW + "\u26A1" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "ATHENIA" +  ChatColor.BLUE + "\u2646       ", DarkInit.getPlugin(), 20, /*title,*/ divider, godLine, armadaGodLine, armadaLine, armadaHPLine, killLine, deathLine, fidelityLine, drachmaeLine);
        scoreboard.setPlaceholderPlayerForUpdate(getPlayer());
        //scoreboard.setUpdateDelay(DarkInit.getPlugin(), 20);
        scoreboard.setAllPlaceholders(getPlayer());
        scoreboard.showTo(getPlayer());
        //startScoreBoardRunner();
    }

    public void scoreBoardRefresh() {
        SidebarString divider = new SidebarString(ChatColor.RED + "--------------------------", ChatColor.GOLD + "--------------------------",
                ChatColor.YELLOW + "--------------------------", ChatColor.GREEN + "--------------------------", ChatColor.BLUE + "--------------------------", ChatColor.DARK_PURPLE + "--------------------------");
        SidebarString godLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-personal-god", this) + " : " + ChatColor.AQUA + "%greekarmada_personalgod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-personal-god", this) + " : " + ChatColor.BLUE + "%greekarmada_personalgod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-personal-god", this) + " : " +  ChatColor.DARK_BLUE + "%greekarmada_personalgod%");
        SidebarString armadaLine = new SidebarString(getPlayer(), ""+ ChatColor.DARK_RED + ChatColor.DARK_RED + "Armada : " + ChatColor.DARK_AQUA + "%greekarmada_farmada%");
        SidebarString armadaGodLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-armada-god", this) + " :   "  + ChatColor.AQUA + "%greekarmada_agod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-armada-god", this) + " :   " + ChatColor.BLUE + "%greekarmada_agod%", ChatColor.DARK_RED + Util.getLocaleString("scoreboard-armada-god", this) + " :   " +  ChatColor.DARK_BLUE + "%greekarmada_agod%");
        SidebarString armadaHPLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-healpoint", this) + "\u2764 : " + ChatColor.DARK_AQUA + "%greekarmada_armadahp%");
        SidebarString killLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Kills\u2694 : " + ChatColor.DARK_AQUA + "%greekarmada_pkills%");
        SidebarString deathLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-deaths", this) + "\u2620 : " + ChatColor.DARK_AQUA + "%greekarmada_pdeaths%");
        SidebarString fidelityLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + Util.getLocaleString("scoreboard-fidelity", this) + " : " + ChatColor.DARK_AQUA + "%greekarmada_fidelity%");
        SidebarString drachmaeLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Drachmae\u0394 : " + ChatColor.DARK_AQUA + "%greekarmada_bal%");
        CopyOnWriteArrayList<SidebarString> copys = new CopyOnWriteArrayList<>();
        for(SidebarString s : scoreboard.getEntries())
            copys.add(s);
        for(SidebarString s : copys)
            scoreboard.removeEntry(s);
        //scoreboard.g removeEntry(0);
        //scoreboard.removeEntry(1);
        scoreboard.addEntry(divider,godLine, armadaGodLine, armadaLine, armadaHPLine, killLine, deathLine, fidelityLine, drachmaeLine);
    }

    /*public void scoreBoardRefresh() {
        SidebarString divider = new SidebarString(ChatColor.RED + "--------------------------", ChatColor.GOLD + "--------------------------",
                ChatColor.YELLOW + "--------------------------", ChatColor.GREEN + "--------------------------", ChatColor.BLUE + "--------------------------", ChatColor.DARK_PURPLE + "--------------------------");
        SidebarString godLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "God Personal : " + ChatColor.AQUA + "%greekarmada_personalgod%", ChatColor.DARK_RED + "God Personal : " + ChatColor.BLUE + "%greekarmada_personalgod%", ChatColor.DARK_RED + "God Personal : " +  ChatColor.DARK_BLUE + "%greekarmada_personalgod%");
        SidebarString armadaLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Armada : " + ChatColor.DARK_AQUA + "%greekarmada_farmada%");
        SidebarString armadaGodLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "God Armada :  "  + ChatColor.AQUA + "%greekarmada_agod%", ChatColor.DARK_RED + "God Armada :  " + ChatColor.BLUE + "%greekarmada_agod%", ChatColor.DARK_RED + "God Armada :  " +  ChatColor.DARK_BLUE + "%greekarmada_agod%");
        SidebarString armadaHPLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "HealthPoint\u2764 : " + ChatColor.DARK_AQUA + "%greekarmada_armadahp%");
        SidebarString killLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Kills\u2694 : " + ChatColor.DARK_AQUA + "%greekarmada_pkills%");
        SidebarString deathLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Deaths\u2620 : " + ChatColor.DARK_AQUA + "%greekarmada_pdeaths%");
        SidebarString fidelityLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Fidelity : " + ChatColor.DARK_AQUA + "%greekarmada_fidelity%");
        SidebarString drachmaeLine = new SidebarString(getPlayer(), ChatColor.DARK_RED + "Drachmae\u0394 : " + ChatColor.DARK_AQUA + "%greekarmada_bal%");
        CopyOnWriteArrayList<SidebarString> copys = new CopyOnWriteArrayList<>();
        for(SidebarString s : scoreboard.getEntries())
            copys.add(s);
        for(SidebarString s : copys)
            scoreboard.removeEntry(s);
        //scoreboard.g removeEntry(0);
        //scoreboard.removeEntry(1);
        scoreboard.addEntry(divider,godLine, armadaGodLine, armadaLine, armadaHPLine, killLine, deathLine, fidelityLine, drachmaeLine);
    }*/


    public enum Rank {
        NONE,
        WARRIOR,
        POWERFULWARRIOR,
        MODERATOR,
        OWNER

    }

    public enum Language {
        ENGLISH,
        FRENCH
    }

    public void save() {
        File saveFile = new File(DarkInit.getPlugin().getDataFolder() + "/players/" + getName() + ".yml");
        YamlConfiguration saveData = YamlConfiguration.loadConfiguration(saveFile);
        saveData.set("name", playerName);
        saveData.set("fancyname", fancyName);
        saveData.set("rank", armadaRank.toString());
        saveData.set("rewards", unclaimedRewards);
        saveData.set("armada", (hasFaction()) ? faction.getFriendlyName() : "");
        saveData.set("god", (god != null) ? god.toString() : null);
        saveData.set("fidelity", fidelity);
        saveData.set("playtime", timePlayed);
        saveData.set("kills", kills);
        saveData.set("deaths", deaths);
        saveData.set("ownerhp", ownerHP);
        saveData.set("language", this.lang.toString());
        try {
            saveData.save(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateInv() {

    }
}
