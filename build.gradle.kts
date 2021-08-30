plugins {
    kotlin("jvm") version "1.5.21"
    java
    `maven-publish`
}

group = "com.rsps.studios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    implementation("io.netty:netty-buffer:4.1.67.Final")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://legionkt.com:8085/repository/maven-snapshots/")
            credentials {
                username = project.properties["myNexusUsername"] as String
                password = project.properties["myNexusPassword"] as String
            }
        }
    }
}