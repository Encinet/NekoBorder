package org.encinet.nekoborder.util;

import org.bukkit.Location;
import org.encinet.nekoborder.record.Border;
import org.encinet.nekoborder.record.PlaneLocation;

public class BorderMath {
    /**
     * 坐标是否在边界之外
     * @param border 边界
     * @param location 平面坐标
     * @return 坐标是否在边界之外
     */
    public static boolean isOutOfBorder(Border border, Location location) {
        PlaneLocation center = border.center();
        double xDistance = border.xDistance();
        double zDistance = border.zDistance();
        double centerX = center.x();
        double centerZ = center.z();
        double x = location.x();
        double z = location.z();
        if (border.isRound()) {
            double radiusX = xDistance / 2;
            double radiusZ = zDistance / 2;

            // 计算玩家与边界中心点的距离
            double distanceX = x - centerX;
            double distanceZ = z - centerZ;

            // 将距离的平方归一化为边界半径的平方
            double normalizedDistanceX = distanceX * distanceX / (radiusX * radiusX);
            double normalizedDistanceZ = distanceZ * distanceZ / (radiusZ * radiusZ);

            // 检查归一化的距离是否大于1 如果大于1则表示玩家在边界之外
            return normalizedDistanceX + normalizedDistanceZ > 1;
        } else {
            double minX = centerX - xDistance / 2;
            double maxX = centerX + xDistance / 2;
            double minZ = centerZ - zDistance / 2;
            double maxZ = centerZ + zDistance / 2;
            return x < minX || x > maxX || z < minZ || z > maxZ;
        }
    }

    /**
     * 计算玩家坐标连接边界的中点的线
     * 返回这个线上的距离一个边界五个格子且在中心点一侧的点
     * 也就是说: 以玩家和中心点的方向上距离一个边界五个格子且在中心点一侧的点
     * @param border 边界
     * @param location 平面坐标
     * @return 以玩家和中心点的方向上距离一个边界五个格子且在中心点一侧的点
     */
    public static PlaneLocation getPointInsideBorder(Border border, Location location) {
        PlaneLocation center = border.center();
        double xDistance = border.xDistance();// x轴长度(直径)
        double zDistance = border.zDistance();// z轴长度(直径)
        double centerX = center.x();// 中心点x坐标
        double centerZ = center.z();// 中心点z坐标
        double x = location.x();// 玩家x坐标
        double z = location.z();// 玩家z坐标

        double newX, newZ;
        if (border.isRound()) {
            // 圆形或椭圆
            double angle = Math.atan2(z - centerZ, x - centerX); // 计算玩家和中心点的角度
            newX = centerX + (xDistance / 2 - 5) * Math.cos(angle); // 沿着这个角度移动到距离边界五个格子的位置
            newZ = centerZ + (zDistance / 2 - 5) * Math.sin(angle);
        } else {
            // 矩形
            if (Math.abs(x - centerX) > Math.abs(z - centerZ)) {
                // 玩家在中心点的左边或右边
                newX = x > centerX ? centerX + xDistance / 2 - 5 : centerX - xDistance / 2 + 5;
                newZ = z;
            } else {
                // 玩家在中心点的上方或下方
                newX = x;
                newZ = z > centerZ ? centerZ + zDistance / 2 - 5 : centerZ - zDistance / 2 + 5;
            }
        }
        // 创建新的点并返回
        return new PlaneLocation(newX, newZ);
    }
}
