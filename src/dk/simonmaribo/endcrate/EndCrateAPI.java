/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EndCrateAPI {

    public static void openPreview(Player player) {
        player.openInventory(Main.rc.getPreview());
    }

    public static void giveKey(Player player, int amount) {
        ItemStack key = Main.rc.getKey();
        key.setAmount(amount);
        player.getInventory().addItem(key);
    }

    public static void openCrate(Location location, Player player) {
        Block block = location.getBlock();
        CrateOpen.openEndCrate(block, player);
    }


}