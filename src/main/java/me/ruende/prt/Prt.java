package me.ruende.prt;

import me.ruende.prt.command.RandomTeleportCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Prt extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getCommandMap().register("", new RandomTeleportCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
