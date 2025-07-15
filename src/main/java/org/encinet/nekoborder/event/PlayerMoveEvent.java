package org.encinet.nekoborder.event;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import org.encinet.nekoborder.file.Config;
import org.encinet.nekoborder.file.Message;
import org.encinet.nekoborder.record.Border;
import org.encinet.nekoborder.record.PlaneLocation;
import org.encinet.nekoborder.util.BorderMath;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PlayerMoveEvent implements Listener {
    @EventHandler
    public void onPlayerMoveEvent(org.bukkit.event.player.PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return; // 玩家只是转头或在同一格内小幅移动，不处理
        }
        Location location = event.getTo();
        if (Config.borders.containsKey(location.getWorld())) {
            Border border = Config.borders.get(location.getWorld());
            if (BorderMath.isOutOfBorder(border, location)) {
                // 走出边界
                Player player = event.getPlayer();
                Entity vehicle = player.getVehicle();// 玩家乘坐的实体

                PlaneLocation backPlane = BorderMath.getPointInsideBorder(border, location);
                float targetY = player.getWorld().getHighestBlockYAt((int) Math.floor(backPlane.x()), (int) Math.floor(backPlane.z()));

                double distance = Math.sqrt(Math.pow(location.x() - backPlane.x(), 2) + Math.pow(location.z() - backPlane.z(), 2));
                Location back = new Location(location.getWorld(), backPlane.x(), targetY, backPlane.z());

                if (distance >= 60) {
                    Objects.requireNonNullElse(vehicle, player).teleport(back);
                } else {
                    // 移动 矢量计算
                    Vector vector = new Vector(backPlane.x() - location.x(), 0, backPlane.z() - location.z());
                    vector.normalize().multiply(1.2);
                    Objects.requireNonNullElse(vehicle, player).setVelocity(vector);
                }
                // 播放粒子效果
                player.getWorld().spawnParticle(Particle.GLOW_SQUID_INK,
                        location.x(), location.y(), location.z(),
                        150, 10, 10, 10);
                // 发送信息
                player.sendTitlePart(TitlePart.TITLE, getMessage(Message.title));
                player.sendTitlePart(TitlePart.SUBTITLE, getMessage(Message.subtitle));
            }
        }
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Location location = event.getTo();
        if (Config.borders.containsKey(location.getWorld())) {
            Border border = Config.borders.get(location.getWorld());
            if (BorderMath.isOutOfBorder(border, location)) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                // 发送信息
                player.sendTitlePart(TitlePart.TITLE, getMessage(Message.title));
                player.sendTitlePart(TitlePart.SUBTITLE, getMessage(Message.subtitle));
            }
        }
    }

    public static Component getMessage(List<String> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        String input = list.get(index);
        return MiniMessage.miniMessage().deserialize(input);
    }
}
