package by.belyahovich.dance_events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//
@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
public class DanceEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanceEventsApplication.class, args);
    }

}
