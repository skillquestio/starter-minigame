#!/bin/bash
sudo apt update
sudo apt-get install -y openjdk-17-jdk
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

curl -L -z apache-maven-3.8.6-bin.tar.gz -o apache-maven-3.8.6-bin.tar.gz https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
sudo tar xf apache-maven-3.8.6-bin.tar.gz -C /opt
sudo ln -s /opt/apache-maven-3.8.6 /opt/maven
sudo sh -c "echo 'export M2_HOME=/opt/maven\nexport MAVEN_HOME=/opt/maven\nexport PATH=\${M2_HOME}/bin:\${PATH}' > /etc/profile.d/maven.sh"
sudo chmod +x /etc/profile.d/maven.sh
rm apache-maven-3.8.6-bin.tar.gz

echo What\'s your Minecraft username?
read mcusername

mv ./src/main/java/Starter ./src/main/java/$mcusername
echo -e "name: $mcusername\nmain: $mcusername.MyPlugin\ndescription: Change this\nauthor: Change this\nversion: 1.0\napi-version: 1.16\nsoftdepend: [SidebarAPI, Multiverse-Core, MinigamesAPI]\n" > ./src/main/resources/plugin.yml



echo Done!