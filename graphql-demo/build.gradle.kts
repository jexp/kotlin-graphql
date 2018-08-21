import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.Coroutines

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.41"


    repositories {
        mavenCentral()
        maven (url="https://kotlin.bintray.com/kotlinx" )
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.5.0")
    }
}

group = "graphql-kotlin"
version = "1.0-SNAPSHOT"

plugins {
    application
    java
    kotlin("jvm") version "1.2.41"
}
/*
apply {
    plugin("kotlinx-serialization")
}
*/
val kotlin_version: String by extra

var ktor_version: String by extra
ktor_version = "0.9.2"

repositories {
    mavenCentral()
    jcenter()
    maven (url="https://kotlin.bintray.com/kotlinx" )
    maven(url="https://dl.bintray.com/kotlin/ktor")
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlin_version))

    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("ch.qos.logback:logback-classic:1.2.1")
    compile("io.ktor:ktor-html-builder:$ktor_version")
    compile("io.ktor:ktor-jackson:$ktor_version")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.2")
    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.5.0")
    compile( "com.graphql-java:graphql-java:9.0")
    testCompile("junit:junit:4.12")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin { // configure<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension>
    experimental.coroutines = Coroutines.ENABLE
}
