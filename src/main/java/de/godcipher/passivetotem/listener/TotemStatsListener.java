package de.godcipher.passivetotem.listener;

import de.godcipher.passivetotem.stat.StatType;
import de.godcipher.passivetotem.stat.TotemStats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class TotemStatsListener implements Listener {

  private final TotemStats totemStats;

  public TotemStatsListener(TotemStats totemStats) {
    this.totemStats = totemStats;
  }

  @EventHandler
  public void onDamageReceiving(EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player)) return;

    Player player = (Player) event.getEntity();

    if (!isTotemEquipped(player)) return;

    double damageReductionPercentage = totemStats.getStat(StatType.DAMAGE_REDUCTION);
    if (damageReductionPercentage > 0) {
      double reducedDamage = event.getDamage() * (1 - damageReductionPercentage / 100);
      event.setDamage(reducedDamage);
    }
  }

  @EventHandler
  public void onDamageDealing(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player)) return;

    Player player = (Player) event.getDamager();

    if (!isTotemEquipped(player)) return;

    double damageIncreasePercentage = totemStats.getStat(StatType.DAMAGE);
    if (damageIncreasePercentage > 0) {
      double increasedDamage = event.getDamage() * (1 + damageIncreasePercentage / 100);
      event.setDamage(increasedDamage);
    }
  }

  private boolean isTotemEquipped(Player player) {
    return player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
        || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;
  }
}
