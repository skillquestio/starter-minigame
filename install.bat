@echo off
IF NOT EXIST OpenJDK (
    mkdir OpenJDK
)
cd ./OpenJDK
curl -z OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi -o OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi -L https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.3+7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi
OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi
cd ..
IF NOT EXIST BuildTools (
    mkdir BuildTools
)
cd BuildTools
curl -z BuildTools.jar -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
java -jar BuildTools.jar --remapped --generate-source --rev 1.19

cd ..

copy /B .\BuildTools\Spigot\Spigot-Server\target\spigot-*-remapped.jar .\lib\spigot-remapped.jar
copy /B .\BuildTools\Spigot\Spigot-Server\target\spigot-*-remapped-mojang.jar .\lib\spigot-remapped.jar
copy /B .\BuildTools\Spigot\Spigot-API\target\spigot-api-*-shaded.jar .\lib\spigot-api.jar
copy /B .\BuildTools\Spigot\Spigot-API\target\spigot-api-*-sources.jar .\lib\spigot-api-src.jar

curl -L -z apache-maven-3.8.6-bin.zip -o apache-maven-3.8.6-bin.zip https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.zip
tar -xf apache-maven-3.8.6-bin.zip
del apache-maven-3.8.6-bin.zip

set /p Input=What's your Minecraft username? || set Input=Unknown

move /Y ./src/main/java/Starter ./src/main/java/%Input%
(
echo name: %Input%
echo main: %Input%.MyPlugin
echo description: Change this
echo author: Change this
echo version: 1.0
echo api-version: 1.16
echo softdepend: [SidebarAPI, Multiverse-Core, MinigamesAPI] 
) > ./src/main/resources/plugin.yml

pause