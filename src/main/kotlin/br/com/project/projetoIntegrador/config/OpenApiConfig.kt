package br.com.project.projetoIntegrador.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "Utilize um token JWT para acessar a API. O token pode ser obtido ao realizar o login."
)
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("PerCapita API RESTful")
                .version("v2")
                .description("API RESTful do aplicativo PerCapita")
                .termsOfService("https://github.com/danpnx/projeto-integrador-dh")
                .license(
                    License()
                        .name("Apache License 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                )
                .contact(
                    Contact()
                        .name("PerCapita")
                        .email("suportepercapita@outlook.com")
                        .url("https://github.com/danpnx/projeto-integrador-dh")
                )
                .summary("API RESTful criada para o Projeto Integrador da Digital House em parceria com a Empiricus.")
        )
    }
}