package com.crawling.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private String version;
    private String title;

    @Bean
    public Docket apiV1() {
        version = "v1";
        title = "naver news crawling API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.crawling.controller"))
                .paths(PathSelectors.ant("/v1/news/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }


    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "Swagger로 생성한 네이버 뉴스 API Docs",
                version,
                "www.example.com",
                new Contact("Contact Me", "www.example.com", "yunjigo@gmail.com"),
                "Licenses yunji",
                "www.example.com",
                new ArrayList<>());

                
    }
    
}