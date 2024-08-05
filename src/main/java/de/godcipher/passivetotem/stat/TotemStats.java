package de.godcipher.passivetotem.stat;

import java.util.EnumMap;
import java.util.Map;

public class TotemStats {

  private final Map<StatType, Integer> stats = new EnumMap<>(StatType.class);

  public TotemStats() {
    for (StatType statType : StatType.values()) {
      stats.put(statType, 0);
    }
  }

  public void setStat(StatType statType, int value) {
    stats.put(statType, value);
  }

  public int getStat(StatType statType) {
    return stats.get(statType);
  }

  public Map<StatType, Integer> getStats() {
    return stats;
  }
}
