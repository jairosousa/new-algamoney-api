package com.algamoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class AlgamoneyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgamoneyApiApplication.class, args);
    }

    /**
     * Liberação do Cors Global
     * mais já esta depreciado.
     * Não suporta o Spring oauth2
     */
//    @Bean
//    public WebMvcConfigurer corsConfigures(){
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/*").allowedOrigins("http://localhost:8000");
//            }
//        }
//    }

}
