package org.encinet.nekoborder.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.encinet.nekoborder.NekoBorder;

import java.io.File;
import java.util.List;

public class Message {
    private static final FileConfiguration message = YamlConfiguration.loadConfiguration(
            new File(NekoBorder.INSTANCE.getDataFolder(), "message.yml"));

    public static List<String> title;
    public static List<String> subtitle;

    /**
     * 消息文件加载
     */
    protected static void load() {
        title = message.getStringList("title");
        subtitle = message.getStringList("subtitle");
    }
}
