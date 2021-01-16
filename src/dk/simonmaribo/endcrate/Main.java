/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate;

import dk.simonmaribo.endcrate.commands.EndcrateCommand;

import dk.simonmaribo.endcrate.listener.EndCrateClickEvent;
import dk.simonmaribo.endcrate.listener.EndCratePreviewClickEvent;
import dk.simonmaribo.endcrate.listener.EntityClickEvent;
import dk.simonmaribo.endcrate.utils.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main extends JavaPlugin {

    private static Main instance;

    public static ConfigWrapper endcrateYMLWrapper;
    public static FileConfiguration endcrateYML;

    public static ConfigWrapper cratelocationsYMLWrapper;
    public static FileConfiguration cratelocationsYML;

    public static ConfigWrapper messagesYMLWrapper;
    public static FileConfiguration messagesYML;

    public static CrateConfig rc;
    public static Plugin getInstance() {
        return instance;
    }

    private static List<ArmorStand> armorStandList = new ArrayList<>();

    public static List<ArmorStand> getArmorStandList() {
        return armorStandList;
    }

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("ParticleLIB") == null){
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().severe("Couldn't find ParticleLIB, the plugin is disabling itself!");
        }
        instance = this;
        if(!(new File(getDataFolder(), "crateconfig.yml").exists())) saveResource("crateconfig.yml", false);
        if(!(new File(getDataFolder(), "cratelocations.yml").exists())) saveResource("cratelocations.yml", false);
        if(!(new File(getDataFolder(), "messages.yml").exists())) saveResource("messages.yml", false);

        endcrateYMLWrapper = new ConfigWrapper(this, null, "crateconfig.yml");
        endcrateYML = endcrateYMLWrapper.getConfig();

        cratelocationsYMLWrapper = new ConfigWrapper(this, null, "cratelocations.yml");
        cratelocationsYML = cratelocationsYMLWrapper.getConfig();

        messagesYMLWrapper = new ConfigWrapper(this, null, "messages.yml");
        messagesYML = messagesYMLWrapper.getConfig();

        getCommand("endcrate").setExecutor(new EndcrateCommand(this));
        getServer().getPluginManager().registerEvents(new EndCrateClickEvent(this), this);
        getServer().getPluginManager().registerEvents(new EndCratePreviewClickEvent(this), this);
        getServer().getPluginManager().registerEvents(new EntityClickEvent(this), this);
        rc = new CrateConfig();
        rc.reload();
        rc.reloadLocations();
    }

    @Override
    public void onDisable() {
        for(Entity ent : armorStandList){
            ent.remove();
        }
    }

    public static String getColored(String s){return ChatColor.translateAlternateColorCodes('&', s);}

    public static List<String> getColored(List<String> lores){
        List<String> coloredLores = new ArrayList<String>();
        for(String lore : lores){
            coloredLores.add(ChatColor.translateAlternateColorCodes('&', lore)); }
        return coloredLores;
    }
}
