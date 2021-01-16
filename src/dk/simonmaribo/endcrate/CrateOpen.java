/*

    AUTHOR: FlupMC
    CONTRIBUTORS: Steilgaard
    WEBSITE: simonmaribo.dk
    SUPPORT: discord.simonmaribo.dk

    Copyright 2021 © Simon Maribo

*/
package dk.simonmaribo.endcrate;

import dk.simonmaribo.endcrate.events.PlayerOpenCrate;
import dk.simonmaribo.endcrate.utils.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.particle.ParticleEffect;

import java.util.Collection;

public class CrateOpen {

    public static void openEndCrate(Block block, Player player){
        Location portalLoc = block.getLocation();
        Location armorStandLoc = portalLoc.add(0.5, -0.5, 0.5);


        // Custom Event
        Prize generatedPrize = new RandomPrize().getPrize();
        PlayerOpenCrate playerOpenCrate = new PlayerOpenCrate(player, block, generatedPrize);
        Bukkit.getServer().getPluginManager().callEvent(playerOpenCrate);
        Prize prizeWon = playerOpenCrate.getPrize();

        if(!playerOpenCrate.isCancelled()){
            String holoOverCrate = Main.messagesYML.getString("hologram-over-crate");
            ArmorStand armorStand = (ArmorStand) armorStandLoc.getWorld().spawnEntity(armorStandLoc, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setVisible(false);
            armorStand.setCustomName("EndCrateArmorStand");
            Main.getArmorStandList().add(armorStand);


            ItemStack head = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/1fe8e7f2db8eaa88a041c89d4c353d066cc4edef77edcf5e08bb5d3baad");
            armorStand.setHelmet(head);
            final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            new BukkitRunnable(){
                int i = 0;
                public void run() {
                    i++;
                    armorStandLoc.add(0,0.025,0);
                    armorStand.teleport(armorStandLoc);
                    ParticleEffect.SPELL_WITCH.send(players,armorStand.getEyeLocation(),0,0,0,0.1,1);
                    if(i >= 7) cancel();
                }
            }.runTaskTimer(Main.getInstance(),0L, 1L);
            new BukkitRunnable(){
                public void run() {
                    player.playSound(armorStand.getLocation(), Sound.CHICKEN_EGG_POP, (float) 1.99, 0);
                    cancel();
                }
            }.runTaskTimer(Main.getInstance(),7L, 1L);

            new BukkitRunnable(){
                int i = 0;
                public void run() {
                    i++;
                    if (i == 1) player.playSound(armorStand.getLocation(), Sound.ENDERMAN_TELEPORT, (float) 1.99, 0);
                    armorStandLoc.add(0,0.15,0);
                    armorStand.teleport(armorStandLoc);
                    ParticleEffect.SPELL_WITCH.send(players,armorStand.getEyeLocation(),0,0,0,0.1,1);
                    if(i >= 8) cancel();
                }
            }.runTaskTimer(Main.getInstance(),27L, 1L);
            int delay = 80;
            new BukkitRunnable(){
                int i = 1;
                public void run() {
                    if(i == 1)player.playSound(armorStand.getLocation(), Sound.PORTAL_TRIGGER, (float) 1.99, (float) 1.05);
                    ParticleEffect.SPELL_WITCH.send(players,armorStand.getLocation().add(0,1,0),0,0,0,0.1,3);
                    Location loc = armorStand.getLocation();
                    loc.setYaw((float) (i * i * 0.25));
                    loc.add(0, 0.001, 0);
                    armorStand.teleport(loc);
                    if(i >= delay + 1) cancel();
                    i++;
                }
            }.runTaskTimer(Main.getInstance(),55L, 1L);

            new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    i++;
                    if (i == 1) {
                        Location loc = armorStand.getEyeLocation();
                        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0f, false, false);
                        ParticleEffect.CLOUD.send(players, armorStand.getLocation().add(0, 1, 0), 0, 0, 0, 0.1, 10);
                        player.playSound(armorStand.getLocation(), Sound.LEVEL_UP, (float) 1.99, 0);
                    }
                    if (i == 50) {
                        armorStand.remove();
                        Main.getArmorStandList().remove(armorStand);

                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), delay + 55L, 1L);
            new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    i++;
                    if (i == 1) {
                        ArmorStand hologram = (ArmorStand) armorStand.getWorld().spawnEntity(armorStand.getLocation().add(0, -0.5, 0), EntityType.ARMOR_STAND);
                        hologram.setGravity(false);
                        hologram.setVisible(false);
                        hologram.setCustomNameVisible(true);
                        hologram.setCustomName(Main.getColored(holoOverCrate));
                        Main.getArmorStandList().add(hologram);

                        ArmorStand hologram2 = (ArmorStand) armorStand.getWorld().spawnEntity(armorStand.getLocation().add(0, -0.8, 0), EntityType.ARMOR_STAND);
                        hologram2.setGravity(false);
                        hologram2.setVisible(false);
                        hologram2.setCustomNameVisible(true);
                        hologram2.setCustomName(Main.getColored(prizeWon.getName()));
                        Main.getArmorStandList().add(hologram2);

                    }
                    if(i == 5){
                        prizeWon.runMessages(player);
                        prizeWon.runCommands(player);
                    }
                    if (i == 50) {
                        for (Entity ent : armorStand.getWorld().getChunkAt(armorStand.getLocation()).getEntities()){
                            if(ent.getCustomName() != null) {
                                if (ent.getCustomName().equals(Main.getColored(holoOverCrate)) || ent.getCustomName().equals(Main.getColored(prizeWon.getName()))) {
                                    Main.getArmorStandList().remove((ArmorStand) ent);
                                    ent.remove();
                                }
                            }
                        }
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), delay + 55L, 1L);

        }
    }

}
