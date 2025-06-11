plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "22"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("org.example.HelloFX")
}

tasks.test {
    useJUnitPlatform()
}