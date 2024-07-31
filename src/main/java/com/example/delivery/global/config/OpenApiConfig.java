package com.example.delivery.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Swagger springdoc-ui 구성 파일 */
@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    Info info =
        new Info()
            .title("Delivery API Document")
            .version("v0.0.1")
            .description("배달서비스의 API 명세서입니다.");
    String jwtSchemeName = "jwtAuth";

    // SecuritySchemes 등록
    Components components =
        new Components()
            .addSecuritySchemes(
                jwtSchemeName,
                new SecurityScheme()
                    .name(jwtSchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("Bearer")
                    .bearerFormat("JWT"));

    return new OpenAPI().info(info).components(components);
  }
}