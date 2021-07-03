plugins {
  id("otel.library-instrumentation")
}

dependencies {
  compileOnly("org.springframework:spring-webflux:5.0.0.RELEASE")
  compileOnly("io.projectreactor.ipc:reactor-netty:0.7.0.RELEASE")
}
