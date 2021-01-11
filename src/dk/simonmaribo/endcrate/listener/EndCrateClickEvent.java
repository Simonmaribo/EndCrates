/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate.listener;

import dk.simonmaribo.endcrate.CrateOpen;
import dk.simonmaribo.endcrate.Main;

import org.bukkit.Material;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;


public class EndCrateClickEvent implements Listener {

    Main main;
    public EndCrateClickEvent(Main main){
        this.main = main;
    }

    @EventHandler
    public void onCrateClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();

        if(action == Action.LEFT_CLICK_BLOCK){
            if(event.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME){
                if(Main.rc.getCrateLocations().contains(event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);
                    if (Main.endcrateYML.getBoolean("EndCrate.Preview")) {
                        player.openInventory(Main.rc.getPreview());

                    }
                }
            }
        }

        if(action == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME) {
                /*ItemStack i = player.getItemInHand();
                if(player.getItemInHand().getType() == Main.rc.getKey().getType()) {
                    if (Main.getColored(player.getItemInHand().getItemMeta().getDisplayName()).equals(Main.getColored(Main.rc.getKey().getItemMeta().getDisplayName()))) {
                        if (Main.getColored(player.getItemInHand().getItemMeta().getLore()) == Main.getColored(Main.rc.getKey().getItemMeta().getLore())) {
                            // Check if crates is a crate location.
                            i.setAmount(i.getAmount()-1);
                            player.getInventory().remove(i);
                            CrateOpen.openEndCrate(event.getClickedBlock(), player);
                            event.setCancelled(true);
                        }
                    }
                }*/
                if (Main.rc.getCrateLocations().contains(event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);
                    if (player.getInventory().containsAtLeast(Main.rc.getKey(), 1)) {
                        Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(event.getClickedBlock().getLocation(), 1d, 1d, 1d);
                        boolean alreadyUsed = false;
                        if (!nearbyEntities.isEmpty()) {
                            for (Entity entity : nearbyEntities) {
                                if (entity.getCustomName() != null) {
                                    if (entity.getType() == EntityType.ARMOR_STAND && entity.getCustomName().equals("EndCrateArmorStand")) {
                                        alreadyUsed = true;
                                    }
                                }
                            }

                        }
                        if (!alreadyUsed) {
                            player.getInventory().removeItem(Main.rc.getKey());
                            CrateOpen.openEndCrate(event.getClickedBlock(), player);
                        } else {
                            player.sendMessage(Main.getColored(Main.messagesYML.getString("already-being-used")));

                        }
                        return;
                    }
                    player.sendMessage(Main.getColored(Main.messagesYML.getString("no-key")));
                }
            }
        }
    }
}






