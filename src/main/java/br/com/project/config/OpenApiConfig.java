package br.com.project.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Utilize um token JWT para acessar a API. O token pode ser obtido ao realizar o login."
)
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(
                new Info()
                        .title("PerCapita API RESTful")
                        .version("v1")
                        .description("API Restful funcional do aplicativo PerCapita")
                        .termsOfService("https://github.com/danpnx/projeto-integrador-dh")
                        .license(
                                new License()
                                        .name("Apache License 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                        .contact(
                                new Contact()
                                        .name("PerCapita")
                                        .email("suportepercapita@outlook.com")
                                        .url("https://github.com/danpnx/projeto-integrador-dh")
                                        .name("Anderson Chaves")
                                        .email("anderson.c-morais@hotmail.com")
                                        .url("https://github.com/chaavez")
                                        .url("https://www.linkedin.com/in/anderson-chaves-956436248/")
                                        .name("Daniel Augusto")
                                        .email("danielpn23@outlook.com")
                                        .url("https://github.com/danpnx")
                                        .url("https://www.linkedin.com/in/daniel-augusto-nunes/")
                                        .name("Sávia Christine")
                                        .email("saviachristine@gmail.com")
                                        .url("https://github.com/savicax")
                                        .url("https://www.linkedin.com/in/savicax/")
                        )
                        .summary(
                                "API RESTful criada para o Projeto Integrador da Digital House em parceria com a Empiricus."
                        )
        );
    }
}
