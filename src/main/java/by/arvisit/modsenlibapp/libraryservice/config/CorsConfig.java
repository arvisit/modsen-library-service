package by.arvisit.modsenlibapp.libraryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Value("${spring.security.cors.allowedOrigin}")
    private String allowedOrigin;
    @Value("${spring.security.cors.allowedMethod}")
    private String allowedMethod;
    @Value("${spring.security.cors.allowedHeader}")
    private String allowedHeader;

    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedHeader(allowedHeader);
        config.addAllowedMethod(allowedMethod);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}