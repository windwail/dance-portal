package ru.neirojet.service;

import org.springframework.boot.json.JsonParser;
import ru.neirojet.domain.Student;

/**
 * Created by icetusk on 29.07.16.
 */
public interface StudentFactory {

    Student createStudent();

}
