plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.spring") version "2.1.21"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("tech.medivh.plugin.publisher") version "1.2.5"
}

group = "io.github.mynna404"
version = "0.0.1-test1"
description = "koaks-spring-boot-starter"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.springframework.boot:spring-boot-autoconfigure")
    api("io.github.mynna404:koaks:0.0.1-beta6")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

medivhPublisher {
    groupId = project.group.toString()
    artifactId = project.name
    version = project.version.toString()
    pom {
        name = "koaks-spring-boot-starter"
        description =
            "koaks-spring-boot-starter"
        url = "https://github.com/koaks-ai/koaks-spring-boot-starter"
        licenses {
            license {
                name = "Apache-2.0 license"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "mynna404"
                name = "mynna404"
                email = "gemingjia0201@163.com"
            }
        }
        scm {
            connection = "scm:git:"
            url = "https://github.com/koaks-ai/koaks-spring-boot-starter"
        }
    }
}
