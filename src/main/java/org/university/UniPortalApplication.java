package org.university;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(
        info = @Info(
                title = "UniPortal API",
                version = "v1",
                description = "University course enrollment REST API"
        )
)
public class UniPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniPortalApplication.class, args);
    }
}

