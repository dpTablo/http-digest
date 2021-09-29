plugins {
    `war`
    `java`
}

group "com.potalab"
version "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")

    testImplementation("junit:junit:4.11")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}