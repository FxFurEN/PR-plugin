package org.pr.plugin.prplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PRPlugin extends JavaPlugin implements Listener {

    Map<UUID, Long> lastInteract = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void test_anim_show(PlayerInteractEvent e) {

        int cooldown = 4000;
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && p.isSneaking()) {
            Block block = e.getClickedBlock();
            if (block.getType() == Material.SNOW) {
                double distance = p.getLocation().distance(block.getLocation());
                if (distance >= 1.3) return;
                else {
                    Player player = e.getPlayer();
                    UUID playerId = player.getUniqueId();
                    Long lastInteraction = lastInteract.get(playerId);
                    long currentTime = System.currentTimeMillis();
                    if (lastInteraction != null && currentTime - lastInteraction < cooldown) {
                        e.setCancelled(true);
                        return;
                    }

                    if (e.getAction() == Action.RIGHT_CLICK_AIR && p.isSneaking()) {
                        p.performCommand("emotes play \"ThrowAsnowball\"");

                        lastInteract.put(playerId, currentTime);
                    }
                    Bukkit.getServer().getScheduler().runTaskLater(this, () ->
                    {
                        p.performCommand("emotes stop ");
                        ItemStack item = new ItemStack(Material.SNOWBALL);
                        p.getInventory().addItem(item);
                        e.getClickedBlock().setType(Material.AIR);
                    }, 60L);
                }
            }
        }
    }

    @EventHandler
    public void test_mmoitems(PlayerInteractEvent e) {
        int cooldown = 4000;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item.getType() == Material.DIAMOND_HOE) {
            // Проверка, что игрок нажал SHIFT + ПКМ
            UUID playerId = p.getUniqueId();
            Long lastInteraction = lastInteract.get(playerId);
            long currentTime = System.currentTimeMillis();
            if (lastInteraction != null && currentTime - lastInteraction < cooldown) {
                e.setCancelled(true);
                return;
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && p.isSneaking()) {
                p.performCommand("emotes play \"ThrowAsnowball\"");

                lastInteract.put(playerId, currentTime);
            }
            Bukkit.getServer().getScheduler().runTaskLater(this, () ->
            {
                p.performCommand("emotes stop ");
            }, 60L);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
