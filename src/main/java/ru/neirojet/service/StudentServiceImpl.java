package ru.neirojet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.neirojet.domain.Student;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("memorystorage")
public class StudentServiceImpl implements StudentService {

    private static Map<Integer, Student> students = new HashMap<>();

    @Qualifier("studentFactoryFileImpl")
    @Autowired
    StudentFactory factory;

    @Qualifier("studentFactoryRestImpl")
    @Autowired
    StudentFactory restFactory;

    @PostConstruct
    public void init() {
        for(int i=0; i < 5; i++) {
            Student s = factory.createStudent();
            students.put(s.getId(),s);
        }
    }

    @Override
    public Student createStudent() {
        Student s = restFactory.createStudent();
        if(s==null) s = new Student();
        return s;
    }

    @Override
    public void setStudentFactory(StudentFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList(students.values());
    }

    @Override
    public Student findByName(String name) {
        for(Student s: students.values()) {
            if(s.getFirstName().equalsIgnoreCase(name) || s.getLastName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public Student findById(Integer id) {
        return students.get(id);
    }

    @Override
    public Student saveStudent(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public void delete(Integer id) {
        students.remove(id);
    }
}
