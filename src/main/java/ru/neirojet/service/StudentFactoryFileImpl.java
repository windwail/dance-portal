package ru.neirojet.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.neirojet.domain.Student;

import javax.annotation.PostConstruct;

@Service
public class StudentFactoryFileImpl implements StudentFactory {

    private Student[] students;

    private int count = 0;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    private void init() {
        try {
            JsonFactory fa = new JsonFactory();
            JsonParser pa = fa.createParser(resourceLoader.getResource("classpath:students.json").getURL());
            ObjectMapper mapper = new ObjectMapper();
            students = mapper.readValue(pa, Student[].class);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Student createStudent() {
        count = (count >= students.length)? 0 : ++count;

        Student s  = students[count];
        //s.setId(count);
        s.setPhotoUrl("http://localhost:8080/img/dummy.jpg");

        return s;
    }
}
