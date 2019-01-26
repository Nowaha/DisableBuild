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
        getLogger().info("§aPlugin has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("§cPlugin has been disabled.");
    }

    List<Player> playersCannotBuild = new ArrayList<>();
    List<Player> playersCannotPlace = new ArrayList<>();
    List<Player> playersCannotBreak= new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (command.getName().equalsIgnoreCase("togglebuild")) {
            if (p.hasPermission("disablebuild.togglebuild")) {
                if (playersCannotBuild.contains(p)) {
                    p.sendMessage("§aYou have enabled building for yourself.");
                    playersCannotBuild.remove(p);
                } else {
                    p.sendMessage("§aYou have disabled building for yourself.");
                    playersCannotBuild.add(p);
                }
            }
        } else if (command.getName().equalsIgnoreCase("toggleplace")) {
            if (p.hasPermission("disablebuild.toggleplace")) {
                if (playersCannotPlace.contains(p)) {
                    p.sendMessage("§aYou have enabled placing for yourself.");
                    playersCannotPlace.remove(p);
                } else {
                    p.sendMessage("§aYou have disabled placing for yourself.");
                    playersCannotPlace.add(p);
                }
            }
        } else if (command.getName().equalsIgnoreCase("togglebreak")) {
            if (p.hasPermission("disablebuild.togglebreak")) {
                if (playersCannotBreak.contains(p)) {
                    p.sendMessage("§aYou have enabled breaking for yourself.");
                    playersCannotBreak.remove(p);
                } else {
                    p.sendMessage("§aYou have disabled breaking for yourself.");
                    playersCannotBreak.add(p);
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @EventHandler
    void onBlockBreak(BlockBreakEvent e) {
        if (playersCannotBuild.contains(e.getPlayer()) || playersCannotBreak.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent e) {
        if (playersCannotBuild.contains(e.getPlayer()) || playersCannotPlace.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
