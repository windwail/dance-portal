package ru.neirojet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ru.neirojet.domain.Student;
import ru.neirojet.service.StudentFactoryRestImpl;
import ru.neirojet.service.StudentFactoryFileImpl;

import java.io.File;
import java.util.*;

/**
 * Created by icetusk on 29.07.16.
 */
public class CollectUsersToFileUtil {

    @Test
    public void testRest() throws Exception {

        StudentFactoryRestImpl factory = new StudentFactoryRestImpl();

        List<Student> students = new ArrayList<>();

        for(int i=0; i < 1000; i++) {
            students.add(factory.createStudent());
        }

        ObjectMapper mapper = new ObjectMapper(); // create once, reuse
        mapper.writeValue(new File("/tmp/result.json"), students);


    }

    @Test
    public void testFile() throws Exception {
        StudentFactoryFileImpl f = new StudentFactoryFileImpl();


        f.createStudent();
    }
}
