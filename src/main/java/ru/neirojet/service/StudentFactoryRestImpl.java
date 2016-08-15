package ru.neirojet.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import ru.neirojet.domain.Student;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

@Service
public class StudentFactoryRestImpl implements StudentFactory{
    private int count = 10000;

    @Override
    public Student createStudent()  {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "https://randomuser.me/api/",
                    String.class);

            JsonFactory fa = new JsonFactory();
            JsonParser pa = fa.createParser(response.getBody());

            HashMap<String, LinkedList<String>> fieldsQueries = new HashMap<>();

            ReflectionUtils.doWithFields(Student.class, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    if (field.isAnnotationPresent(JsonProperty.class)) {
                        JsonProperty p = field.getAnnotationsByType(JsonProperty.class)[0];
                        String query = p.value();
                        LinkedList<String> searchable = new LinkedList<>();
                        searchable.addAll(Arrays.asList(query.split("[.]")));
                        fieldsQueries.put(field.getName(), searchable);
                    }
                }
            });


            Student s = new Student();

            while (true) {
                JsonToken t = pa.nextToken();
                if (t == null) {
                    break;
                }
                for (String fieldName : fieldsQueries.keySet()) {
                    LinkedList<String> searchable = fieldsQueries.get(fieldName);
                    if (t == JsonToken.FIELD_NAME
                            && searchable.peek() != null
                            && searchable.peek().equalsIgnoreCase(pa.getCurrentName())) {
                        if (searchable.size() > 1) {
                            //System.out.println("\tremoving " + searchable.peek());
                            searchable.poll();
                        } else {
                            pa.nextToken();
                            Field f = ReflectionUtils.findField(Student.class, fieldName);
                            f.setAccessible(true);
                            ReflectionUtils.setField(f, s, pa.getValueAsString());
                            //System.out.println("\tvalue " + pa.getValueAsString());
                            searchable.poll();
                            break;
                        }
                    }
                }
            }

            //s.setId(++count);

            return s;

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
