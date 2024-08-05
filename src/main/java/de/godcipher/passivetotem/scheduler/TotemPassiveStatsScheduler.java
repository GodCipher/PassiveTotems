package de.godcipher.passivetotem.scheduler;

import de.godcipher.passivetotem.stat.StatType;
import de.godcipher.passivetotem.stat.TotemStats;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TotemPassiveStatsScheduler implements Runnable {

  private static final double PLAYER_DEFAULT_SPEED = 0.2;
  private static final double PLAYER_DEFAULT_HEALTH = 20.0;

  private final TotemStats totemStats;

  public TotemPassiveStatsScheduler(TotemStats totemStats) {
    this.totemStats = totemStats;
  }

  @Override
  public void run() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      applyStats(player);
      updateTotemLore(player);
    }
  }

  private boolean isTotemEquipped(Player player) {
    return player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
        || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;
  }

  private void applyStats(Player player) {
    if (isTotemEquipped(player)) {
      player.setWalkSpeed(
          (float)
              (PLAYER_DEFAULT_SPEED
                  + PLAYER_DEFAULT_SPEED * (double) totemStats.getStat(StatType.SPEED) / 100));
      player
          .getAttribute(Attribute.GENERIC_MAX_HEALTH)
          .setBaseValue(
              PLAYER_DEFAULT_HEALTH
                  + PLAYER_DEFAULT_HEALTH * (double) totemStats.getStat(StatType.MAX_HEALTH) / 100);
    } else {
      player.setWalkSpeed((float) PLAYER_DEFAULT_SPEED);
      player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PLAYER_DEFAULT_HEALTH);
    }
  }

  private void updateTotemLore(Player player) {
    ItemStack totem = getTotem(player);
    if (totem == null) return;

    ItemMeta meta = totem.getItemMeta();
    List<String> lore = new ArrayList<>();
    if (!hasStats()) {
      meta.setLore(lore);
      totem.setItemMeta(meta);
      return;
    }

    lore.add("§7§oTotem Stats:");

    for (StatType statType : StatType.values()) {
      if (totemStats.getStat(statType) > 0)
        lore.add("§5+" + totemStats.getStat(statType) + "% " + statType.getDisplayName());
    }

    meta.setLore(lore);
    totem.setItemMeta(meta);
  }

  private boolean hasStats() {
    return totemStats.getStats().values().stream().anyMatch(value -> value > 0);
  }

  private ItemStack getTotem(Player player) {
    if (!isTotemEquipped(player)) {
      return null;
    }

    return player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
        ? player.getInventory().getItemInMainHand()
        : player.getInventory().getItemInOffHand();
  }
}
