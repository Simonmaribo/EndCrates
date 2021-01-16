/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/

package dk.simonmaribo.endcrate.listener;

import dk.simonmaribo.endcrate.Main;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class EntityClickEvent implements Listener {
    private Main main;
    public EntityClickEvent(Main main){
        this.main = main;
    }

    @EventHandler
    public void onArmorStandClick(PlayerInteractAtEntityEvent event){
        if(!(event.getRightClicked() instanceof ArmorStand)) return;

        if(Main.getArmorStandList().contains((ArmorStand) event.getRightClicked())) event.setCancelled(true);

    }
}
