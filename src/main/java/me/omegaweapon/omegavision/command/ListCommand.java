package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class ListCommand extends PlayerCommand {
  
  public ListCommand() {
    super("nightvisionlist");
    
    // Set the permission and the permission message for the command
    setPermission("omegavision.list");
    setPermissionMessage(Utilities.colourise(OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("No_Permission")));
    
    // Set command usage and description
    setDescription("Get a list of all the current players who have nightvision enabled.");

    // Set the aliases for the command
    setAliases(Arrays.asList(
      "nvl",
      "nvlist"
    ));
  }
  
  @Override
  protected void onCommand(final Player player, final String[] strings) {
    Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " &bThe following players have nightvision enabled:");
    for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
      if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        Utilities.message(player,  OmegaVision.getMessagesFile().getConfig().getString("Prefix") +  " &c" + onlinePlayers.getName());
      }
    }
  }
}
