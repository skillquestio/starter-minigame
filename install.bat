@echo off
IF NOT EXIST OpenJDK (
    mkdir OpenJDK
)
cd ./OpenJDK
curl -z OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi -o OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi -L https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.3+7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi
OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7.msi
cd ..

curl -z apache-maven-3.8.5-bin.zip -o apache-maven-3.8.5-bin.zip https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.zip
tar -xf apache-maven-3.8.5-bin.zip
del apache-maven-3.8.5-bin.zip

pause