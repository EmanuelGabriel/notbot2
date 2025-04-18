package br.com.emanuelgabriel.notbot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Notbot2Application {

    public static void main(String[] args) {
        SpringApplication.run(Notbot2Application.class, args);
    }

}
