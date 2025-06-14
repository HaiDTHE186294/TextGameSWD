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
    mainClass.set("item.example.InventoryUIExample")
}

dependencies {
    // JavaFX theo OS – ví dụ dùng Windows
    implementation("org.openjfx:javafx-base:22:win")
    implementation("org.openjfx:javafx-controls:22:win")
    implementation("org.openjfx:javafx-graphics:22:win")
    implementation("org.openjfx:javafx-fxml:22:win")

    // FXGL phiên bản ổn định 21.1
    implementation("com.github.almasb:fxgl:21.1")

    // Jackson JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.2")
}

tasks.test {
    useJUnitPlatform()
}
