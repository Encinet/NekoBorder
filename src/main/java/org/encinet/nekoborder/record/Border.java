package org.encinet.nekoborder.record;

/**
 * 边界信息
 * @param isRound 是否是园的 否则就为方
 * @param center 中心点
 * @param xDistance x轴长度
 * @param zDistance y轴长度
 */
public record Border(boolean isRound, PlaneLocation center, long xDistance, long zDistance) {
}
