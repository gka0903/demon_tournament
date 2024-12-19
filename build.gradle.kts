plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 테스트 의존성
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    // Java 소스 호환성 설정 (필요한 버전에 맞게 수정 가능)
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Java 17 사용 예시
    }
}

tasks.compileJava {
    // 소스 경로 명시
    sourceCompatibility = "17"
    targetCompatibility = "17"
    options.encoding = "UTF-8" // UTF-8 인코딩 설정
}
