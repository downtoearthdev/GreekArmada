package com.scorchedcode.darklust.GreekArmada;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.anhcraft.spaciouslib.utils.VaultUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArmadaPlaceholders extends PlaceholderExpansion {

    private ArmadaPlayer ap;

    @Override
    public String getIdentifier() {
        return "greekarmada";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        //return super.onPlaceholderRequest(p, params);
        ArmadaPlayer ap = DarkInit.getPlugin().getRegisteredArmadas().getPlayer(p.getName());
        if (ap != null) {
                if (params.equals("armada"))
                    return (ap.hasFaction()) ? ap.getArmada().getName() : "";
                if(params.equals("farmada"))
                    return (ap.hasFaction()) ? ap.getArmada().getFriendlyName() : "N/A";
                if (params.equals("rank"))
                    return (ap.hasFaction()) ? ap.getRank().toString() : "";
                if (params.equals("desc")) {
                    if(ap.hasFaction()) {
                        String desc = " =[" + ap.getArmada().getName() + ChatColor.RESET + "]=" + "\n" +
                                ChatColor.GOLD + "Owner: " + ChatColor.RED + ap.getArmada().getOwner().getName() + "\n" +
                                ChatColor.GOLD + "God: " + ChatColor.BLUE + ((ap.getArmada().getGod() != null) ? Util.toProperUppercaseString(ap.getArmada().getGod().toString()) : "None") + "\n" +
                                ChatColor.GREEN + ap.getArmada().getKills() + "K/" + ap.getArmada().getDeaths() + "D\n" +
                                "K/D: " + ap.getArmada().getKDRatio();
                        return desc;
                    }
                    return "";
                }
                if(params.equals("bal"))
                    return String.valueOf(VaultUtils.getBalance(ap.getPlayer()));
                if(params.equals("fidelity"))
                    return String.valueOf(ap.getFidelity());
                if(params.equals("kills"))
                    return (ap.hasFaction()) ? String.valueOf(ap.getArmada().getKills()) : "N/A";
                if(params.equals("deaths"))
                    return (ap.hasFaction()) ? String.valueOf(ap.getArmada().getDeaths()) : "N/A";
            if(params.equals("pkills"))
                return String.valueOf(ap.getKills());
            if(params.equals("pdeaths"))
                return String.valueOf(ap.getDeaths());
                if(params.equals("armadahp"))
                    return  ((ap.hasFaction()) ? String.valueOf(ap.getArmada().getPower()) : "N/A");
                if(params.equals("agod"))
                    return (ap.hasFaction() && ap.getArmada().getGod() != null) ? Util.toProperUppercaseString(ap.getArmada().getGod().toString()) : "None";
                if(params.equals("personalgod"))
                    return  (ap.getGod() != null) ? Util.toProperUppercaseString(ap.getGod().toString()) : "None";
        }
        return "";
    }

    @Override
    public String getAuthor() {
        return "Darklust";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
