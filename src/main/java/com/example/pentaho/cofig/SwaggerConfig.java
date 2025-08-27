package com.example.pentaho.cofig;

import com.example.pentaho.utils.StringUtils;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

//    @Autowired
//    private BuildProperties buildProperties;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Bean
    public OpenAPI springShopOpenAPI(Environment env) throws UnknownHostException {
//        LocalDateTime buildTimeInTaipei = buildProperties.getTime().atOffset(ZoneOffset.UTC).atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime();
//        String formattedTimeInTaipei = buildTimeInTaipei.format(formatter);

        OpenAPI openAPI = new OpenAPI();
        Optional.ofNullable(env.getProperty("swagger.baseurl"))
                .map(baseurlStr -> Arrays.stream(baseurlStr.split(","))
                        .map(String::trim)
                        .filter(StringUtils::isNotNullOrEmpty)
                        .collect(Collectors.toList()))
                .orElse(List.of())
                .forEach(baseurl -> {
                    openAPI.addServersItem(new Server().url(baseurl));
                });

        return openAPI.info(new Info().title("MOI RESTFUL API Documentation")
                            .version("Build on "+"")
                    );
    }
}

