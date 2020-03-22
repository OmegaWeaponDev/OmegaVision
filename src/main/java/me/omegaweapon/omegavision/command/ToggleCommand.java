package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ToggleCommand extends PlayerCommand {
  public static Map<UUID, String> playerMap = new HashMap<>();
  public static Map<UUID, Long> nightvisionAppliedTime = new HashMap<>();
  
  public ToggleCommand() {
    super("nv");
  
    // Set the permission and permission message
    setPermission("omegavision.toggle");
    setPermissionMessage(Utilities.colourise(OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getConfigFile().getConfig().getString("No_Permission")));
  
    // Set the description message
    setDescription("The nightvision toggle command");
  }
  
  @Override
  protected void onCommand(Player player, String[] strings) {
    
    if(strings.length == 0) {
      Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " &bThe command is: &c/nv toggle");
      return;
    }
    
    // Makes sure they are using the toggle argument
    if(strings[0].equalsIgnoreCase("toggle")) {
      
      // Checks if the player does not have nightvision
      if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        
        // Add the nightvision effect to the player
        Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1,
          OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Ambient"),
          OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Effects"),
          OmegaVision.getConfigFile().getConfig().getBoolean("NightVision_Icon")
        );
        
        // Add the player to the maps
        playerMap.put(player.getUniqueId(), player.getName());
        nightvisionAppliedTime.put(player.getUniqueId(), System.currentTimeMillis());
        
        // Send the player the nightvision applied message
        Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("NightVision_Applied"));
        
        // Add the players nightvision status to playerData.yml if they have the login permission
        if(player.hasPermission("omegavision.login")) {
          // Check if they have been added to the file, if not, add them
          if(!OmegaVision.getPlayerData().getConfig().contains(player.getUniqueId().toString())) {
            OmegaVision.getPlayerData().getConfig().createSection(player.getUniqueId().toString());
          }
          OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision", true);
          try{
            OmegaVision.getPlayerData().saveConfig();
          } catch(Exception ex) {
            ex.printStackTrace();
          }
        }
        
        // If enabled, send the player an action bar when toggling nightvision
        if(OmegaVision.getConfigFile().getConfig().getBoolean("ActionBar_Messages")) {
          Utilities.sendActionBar(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("ActionBar_NightVision_Applied"));
        }
      } else if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
        // Remove the nightvision effect from the player
        Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
        
        // Remove the player from the playerMap
        playerMap.remove(player.getUniqueId());
  
        // Add the players nightvision status to playerData.yml if they have the login permission
        if(player.hasPermission("omegavision.login")) {
          // Check if they have been added to the file, if not, add them
          if(!OmegaVision.getPlayerData().getConfig().contains(player.getUniqueId().toString())) {
            OmegaVision.getPlayerData().getConfig().createSection(player.getUniqueId().toString());
          }
          OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision", false);
          try{
            OmegaVision.getPlayerData().saveConfig();
          } catch(Exception ex) {
            ex.printStackTrace();
          }
        }
        
        // Implement the blindness effect
        if(!player.hasPermission("omegavision.blindnessbypass")) {
          long timeRemoved = TimeUnit.MINUTES.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
          int configTimer = OmegaVision.getConfigFile().getConfig().getInt("Blindness_Effect.Timer");
          int configDuration = OmegaVision.getConfigFile().getConfig().getInt("Blindness_Effect.Duration");
          long timeApplied = TimeUnit.MINUTES.convert(nightvisionAppliedTime.get(player.getUniqueId()), TimeUnit.MILLISECONDS);
          
          if((timeRemoved - timeApplied) >= configTimer) {
            Utilities.addPotionEffect(player, PotionEffectType.BLINDNESS, configDuration, 1, true, true, true);
            nightvisionAppliedTime.remove(player.getUniqueId());
          }
        }
        // Send the player a message to say nightvision has been removed
        Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("NightVision_Removed"));
        
        // Update the playerData.yml to reflect the nightvision being removed.
        if(OmegaVision.getPlayerData().getConfig().contains(player.getUniqueId().toString())) {
          OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision", false);
          try{
            OmegaVision.getPlayerData().saveConfig();
          } catch(Exception ex) {
            ex.printStackTrace();
          }
          
          // Send an actionbar message to say nightvision has been removed if they are enabled in config
          Utilities.sendActionBar(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("ActionBar_NightVision_Removed"));
        }
      }
    }
    
    if(strings.length == 2) {
      
      if(strings[0].equalsIgnoreCase("toggle")) {
        Player target = Bukkit.getPlayer(strings[1]);
        // Make sure the target actually exists an the player has permissions to apply nightvision to others
        if(target != null && player.hasPermission("omegavision.toggle.others")) {
          // Check if the target does not already have nightvision applied
          if(!target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            // At nightvision to the target then send them a message
            Utilities.addPotionEffect(target, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1,
              OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Ambient"),
              OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Effects"),
              OmegaVision.getConfigFile().getConfig().getBoolean("NightVision_Icon")
            );
            Utilities.message(target, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("Nightvision_Applied"));
            
            // Add the targeted player to the playerMap
            playerMap.put(target.getUniqueId(), player.getName());
  
            // Add the targets nightvision status to playerData.yml if they have the login permission
            if(target.hasPermission("omegavision.login")) {
              // Check if they have been added to the file, if not, add them
              if(!OmegaVision.getPlayerData().getConfig().contains(target.getUniqueId().toString())) {
                OmegaVision.getPlayerData().getConfig().createSection(target.getUniqueId().toString());
              }
              // Change the nightvision status to true in the file.
              OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision", true);
            }
            try{
              OmegaVision.getPlayerData().saveConfig();
            } catch(Exception ex) {
              ex.printStackTrace();
            }
            
            // Send the player a confirmation message that the player has got nightvision
            Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("Nightvision_Applied"));
          }
          
          // Check if the target already has nightvision applied
          if(target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            // Remove nightvision from the target and send them a message
            Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);
            Utilities.message(target, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("Nightvision_Removed"));
            
            // Remove the target from the playerMap
            playerMap.remove(target.getUniqueId());
            
            // Add the targets nightvision status to playerData.yml if they have the login permission
            if(target.hasPermission("omegavision.login")) {
              // Check if they have been added to the file, if not, add them
              if(!OmegaVision.getPlayerData().getConfig().contains(target.getUniqueId().toString())) {
                OmegaVision.getPlayerData().getConfig().createSection(target.getUniqueId().toString());
              }
              // Change the nightvision status to false in the file.
              OmegaVision.getPlayerData().getConfig().set(target.getUniqueId().toString() + ".NightVision", false);
              try{
                OmegaVision.getPlayerData().saveConfig();
              } catch(Exception ex) {
                ex.printStackTrace();
              }
            }
          }
        }
      }
    }
  }
}

