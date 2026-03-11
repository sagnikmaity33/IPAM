package sagnikverse.IPAM.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo(){

        return new OpenAPI()
                .info(new Info()
                        .title("IPAM API")
                        .description("IP Address Management System")
                        .version("1.0"));
    }

}