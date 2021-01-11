/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CrateConfig {

    private ItemStack key;
    private Inventory preview;
    private ArrayList<Prize> prizes = new ArrayList<>();
    private ArrayList<Location> crateLocations = new ArrayList<>();
    public CrateConfig(){
    }
    public void reloadLocations(){
        crateLocations.clear();
        try{
            for (String id : Main.cratelocationsYML.getConfigurationSection("CrateLocations.EndCrate").getKeys(false)) {
                double x = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + id + ".x");
                double y = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + id + ".y");
                double z = Main.cratelocationsYML.getDouble("CrateLocations.EndCrate." + id + ".z");
                String w = Main.cratelocationsYML.getString("CrateLocations.EndCrate." + id + ".world");
                World world = Bukkit.getWorld(w);
                crateLocations.add(new Location(world, x, y, z));
            }
        } catch(NullPointerException exception){
            Bukkit.getConsoleSender().sendMessage("[EndCrate] No Locations found! Create some crates!");
        }
    }
    public void reload() {
        // Generate KEY
        String ITEM_TYPE = Main.endcrateYML.getString("EndCrate.Key.Item");
        String ITEM_NAME = Main.endcrateYML.getString("EndCrate.Key.Name");
        List<String> ITEM_LORE = Main.endcrateYML.getStringList("EndCrate.Key.Lore");
        boolean ITEM_ENCHANTED = Main.endcrateYML.getBoolean("EndCrate.Key.Enchanted");

        ItemStack key = new ItemStack(Material.valueOf(ITEM_TYPE.toUpperCase()));

        if(ITEM_ENCHANTED) key.addUnsafeEnchantment(Enchantment.LURE, 1);

        ItemMeta meta = key.getItemMeta();

        meta.setDisplayName(Main.getColored(ITEM_NAME));
        meta.setLore(Main.getColored(ITEM_LORE));
        if(ITEM_ENCHANTED) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        key.setItemMeta(meta);

        this.key = key;

        // Generate Preview INV
        int size = 9;
        Set<String> prizes = Main.endcrateYML.getConfigurationSection("EndCrate.Winnings").getKeys(false);
        if (prizes.size() > 9) size = 18;
        if (prizes.size() > 18) size = 27;
        if (prizes.size() > 27) size = 36;
        if (prizes.size() > 36) size = 45;
        if (prizes.size() > 45) size = 54;
        Inventory inventory = Bukkit.createInventory(null, size, Main.getColored(Main.messagesYML.getString("preview-title")));


        float totalPercentage = 0.00f;

        int n = 0;
        for(String winning : prizes){
            Prize prize = new Prize(winning);
            inventory.setItem(n, prize.getPreviewItem());
            this.prizes.add(prize);
            totalPercentage += prize.getPercentage();
            n++;
        }
        this.preview = inventory;
        if(totalPercentage > 100.00f){
            Main.getInstance().getLogger().warning("The total percentage for the EndCrate is over 100%");
        }
        if(totalPercentage < 100.00f){
            Main.getInstance().getLogger().warning("The total percentage for the EndCrate is under 100%");
        }

    }
    public ItemStack getKey(){
        return this.key;
    }

    public Inventory getPreview(){
        return this.preview;
    }
    public ArrayList<Prize> getPrizes(){
        return this.prizes;
    }

    public ArrayList<Location> getCrateLocations() {
        return crateLocations;
    }
}
