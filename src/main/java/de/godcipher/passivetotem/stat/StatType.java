package de.godcipher.passivetotem.stat;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum StatType {
  DAMAGE_REDUCTION("damage-reduction", "Damage Reduction"),
  DAMAGE("damage", "Damage"),
  MAX_HEALTH("max-health", "Max. HP"),
  SPEED("speed", "Speed");

  public static StatType fromId(String id) {
    for (StatType type : StatType.values()) {
      if (type.getId().equalsIgnoreCase(id)) {
        return type;
      }
    }
    return null;
  }

  private final String id;
  private final String name;
  @Setter private String displayName;

  StatType(String id, String name) {
    this.id = id;
    this.name = name;
    this.displayName = name;
  }

  public void resetDisplayName() {
    this.displayName = this.name;
  }
}
