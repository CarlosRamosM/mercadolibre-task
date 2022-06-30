package mx.com.ml.rebell.alliance.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  private static final Set<String> CONSUMES_PRODUCES = Stream.of(MediaType.APPLICATION_JSON_VALUE).collect(Collectors.toSet());

  @Bean
  public Docket apiDocket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .consumes(CONSUMES_PRODUCES)
        .produces(CONSUMES_PRODUCES)
        .select()
        .apis(RequestHandlerSelectors.basePackage("mx.com.ml.rebell.alliance.message"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(getApiInfo())
        .tags(
            new Tag("alliance-message", "Servicios para el envío de mensajes aliados.", 1)
        );
  }

  private ApiInfo getApiInfo() {
    return new ApiInfo(
        "Fuego de Quasar - API",
        "REST API - ML Challenge Quasar.",
        "v1",
        "Terms of service url",
        new Contact(
            "Carlos Ramos Montoya",
            "https://www.linkedin.com/in/carlosramosmontoya/",
            "ramos.montoya.carlos@gmail.com"),
        "License",
        "https://www.mercadolibre.com/",
        Collections.emptyList());
  }
}
