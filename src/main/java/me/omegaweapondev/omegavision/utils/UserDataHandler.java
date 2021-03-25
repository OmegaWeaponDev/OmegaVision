package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.configs.ConfigCreator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDataHandler {
  private final OmegaVision plugin;

  private final ConfigCreator userData = new ConfigCreator("playerData.yml");
  private final Map<UUID, Boolean> playerMap = new HashMap<>();
  private final Map<UUID, Long> nightvisionAppliedTime = new HashMap<>();
  private final Map<UUID, Long> nightvisionLimitReached = new HashMap<>();

  public UserDataHandler(final OmegaVision plugin) {
    this.plugin = plugin;
  }

  public void createUserFile() {
    userData.createConfig();
  }

  public void saveUserFile() {
    userData.saveConfig();
  }

  public ConfigCreator getUserFile() {
    return userData;
  }

  public boolean hasNightNightVision(final UUID playerUUID) {
    if(getPlayerMap().isEmpty()) {
      return false;
    }

    return getPlayerMap().get(playerUUID);
  }

  public int getCurrentUsageAmount(final UUID playerUUID) {
    return getPlayerData().getInt("Users." + playerUUID.toString() + ".Limit");
  }

  public long getLastUsed(final UUID playerUUID) {
    return getNightvisionAppliedTime().get(playerUUID);
  }

  public void populateMapsOnEnable() {
    if(getPlayerData().getConfigurationSection("Users") == null) {
      return;
    }

    if(getPlayerData().getConfigurationSection("Users").getKeys(false).size() == 0) {
      return;
    }

    for(String playerUUID : getPlayerData().getConfigurationSection("Users").getKeys(false)) {
      Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));

      getPlayerMap().put(player.getUniqueId(), getPlayerData().getBoolean("Users." + playerUUID));
    }
  }

  public void clearMapsOnDisable() {
    getPlayerMap().clear();
    getNightvisionLimitReached().clear();
    getNightvisionAppliedTime().clear();
  }

  public FileConfiguration getPlayerData() {
     return getUserFile().getConfig();
  }

  public Map<UUID, Boolean> getPlayerMap() {
    return playerMap;
  }

  public Map<UUID, Long> getNightvisionAppliedTime() {
    return nightvisionAppliedTime;
  }

  public Map<UUID, Long> getNightvisionLimitReached() {
    return nightvisionLimitReached;
  }
}
