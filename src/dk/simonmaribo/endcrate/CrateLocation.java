/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate;

import org.bukkit.Location;

public class CrateLocation {


    public static void addCrate(Location loc){
        int n = Main.cratelocationsYML.getInt("Crates");
        n++;
        Main.cratelocationsYML.set("Crates", n);
        Main.cratelocationsYML.set("CrateLocations.EndCrate." + String.valueOf(n) + ".world", loc.getWorld().getName());
        Main.cratelocationsYML.set("CrateLocations.EndCrate." + String.valueOf(n) + ".x", loc.getX());
        Main.cratelocationsYML.set("CrateLocations.EndCrate." + String.valueOf(n) + ".y", loc.getY());
        Main.cratelocationsYML.set("CrateLocations.EndCrate." + String.valueOf(n) + ".z", loc.getZ());
        Main.cratelocationsYMLWrapper.saveConfig();
        Main.rc.reloadLocations();
    }
    public static void removeCrate(String id){
        Main.cratelocationsYML.set("CrateLocations.EndCrate." + id, null);
        Main.cratelocationsYMLWrapper.saveConfig();
        Main.rc.reloadLocations();
    }


}
