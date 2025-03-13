plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.29")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation(libs.junit)

    val javafxVersion = "21"

    implementation("org.openjfx:javafx-controls:$javafxVersion")
    implementation("org.openjfx:javafx-fxml:$javafxVersion")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "db_lab.App"
}
