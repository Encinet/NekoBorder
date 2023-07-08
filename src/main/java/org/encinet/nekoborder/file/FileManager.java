package org.encinet.nekoborder.file;

import org.encinet.nekoborder.NekoBorder;

/**
 * 插件文件控制器
 */
public class FileManager {
    public static void load() {
        NekoBorder.INSTANCE.saveDefaultConfig();
        NekoBorder.INSTANCE.saveResource("message.yml", false);// false为不覆盖 true为每次调用都覆盖
        NekoBorder.INSTANCE.reloadConfig();

        Config.load();
        Message.load();
    }
}
