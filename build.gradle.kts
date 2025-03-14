plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}
group = "edu.bsu.cs"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21) // Use 11, 17, or 21
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // Hibernate dependencies
    implementation("com.h2database:h2:2.2.224")

    // JavaFX dependencies
    implementation("org.openjfx:javafx-controls:22")
    implementation("org.openjfx:javafx-fxml:22")

    // Hibernate dependencies
    implementation("org.hibernate:hibernate-core:6.4.1.Final")
    implementation("org.hibernate:hibernate-c3p0:6.4.1.Final") // Connection pooling

    // PostgreSQL JDBC Driver
    implementation("org.postgresql:postgresql:42.7.1")

    // Apache OpenNLP for natural language processing
    implementation("org.apache.opennlp:opennlp-tools:2.3.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.11")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Password hashing library
    implementation("org.mindrot:jbcrypt:0.4")

    // JSON Processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")

    // Testing Dependencies
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.3.1")
}

javafx {
    version = "22"
    modules("javafx.controls", "javafx.fxml", "javafx.graphics")
}

application {
    mainClass.set("edu.bsu.cs.SocialApp")
}

tasks.test {
    useJUnitPlatform()
}