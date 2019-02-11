package me.nowaha.disablebuild;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public final class Disablebuild extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        playersCannotBuild = loadConfigValue("playersCannotBuild");
        playersCannotPlace = loadConfigValue("playersCannotPlace");
        playersCannotBreak = loadConfigValue("playersCannotBreak");

        getLogger().info("§aPlugin has been enabled.");
    }

    List<String> loadConfigValue(String path) {
        return loadConfigValue(path, new ArrayList<String>());
    }

    List<String> loadConfigValue(String path, List<String> defaultValue) {
        if (getConfig().get(path) == null) {
            getConfig().set(path, defaultValue);
            saveConfig();

            return defaultValue;
        } else {
            return (List<String>) getConfig().get(path);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig().set("playersCannotBuild", playersCannotBuild);
        getConfig().set("playersCannotPlace", playersCannotPlace);
        getConfig().set("playersCannotBreak", playersCannotBreak);
        saveConfig();

        getLogger().info("§cPlugin has been disabled.");
    }

    List<String> playersCannotBuild = new ArrayList<>();
    List<String> playersCannotPlace = new ArrayList<>();
    List<String> playersCannotBreak= new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (command.getName().equalsIgnoreCase("togglebuild")) {
            if (p.hasPermission("disablebuild.togglebuild")) {
                String UUID = p.getUniqueId().toString();

                if (playersCannotBuild.contains(UUID)) {
                    p.sendMessage("§aYou have enabled building for yourself.");
                    playersCannotBuild.remove(UUID);
                    playersCannotPlace.remove(UUID);
                    playersCannotBreak.remove(UUID);
                } else {
                    p.sendMessage("§aYou have disabled building for yourself.");
                    playersCannotBuild.add(UUID);
                }
            } else {
                p.sendMessage("§cYou are not allowed to do that.");
            }
        } else if (command.getName().equalsIgnoreCase("toggleplace") || p.hasPermission("disablebuild.togglebuild")) {
            if (p.hasPermission("disablebuild.toggleplace")) {
                String UUID = p.getUniqueId().toString();

                if (playersCannotPlace.contains(UUID)) {
                    p.sendMessage("§aYou have enabled placing for yourself.");
                    playersCannotPlace.remove(UUID);
                } else {
                    p.sendMessage("§aYou have disabled placing for yourself.");
                    playersCannotPlace.add(UUID);
                }
            } else {
                p.sendMessage("§cYou are not allowed to do that.");
            }
        } else if (command.getName().equalsIgnoreCase("togglebreak")) {
            if (p.hasPermission("disablebuild.togglebreak") || p.hasPermission("disablebuild.togglebuild")) {
                String UUID = p.getUniqueId().toString();

                if (playersCannotBreak.contains(UUID)) {
                    p.sendMessage("§aYou have enabled breaking for yourself.");
                    playersCannotBreak.remove(UUID);
                } else {
                    p.sendMessage("§aYou have disabled breaking for yourself.");
                    playersCannotBreak.add(UUID);
                }
            } else {
                p.sendMessage("§cYou are not allowed to do that.");
            }
        }
        return true;
    }

    @EventHandler
    void onBlockBreak(BlockBreakEvent e) {
        if (playersCannotBuild.contains(e.getPlayer().getUniqueId().toString()) || playersCannotBreak.contains(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent e) {
        if (playersCannotBuild.contains(e.getPlayer().getUniqueId().toString()) || playersCannotPlace.contains(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
        }
    }
}
