![Banner](https://i.imgur.com/wUjbVQx.png)

OmegaVision is a simple modern plugin that allows players to toggle their nightvision on or off with a simple command. You are also able to enable or disable all the features easily inside the config.yml. All the messages are also fully customizable.

For further information about this plugin, please see the [wiki](https://github.com/OmegaWeaponDev/OmegaVision/wiki)

***

## **Installation**

OmegaVision currently supports all the spigot versions from 1.13 to 1.16.1

Installing OmegaVision is a very simple task. All you are required to do is to download the latest version and drop the OmegaVision-{version number}.jar into your servers plugins folder then restart the server. 

You will then get all the config.yml and messages.yml that you are able to customize all the messages and settings to meet all your servers needs. 

***

## **Features**

Below you can find all the different features that I've implemented into the plugin. If you have any features that you're wanting added to this plugin. Please send me a feature request via the [issues](https://github.com/OmegaWeaponDev/OmegaVision/issues) page, as that allows me to keep track of all the suggestions in one location.

| Feature | Description |
| ------- | ----------- |
| Toggle Nightvision | Allows the user to toggle nightvision for themselves or other players. |
| Keep NightVision on Login | Allows the user to keep their nightvision when they login/logout. |
| Keep Nightvision on Death | Allows the user to keep their nightvision on death. |
| Enable/disable Potion Effects| Allows the user to enable or disable the icon, ambient particles and normal particles effects from when they activate nightvision. |
| List all players with nightvision | Generate a list of all the online players who currently have nightvision enabled |
| Blindness after Nightvision over use| Determine how long they can use nightvision before they get blinded (Blindness will take effect after nightvision is removed) |
| Limit command usage | Apply a configurable limit to how many times a player can use the nightvision command to enable nightvision

***
## **Commands & Permissions**

Below you will find a list of all the commands and all the permissions that OmegaVision has to offer. There will be a description for each of the commands and permissions so you're able to tell what each of them do.

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
| `omegavision.login` | lets the user keep nightvision when they login/logout |
| `omegavision.death` | Lets the user keep nightivison when they die |
| `omegavision.update` | Lets the user receive plugin update messages |
| `omegavision.list` | Lets the user generate a list of all players who have nightvision |
| `omegavision.blindness.bypass`| Bypass the blindness effect after using nightvision for a set time |
| `omegavision.limit.bypass` | Allow users to bypass the limits set in the config.yml |
| `omegavision.limit.check` | Allows the user to check how close they are to reaching the limit |
| `omegavision.limit.checkothers` | Allows the user to check how close another person is to reaching the limit |
| `omegavision.limit.reset` | Allows the user to reset a players limit |
| `omegavsiion.limit.*` | Gives the user all the omegavision limit permissions

***

## **Configuration**

Sometimes when adding new features I'm to adding or changing some settings in the config.yml & messages.yml. This means that you may need to re-generate the file. If you aren't wanting to do this you are more than welcome to check out the Default Config File on github and find the settings that have been added or changed and add them into your config.yml without having to restart the server.

Default Configuration file: [config.yml](https://github.com/OmegaWeaponDev/OmegaVision/wiki/Configuration)

Default Messages File: [messages.yml](https://github.com/OmegaWeaponDev/OmegaVision/wiki/Messages)

## Installation, Downloading & Updating

### Downloading:

You can always find the latest version by visiting the [spigot](https://www.spigotmc.org/resources/omegavision.73013/) 
page. This plugin works on all Bukkit/Spigot/Paper servers from version 1.13 and above.

### Installing:

When it comes to installing the plugin, Simply drop the OmegaVision-[version number].jar into your servers plugin folder and then start your server.

### Updating: 

Updating is very easy, all you're needing to do is download the new version of the plugin and replace the old version in your plugins folder.

I have implemented an Auto Config Updater that will automatically update all the files if I have changed them in any way.

***

## **Support**

If at any stage you come across a sneaky bug that I was unable to locate during testing please do not hesitate to get in contact with me so I am able to squash it quickly for you. When you are finding yourself in need to contact me about any of my plugins, if it is relating to a bug, please try and provide as much detail as possible, including server version, plugin version and the steps taken to get the bug. This allows me to focus on the bug a lot quicker and allows me to get an update released sooner in order to resolve the bug.

### **Discord**
![IMG](https://i.imgur.com/yQIZDR6.png)

I have spent some time setting up a discord server that you are welcome to join at any time. Here you are able to seek help, have a chat, be informed of any updates or announcements and just be apart of a great community. You can join the community or invite your friends to the discord community by using this link: https://discord.gg/9nZTPcp

### **Spigot**

Of course, we have the normal contact method of using the SpigotMC website. You can locate my profile here: https://www.spigotmc.org/members/omegaweapondev.14953/ Feel free to privately message me about anything related to any of my plugins. 

### **GitHub**

As you can tell, I also have set everything here on git hub as well. So if you need any support feel free to post a issue for the related plugin

***

Lastly, I would like to say Thank you for supporting my plugins and supporting me as a developer. While I am still somewhat new to plugin development I try to provide the best support and the best plugins for you guys (and girls). Again, Thank you. 
