package org.sii.performance;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PerformanceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
