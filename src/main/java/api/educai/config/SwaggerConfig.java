package api.educai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        Server server = new Server();
        server.setUrl("localhost:8080/swagger-ui/index.html");
        return new OpenAPI().servers(List.of(server)).info(new Info().title("Educai API").version("1.0"));
    }
}
