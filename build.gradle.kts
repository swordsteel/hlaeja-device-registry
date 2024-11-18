plugins {
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.service)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.springframework.boot)
}

dependencies {
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.ltd.hlaeja.library.common.messages)
    implementation(hlaeja.org.springframework.springboot.actuator.starter)
    implementation(hlaeja.org.springframework.springboot.r2dbc.starter)
    implementation(hlaeja.org.springframework.springboot.webflux.starter)

    runtimeOnly(hlaeja.org.postgresql)
    runtimeOnly(hlaeja.org.postgresql.r2dbc)

    testImplementation(hlaeja.assertj.core)
    testImplementation(hlaeja.io.mockk)
    testImplementation(hlaeja.io.projectreactor.reactor.test)
    testImplementation(hlaeja.kotlin.test.junit5)
    testImplementation(hlaeja.kotlinx.coroutines.test)
    testImplementation(hlaeja.org.springframework.springboot.test.starter)

    testRuntimeOnly(hlaeja.org.junit.platform.launcher)
}

group = "ltd.hlaeja"
