/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/

package dk.simonmaribo.endcrate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class Prize {

    private float percentage;

    private String name;

    private List<String> commands;
    private List<String> messages;

    private ItemStack previewItem;

    public Prize(String path){
        this.name = Main.endcrateYML.getString("EndCrate.Winnings." + path + ".Name");
        Material itemType = Material.valueOf(Main.endcrateYML.getString("EndCrate.Winnings." + path + ".Item-Type").toUpperCase());
        boolean isEnchanted = Main.endcrateYML.getBoolean("EndCrate.Winnings." + path + ".Enchanted");
        this.percentage = (float) Main.endcrateYML.getDouble("EndCrate.Winnings." + path + ".Percentage");
        this.commands = Main.endcrateYML.getStringList("EndCrate.Winnings." + path + ".Commands");
        this.messages = Main.endcrateYML.getStringList("EndCrate.Winnings." + path + ".Messages");

        ItemStack previewItem = new ItemStack(itemType);
        if(isEnchanted) previewItem.addUnsafeEnchantment(Enchantment.LURE, 1);

        ItemMeta itemMeta = previewItem.getItemMeta();
        itemMeta.setDisplayName(Main.getColored(name));
        if(!Main.endcrateYML.getBoolean("EndCrate.Hide-Percentages"))
            itemMeta.setLore(Main.getColored(Collections.singletonList(Main.messagesYML.getString("chance-lore").replace("{CHANCE}", String.valueOf(percentage)))));

        if(isEnchanted) itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        previewItem.setItemMeta(itemMeta);

        this.previewItem = previewItem;
    }

    public String getName(){
        return this.name;
    }
    public List<String> getCommands(){
        return this.commands;
    }
    public List<String> getMessages(){
        return this.messages;
    }
    public ItemStack getPreviewItem(){
        return this.previewItem;
    }
    public float getPercentage() {
        return this.percentage;
    }

    public void runCommands(Player player){
        for(String command : this.commands)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
    }
    public void runMessages(Player player){
        for(String message : this.messages)
            player.sendMessage(Main.getColored(message.replace("%Player%", player.getName())));

    }
}

