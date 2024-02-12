package by.arvisit.modsenlibapp.libraryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ModsenLibraryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModsenLibraryServiceApplication.class, args);
    }

}
