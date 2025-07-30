package ru.mts.media.platform.umc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .properties("spring.config.name:application,postgres,graphql")
                .build()
                .run(args);
    }
}
