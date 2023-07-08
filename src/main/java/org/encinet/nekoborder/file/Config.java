package org.encinet.nekoborder.file;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.encinet.nekoborder.NekoBorder;
import org.encinet.nekoborder.record.Border;
import org.encinet.nekoborder.record.PlaneLocation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    private static FileConfiguration config() {
        return NekoBorder.INSTANCE.getConfig();
    }
    public static boolean debug = false;

    public static Map<World, Border> borders;

    /**
     * 配置文件加载
     */
    protected static void load() {
        debug = config().getBoolean("debug", false);
        NekoBorder.debug("Loading config");

        NekoBorder.debug("Loading settings");
        borders = new ConcurrentHashMap<>();
        List<Map<?, ?>> settings = config().getMapList("settings");
        int count = 0;
        for (final Map<?, ?> map : settings) {
            NekoBorder.debug("Loading " + ++count + " settings");

            if ((boolean) map.get("enable")) {
                NekoBorder.debug("Setting " + count + " enabled");
                World world = Bukkit.getWorld((String) map.get("world"));
                if (world != null) {
                    boolean isRound = (boolean) map.get("isRound");
                    String[] centerArray = ((String) map.get("center")).split(",", 2);
                    if (centerArray.length == 2) {
                        double centerX = Double.parseDouble(centerArray[0]);
                        double centerZ = Double.parseDouble(centerArray[1]);
                        PlaneLocation center = new PlaneLocation(centerX, centerZ);
                        borders.put(world,
                                new Border(isRound, center,
                                        Long.parseLong(map.get("xDistance").toString()), Long.parseLong(map.get("zDistance").toString())));
                        NekoBorder.debug("Setting " + count + " success");
                    } else NekoBorder.debug("Setting " + count + " center wrong");
                } else NekoBorder.debug("Setting " + count + " world not existing");
            } else NekoBorder.debug("Setting " + count + " disabled");
        }
    }
}
