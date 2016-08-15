package ru.neirojet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import ru.neirojet.domain.Student;

@SpringBootApplication
public class DancePortalApplication {
	public static void main(String[] args) {
		SpringApplication.run(DancePortalApplication.class, args);
	}
}
