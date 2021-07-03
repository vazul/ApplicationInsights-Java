plugins {
  id("otel.java-conventions")
  id("otel.publish-conventions")
}

group = "io.opentelemetry.javaagent"

val instrumentationMuzzle by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  extendsFrom(configurations.implementation.get())
}

dependencies {
  // Only used during compilation by bytebuddy plugin
  compileOnly("com.google.guava:guava")

  implementation(project(":javaagent-bootstrap"))
  implementation(project(":javaagent-extension-api"))
  implementation(project(":javaagent-api"))
  implementation(project(":instrumentation-api"))

  implementation("io.opentelemetry:opentelemetry-api")
  implementation("io.opentelemetry:opentelemetry-api-metrics")
  implementation("io.opentelemetry:opentelemetry-sdk")
  implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
  implementation("io.opentelemetry:opentelemetry-sdk-metrics")
  implementation("io.opentelemetry:opentelemetry-extension-kotlin")
  implementation("io.opentelemetry:opentelemetry-extension-aws")
  implementation("io.opentelemetry:opentelemetry-extension-trace-propagators")
  implementation("io.opentelemetry:opentelemetry-sdk-extension-resources")

  // Only the logging exporter is included in our slim distribution so we include it here.
  // Other exporters are in javaagent-exporters
  implementation("io.opentelemetry:opentelemetry-exporter-logging")

  api("net.bytebuddy:byte-buddy")
  implementation("org.slf4j:slf4j-api")

  annotationProcessor("com.google.auto.service:auto-service")
  compileOnly("com.google.auto.service:auto-service")

  testImplementation(project(":testing-common"))
  testImplementation("com.google.guava:guava")
  testImplementation("org.assertj:assertj-core")
  testImplementation("org.mockito:mockito-core")
  testImplementation("org.mockito:mockito-junit-jupiter")

  instrumentationMuzzle(sourceSets.main.get().output)
}

// Here we only include autoconfigure but don"t include OTLP exporters to ensure they are only in
// the full distribution. We need to override the default exporter setting of OTLP as a result.
tasks {
  withType<Test>().configureEach {
    environment("OTEL_TRACES_EXPORTER", "none")
    environment("OTEL_METRICS_EXPORTER", "none")
  }
}
