/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate.events;

import dk.simonmaribo.endcrate.Prize;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerOpenCrate extends Event {
    private static final HandlerList handlers = new HandlerList();


    private Player player;
    private Block block;
    private Prize prize;
    private boolean isCancelled;

    public PlayerOpenCrate(Player player, Block block, Prize prize){
        this.player = player;
        this.block = block;
        this.prize = prize;
        this.isCancelled = false;
    }

    public Block getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
    }

    public Prize getPrize() {
        return prize;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
