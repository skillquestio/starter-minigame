# Minecraft Minigame Starter Pack

This repository contains a starting point for your minigame. It can load as-is into the server, however there isn't any game logic inside of it.

## Setting up your project

### Setting up your server

1. Go through the [`Create a Minecraft Server`](https://skillquest.io/quest/create-a-minecraft-server-1652794608908x505919601352900600) quest (MC Version 1.19)
2. Copy [`Skillquest.jar`](https://github.com/skillquestio/starter-minigame/blob/main/lib/Skillquest.jar?raw=true) into your server's `plugins` directory
3. Start your server
4. Once the server is fully booted, stop the server and copy [`MinigamesAPI.jar`](https://github.com/skillquestio/starter-minigame/blob/main/lib/MinigamesAPI.jar?raw=true) into your server's `plugins/Skillquest` directory
5. You can now start your server again, and in the server console, you should see that `MinigamesAPI` loads when `Skillquest` loads.

### Installing the dependencies

Ubuntu:

```bash
chmod +x ./install.sh
./install.sh
source /etc/profile.d/maven.sh
```

macOS:

```bash
chmod +x ./install.bash
./install.bash
```

Windows:

```bash
.\install.bat
```

### Setting up the Maven project

1. Open up `pom.xml`
2. On line 4, change `groupId` to your Minecraft username
3. On line 5, change `artifactId` to the name of your minigame [use dashes (or blanks) for spaces]
4. On line 93, change the `outputDirectory` to your server's `plugins/Skillquest/minigames` directory
5. On line 95, change `finalName` to the same value as `artifactId` on line 5
6. Wait until the install script is finished, then right-click on `pom.xml` and click `Update Project`
7. Restart VS Code
8. Inside of all the classes in `src/main/java/[your_mc_username]`, you will need to change the first line in every file from `package Starter;` to `package your_mc_username;`
