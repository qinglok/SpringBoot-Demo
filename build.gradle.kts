import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("plugin.jpa") version "1.2.71"
	id("org.springframework.boot") version "2.1.7.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	war
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
	kotlin("kapt") version "1.2.71"
}

group = "xyz.beingx"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

allprojects {
	repositories {
		maven("https://maven.aliyun.com/repository/central")
		maven("https://maven.aliyun.com/repository/jcenter")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	compileOnly(project(":auto-entity-keys"))
	annotationProcessor(project(":auto-entity-keys"))
	kapt(project(":auto-entity-keys"))

	//JWT
	implementation("com.auth0:java-jwt:3.8.1")

	//mariadb驱动
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client:2.4.3")

	//用Spring构建的API的自动JSON API文档
	implementation("io.springfox:springfox-swagger2:2.9.2")
	implementation("io.springfox:springfox-swagger-ui:2.9.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
