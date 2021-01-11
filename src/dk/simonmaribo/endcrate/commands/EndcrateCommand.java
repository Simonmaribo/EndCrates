/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate.commands;

import dk.simonmaribo.endcrate.CrateLocation;
import dk.simonmaribo.endcrate.EndCrateAPI;
import dk.simonmaribo.endcrate.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class EndcrateCommand implements CommandExecutor {
    private Main main;

    public EndcrateCommand(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("endcrate.admin")){
            sender.sendMessage(Main.getColored("&8[&aEndCrate&8] &7You do not have access to this command!"));
            return true;
        }
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            sender.sendMessage(Main.getColored("&8[&aEndCrate&8] &7Commands&8:"));
            if(sender instanceof Player)
                sender.sendMessage(Main.getColored("&8 » &a/endcrate ( set | remove | list )"));
                sender.sendMessage(Main.getColored("&8 » &a/endcrate tp <id>"));
            sender.sendMessage(Main.getColored("&8 » &a/endcrate givekey <player>"));
            sender.sendMessage(Main.getColored("&8 » &a/endcrate reload"));
            return true;
        }
        if(args[0].equalsIgnoreCase("reload")){
            boolean success;
            try {
                Main.cratelocationsYMLWrapper.reloadConfig();
                Main.cratelocationsYML = Main.cratelocationsYMLWrapper.getConfig();
                Main.endcrateYMLWrapper.reloadConfig();
                Main.endcrateYML = Main.endcrateYMLWrapper.getConfig();
                Main.messagesYMLWrapper.reloadConfig();
                Main.messagesYML = Main.messagesYMLWrapper.getConfig();
                Main.rc.reloadLocations();
                Main.rc.reload();
                success = true;

            } catch(Exception e){
                e.printStackTrace();
                success = false;
            }
            if(success) sender.sendMessage(Main.getColored("&8[&aEndCrate&8] &aReload successfully completed"));
            if(!success) sender.sendMessage(Main.getColored("&8[&aEndCrate&8] &cAn error occurred. Please check the console."));
        }
        if(args[0].equalsIgnoreCase("givekey")){
            if (args.length == 1){
                if(sender instanceof Player){
                    EndCrateAPI.giveKey((Player) sender, 1);
                    return true; }
                sender.sendMessage(Main.getColored("&8[&aEndCrate&8] &7You can't give the console a key."));
            }else{
                if(Bukkit.getPlayer(args[1]) != null){
                    EndCrateAPI.giveKey(Bukkit.getPlayer(args[1]), 1);
                    return true;
                }
                sender.sendMessage(Main.getColored("&8[&aEndCrate&8] &7" + args[1] + " isn't online."));
            }
        }
        if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("list")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(args[0].equalsIgnoreCase("set")){
                    Block target = player.getTargetBlock((Set<Material>)null, 100);
                    if(target.getType() == Material.ENDER_PORTAL_FRAME){
                        Location crateLocation = target.getLocation();
                        if(!Main.rc.getCrateLocations().contains(crateLocation)) {
                            CrateLocation.addCrate(crateLocation);
                            player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7You placed a crate at&8: &7x: &a" + crateLocation.getX() + " &7y: &a" + crateLocation.getY() + " &7z: &a" + crateLocation.getZ() + " &8(&a" + crateLocation.getWorld().getName() + "&8)"));
                        }else{
                            player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7This block is already occupied by a crate."));
                        }
                    }else{
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7You need to look at a ENDER_PORTAL_FRAME"));
                    }
                }
                if(args[0].equalsIgnoreCase("list")){
                    if (Main.rc.getCrateLocations().size() == 0){
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7No crates registered!"));
                        return true;
                    }
                    player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7Locations&8:"));
                    for(String id : Main.cratelocationsYML.getConfigurationSection("CrateLocations.EndCrate").getKeys(false)){
                        double x = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + id + ".x");
                        double y = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + id + ".y");
                        double z = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + id + ".z");
                        String w = Main.cratelocationsYML.getString("CrateLocations.EndCrate." + id + ".world");
                        player.sendMessage(Main.getColored("&8 » &a" + id + "&8 : &7x: &a" + x + " &7y: &a" + y + " &7z: &a" + z + " &8(&a" + w + "&8)"));

                    }
                }
                if(args[0].equalsIgnoreCase("remove")){
                    if(args.length != 2) {
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7Usage&8: &a/endcrate remove <id>"));
                        return true;
                    }
                    if (Main.rc.getCrateLocations().size() == 0) {
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7No crates registered!"));
                        return true;
                    }
                    if (!Main.cratelocationsYML.getConfigurationSection("CrateLocations.EndCrate").getKeys(false).contains(args[1])){
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7No crate with id &a" + args[1] + " &7found"));
                        return true;
                    }
                    CrateLocation.removeCrate(args[1]);
                    player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7You have removed a crate with id &a" + args[1]));

                }
                if(args[0].equalsIgnoreCase("tp")){
                    if(args.length != 2) {
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7Usage&8: &a/endcrate tp <id>"));
                        return true;
                    }
                    if (Main.rc.getCrateLocations().size() == 0) {
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7No crates registered!"));
                        return true;
                    }
                    if (!Main.cratelocationsYML.getConfigurationSection("CrateLocations.EndCrate").getKeys(false).contains(args[1])){
                        player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7No crate with id &a" + args[1] + " &7found"));
                        return true;
                    }
                    double x = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + args[1] + ".x");
                    double y = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + args[1] + ".y");
                    double z = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + args[1] + ".z");
                    String w = Main.cratelocationsYML.getString("CrateLocations.EndCrate." + args[1] + ".world");
                    World world = Bukkit.getWorld(w);
                    player.teleport(new Location(world, x, y+1, z));
                    player.sendMessage(Main.getColored("&8[&aEndCrate&8] &7You teleported to crate with id &a" + args[1]));
                }


                return true;
            }
            sender.sendMessage(Main.getColored("&8[&aEndCrate&8] &7This command is player-only!"));
        }
        return true;
    }
}
