package com.ecommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    public static  final String AUTHORIZATION_HEADER = "Authorization";

    private List<SecurityContext> securityContexts(){
        return Arrays.asList(SecurityContext.builder().securityReferences(securityReferences()).build());
    }

    private List<SecurityReference> securityReferences(){
        AuthorizationScope scopes = new AuthorizationScope("global", "accessEverything");
        return Arrays.asList((new SecurityReference("scope", new AuthorizationScope[] {scopes})));
    }


    private ApiKey apiKey(){
        return new ApiKey("JWT",AUTHORIZATION_HEADER,"header");
    }



    @Bean
    public Docket apis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getInfo())
                .securityContexts(securityContexts())
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getInfo() {
        return new ApiInfo("E-commerce Application", "this is project is developed by Vinod Seervi", "1.0", "Terms of Service",
                new Contact("vinod", "https://vinodseervi.github.io/vinodseervi/", "vinodseervi0124@gmail.com"), "License of API", "API License URL", Collections.emptyList());
    }


}
