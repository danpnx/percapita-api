package br.com.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(
                new Info()
                        .title("Nome-do-aplicativo API RESTful")
                        .version("v1")
                        .description("API Restful funcional do aplicativo Nome-do-aplicativo")
                        .termsOfService("https://github.com/danpnx/projeto-integrador-dh")
                        .license(
                                new License()
                                        .name("Apache License 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                        .contact(
                                new Contact()
                                        .name("Nome-do-aplicativo")
                                        .email("anderson.c-10@hotmail.com")
                                        .url("https://github.com/danpnx/projeto-integrador-dh")
                        )
                        .summary(
                                "API RESTful criada para o Projeto Integrador da Digital House em parceria com a Empiricus."
                        )
        );
    }
}
