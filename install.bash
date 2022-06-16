#!/bin/bash
sudo brew install openjdk@17
sudo brew install maven
mkdir -p BuildTools
rm ./BuildTools/spigot-*.jar
cd BuildTools
curl -z BuildTools.jar -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
java -jar BuildTools.jar --remapped --generate-source --rev 1.19
cd ..
cp ./BuildTools/Spigot/Spigot-Server/target/spigot-*-remapped.jar ./lib/spigot-remapped.jar
cp ./BuildTools/Spigot/Spigot-Server/target/spigot-*-remapped-mojang.jar ./lib/spigot-remapped.jar
cp ./BuildTools/Spigot/Spigot-API/target/spigot-api-*-shaded.jar ./lib/spigot-api.jar
cp ./BuildTools/Spigot/Spigot-API/target/spigot-api-*-sources.jar ./lib/spigot-api-src.jar

echo What\'s your Minecraft username?
read mcusername

mv ./src/main/java/Starter ./src/main/java/$mcusername
echo -e "name: $mcusername\nmain: $mcusername.MyPlugin\ndescription: Change this\nauthor: Change this\nversion: 1.0\napi-version: 1.16\nsoftdepend: [SidebarAPI, Multiverse-Core, MinigamesAPI]\n" > ./src/main/resources/plugin.yml

echo Done!