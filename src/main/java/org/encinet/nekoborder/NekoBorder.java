package org.encinet.nekoborder;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.encinet.nekoborder.event.PlayerMoveEvent;
import org.encinet.nekoborder.file.Config;
import org.encinet.nekoborder.file.FileManager;

import java.util.Objects;
import java.util.logging.Logger;

public final class NekoBorder extends JavaPlugin {
    public static Plugin INSTANCE;
    public static Logger logger;

    @Override
    public void onLoad() {
        INSTANCE = this;
        logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Loading files
        // 这时候世界才加载完毕
        FileManager.load();

        // Register Event
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerMoveEvent(), this);

        // Register Command
        if (Bukkit.getPluginCommand("nekoborder") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("nekoborder")).setExecutor(new Command());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // 禁用插件时注销所有事件
        HandlerList.unregisterAll(this);
    }

    public static void debug(String text) {
        if (Config.debug) {
            logger.info("[DEBUG] " + text);
        }
    }
}
