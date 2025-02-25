plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "org.teamf"
version = "0.1.0"

repositories {
    mavenCentral()
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    // Apache OpenNLP for natural language processing
    implementation("org.apache.opennlp:opennlp-tools:2.3.0")

    // JUnit 5 for testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    // Mockito for mocking in tests
    testImplementation("org.mockito:mockito-core:5.3.1")
}

application {
    mainClass.set("org.teamf.socialapp.SocialApp")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}