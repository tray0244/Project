package com.delivery.userservice.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openApI() {
        // JWT 토큰 설정
    String jwt = "jwt";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
    Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
            .name(jwt)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
    );

    // Swagger UI 정보 설정
    return new OpenAPI()
            .components(components)
            .addSecurityItem(securityRequirement)
            .info(new Info()
                    .title("User Service API")
                    .description("배달 플랫폼 유저 서비스 API 명세서")
                    .version("v1.0.0"));
    }
}
