/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate.listener;

import dk.simonmaribo.endcrate.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EndCratePreviewClickEvent implements Listener {
    private Main main;

    public EndCratePreviewClickEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEndCratePreviewClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory().getTitle() == Main.rc.getPreview().getTitle()){
            if (String.valueOf(event.getClickedInventory()).replaceAll("Custom", "").equals(String.valueOf(Main.rc.getPreview()).replaceAll("Custom", ""))) {
                event.setCancelled(true);
            }
        }

    }
}
