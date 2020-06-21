![Banner](https://i.imgur.com/lsCI3wT.png)

OmegaVision is a simple modern plugin that allows players to toggle their nightvision on or off with a simple command. You are also able to enable or disable all the features easily inside the config.yml. All the messages are also fully customizable.

For further information about this plugin, please see the [wiki](https://github.com/OmegaWeaponDev/OmegaVision/wiki)

***

## **Installation**

OmegaVision currently supports all the spigot versions from 1.13 upto 1.15.2

Installing OmegaVision is a very simple task. All you are required to do is download the latest version and drop the OmegaVision-{version number}.jar into your servers plugins folder then restart the server. 

You will then get all the config.yml and messages.yml that you are able to customize all the messages and settings to meet all your servers needs. 

***

## **Features**

Below you can find all the different features that i've implemented into the plugin. If you have any features that you're wanting added to this plugin. Please send me a feature request via the [issues](https://github.com/OmegaWeaponDev/OmegaVision/issues) page, as that allows me to keep track of all the suggestions in one location.

| Feature | Description |
| ------- | ----------- |
| Toggle Nightvision | Allows the user to toggle nightvision for themselves or other players. |
| Keep NightVision on Login | Allows the user to keep their nightvision when they login/logout. |
| Keep Nightvision on Death | Allows the user to keep their nightvision on death. |
| Enable/disable Potion Effects| Allows the user to enable or disable the icon, ambient particles and normal particles effects from when they activate nightvision. |
| List all players with nightvision | Generate a list of all the online players who currently have nightvision enabled |
|Blindness after Nightvision over use| Determine how long they can use nightvision before they get blinded (Blindness will take effect after nightvision is removed) |
| Limit command usage | Apply a configurable limit to how many times a player can use the nightvision command to enable nightvision

***
## **Commands & Permissions**

Below you will find a list of all the commands and all the permissions that OmegaVision has to offer. There will be a discription for each of the commands and permissions so you're able to tell what each of them do.

### Commands
Note: The `/nvlimit` and `/omegavision` commands can be used from the console. All the others require you to be in-game


| Command | Description | Permission | Aliases |
| ----------- | ----------- | ----------- | ----------- |
| `/omegavision`  | Gives you a simple list of the commands and what they do. | `omegavision.admin` | `/ov`, `/ovision` |
| `/nightvision toggle` | Allows you to toggle nightvision for yourself. | `omegavision.toggle` | `/nvision toggle`, `/nv toggle` |
| `/nightvision toggle <playername>` | Allows you to toggle nightvision for other players. | `omegavision.toggle.others` | "same as above"
| `/omegavision reload` | Allows the user to reload all the files. | `omegavision.reload` |
| `/nightvisionlist` | Get a list of all users how have nightvision | `omegavision.list` | `/nvlist` |
| `/nvlimit check` | Check how close you are to reaching the limit | `omegavision.limit.check ` | 
| `/nvlimit check <playername>` | Check how close another person is to reaching the limit | `omegavision.limit.checkothers` |
| `/nvlimit reset <playername>` | Reset a players nightvision limit | `omegavision.limit.reset` |

### Permissions
Note: If you have the permission `omegavision.*` you will get all the permissions below.

| Permissions | Description |
| ----------- | ----------- |
| `omegavision.toggle` | Gives the player permission to use the toggle and main command |
| `omegavision.toggle.others` | Give the player permission to toggle nightvision for other players |
| `omegavision.admin`| Lets the player use the main command for the plugin |
| `omegavision.login` | lets the user keep nightvision when they login/logout |
| `omegavision.death` | Lets the user keep nightivison when they die |
| `omegavision.update` | Lets the user receive plugin update messages |
| `omegavision.list` | Lets the user generate a list of all players who have nightvision |
| `omegavision.blindnessbypass`| Bypass the blindness effect after using nightvision for a set time |
| `omegavision.limit.bypass` | Allow users to bypass the limits set in the config.yml |
| `omegavision.limit.check` | Allows the user to check how close they are to reaching the limit |
| `omegavision.limit.checkothers` | Allows the user to check how close another person is to reaching the limit |
| `omegavision.limit.reset` | Allows the user to reset a players limit |

***

## **Configuation**

Sometimes when adding new features I am needing to add / change some settings in the config.yml & messages.yml. This means that you may need to re-generate the file. If you aren't wanting to do this you are more than welcome to check out the Default Config File on github and find the settings that have been added or changed and add them into your config.yml without having to restart the server.

Default Configuration file: [config.yml](https://github.com/OmegaWeaponDev/OmegaVision/wiki/Configuration)

Default Messages File: [messages.yml](https://github.com/OmegaWeaponDev/OmegaVision/wiki/Messages)

## **Support**

Everyone makes mistakes, there will be times when you may come accross a bug or an issue with the plugin that needs to be resolved. I currently have 3 amazing ways that you are able to recieve support for all of my plugins.

### SpigotMC Forums:

If you would like to recieve support via the SpigotMC Forums you are more than welcome to leave a message at: https://www.spigotmc.org/threads/omegavision.405434/ and wait for myself or some one else to help you out!

### GitHub: 

If you feel more comfortable leaving an issue on github to recieve help for your issue than you're more than welcome to check out the OmegaVision Repo on github and leave an Issue and i'll get to it at the earliest chance I get. https://github.com/OmegaWeaponDev/OmegaVision/issues

### Discord:

I have been busy working on an amazing discord server that you are more than welcome to join and recieve some direct support from myself or others in the community that also use my plugins. 

You can join this discord server here: https://discord.gg/9nZTPcp

***

## **Suggestions**

As a developer it can sometimes be challenging for me to always come up with new ideas and features that you amazing people would like to see in my plugins. So, I encourage you to step forward with any ideas that you may have regarding my plugins or any plugins that you think I should try and develop. 

The best place for you to share these ideas with me would be GitHub: https://github.com/OmegaWeaponDev/OmegaVision/issues. This will allow me to easily keep track of all the suggestions without them getting lost in a discord server or on the spigot forums.

***

## **Leave A Review**

If you have liked this plugin and found it to be helpful please leave a review so others will be able to find it and try it out for themselves as well. This will really help me out as it will make this plugin stand out amongst others and show people how much you like the plugin.