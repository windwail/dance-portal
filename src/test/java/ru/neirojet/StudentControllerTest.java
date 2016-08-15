package ru.neirojet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.matchers.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import ru.neirojet.controllers.StudentController;
import ru.neirojet.domain.Student;
import ru.neirojet.service.StudentFactory;
import ru.neirojet.service.StudentFactoryRestImpl;
import ru.neirojet.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private StudentFactoryRestImpl studentFactoryRest;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                //.setViewResolvers(viewResolver())
                .build();

        studentFactoryRest = new StudentFactoryRestImpl();
    }

    public ViewResolver viewResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(engine);
        return viewResolver;
    }

    @Test
    public void testList() throws Exception  {
        List<Student> list = new ArrayList<>();
        list.add(new Student());
        list.add(new Student());
        list.add(new Student());

        Mockito.when(studentService.findAll()).thenReturn((List)list);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/list"))
                .andExpect(model().attribute("students", hasSize(3)));
    }

    @Test
    public void testShow() throws Exception {
        Integer id = 1;

        Mockito.when(studentService.findById(id)).thenReturn(new Student());

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/show"))
                .andExpect(model().attribute("student", instanceOf(Student.class)));
    }

    @Test
    public void testEdit() throws Exception {
        Integer id = 1;

        Mockito.when(studentService.findById(id)).thenReturn(new Student());

        mockMvc.perform(get("/student-edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/student-form"))
                .andExpect(model().attribute("student", instanceOf(Student.class)));
    }


    @Test
    public void testNew() throws Exception {

        Mockito.when(studentService.createStudent()).thenReturn(studentFactoryRest.createStudent());

        mockMvc.perform(get("/student-new"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/student-form"))
                .andExpect(model()
                        .attribute("student",
                                allOf(
                                        instanceOf(Student.class),
                                        hasProperty("id", notNullValue(Integer.class)
                                    )
                                )
                        ));

        //Mockito.verifyZeroInteractions(studentService);
    }

    @Test
    public void testSave() throws Exception {

        String id = "1";
        String firstName = "firstName";
        String lastName = "lastName";
        String gender = "gender";
        String email = "email";
        String cell = "cell";

        Student s = new Student();
        s.setId(1);
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setGender(gender);
        s.setEmail(email);
        s.setCell(cell);

        ResultActions resultActions = mockMvc.perform(post("/student")
                .param("id", id)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("gender", gender)
                .param("email", email)
                .param("cell", cell))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/student/1"))
                .andExpect(model().attribute("student", instanceOf(Student.class)))
                .andExpect(model().attribute("student", hasProperty("id", is(1))))
                .andExpect(model().attribute("student", hasProperty("firstName",is(firstName))))
                .andExpect(model().attribute("student", hasProperty("lastName",is(lastName))))
                .andExpect(model().attribute("student", hasProperty("gender",is(gender))))
                .andExpect(model().attribute("student", hasProperty("email",is(email))))
                .andExpect(model().attribute("student", hasProperty("cell",is(cell))));

        ArgumentCaptor<Student> boundStudent = ArgumentCaptor.forClass(Student.class);
        verify(studentService).saveStudent(boundStudent.capture());

        Assert.assertEquals(firstName, boundStudent.getValue().getFirstName());
        //TODO: and all other fields.
    }

    @Test
    public void testDelete() throws Exception {
        Integer id = 1;


        mockMvc.perform(get("/student-delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1)).delete(id);
    }




}
