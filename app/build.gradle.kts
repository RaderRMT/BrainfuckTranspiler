plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

application {
    // Define the main class for the application.
    mainClass.set("fr.rader.transpiler.Main")
}
