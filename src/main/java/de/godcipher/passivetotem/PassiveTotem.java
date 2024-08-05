package de.godcipher.passivetotem;

import de.godcipher.passivetotem.scheduler.TotemPassiveStatsScheduler;
import de.godcipher.passivetotem.stat.StatType;
import de.godcipher.passivetotem.stat.TotemStats;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PassiveTotem extends JavaPlugin {

  private final TotemStats totemStats = new TotemStats();

  @Override
  public void onEnable() {
    loadConfig();
    generateConfig();
    updateTotemStats();
    runScheduler();
  }

  @Override
  public void onDisable() {}

  private void runScheduler() {
    getServer()
        .getScheduler()
        .runTaskTimer(this, new TotemPassiveStatsScheduler(totemStats), 0, 20);
  }

  private void loadConfig() {
    getConfig().options().copyDefaults(true);
    saveDefaultConfig();
  }

  private void generateConfig() {
    File configFile = new File(getDataFolder(), "config.yml");
    FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(configFile);

    try (PrintWriter writer = new PrintWriter(configFile)) {
      writer.println("# ALL CONFIGURATION VALUES ARE PERCENTAGES");
      writeDisplayNames(writer, existingConfig);
      writeSetBonusValues(writer, existingConfig);
    } catch (IOException e) {
      getLogger().severe("Failed to generate config file");
    }
  }

  private void writeDisplayNames(PrintWriter writer, FileConfiguration existingConfig) {
    writer.println("# Display names");
    for (StatType statType : StatType.values()) {
      String key = statType.getId() + "-display-name";
      String displayName =
          existingConfig.contains(key) ? existingConfig.getString(key) : statType.getDisplayName();
      writer.println(key + ": " + displayName);
    }
    writer.println();
  }

  private void writeSetBonusValues(PrintWriter writer, FileConfiguration existingConfig) {
    writer.println("# Set bonus values");
    for (StatType statType : StatType.values()) {
      String key = statType.getId() + "-value";
      int value = existingConfig.contains(key) ? existingConfig.getInt(key) : 0;
      writer.println(key + ": " + value);
    }
  }

  private void updateTotemStats() {
    for (StatType statType : StatType.values()) {
      String key = statType.getId() + "-value";
      int value = getConfig().getInt(key);
      totemStats.setStat(statType, value);
    }
  }
}
