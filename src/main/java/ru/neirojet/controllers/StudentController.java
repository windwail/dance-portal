package ru.neirojet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.neirojet.domain.Student;
import ru.neirojet.service.StudentService;

@Controller
public class StudentController {

    @Autowired
    StudentService service;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/students")
    public String students(Model model){
        model.addAttribute("students", service.findAll());
        return "student/list";
    }


    @RequestMapping("/student/{id}")
    public String student(@PathVariable Integer id, Model model) {
        model.addAttribute("student", service.findById(id));
        return "student/show";
    }

    @RequestMapping("/student-new")
    public String studentNew(Model model) {
        Student s = service.createStudent();
        model.addAttribute("student", s);
        return "student/student-form";
    }

    @RequestMapping("/student-new-clear")
    public String studentNewClear(Model model) {
        Student s = new Student();
        model.addAttribute("student", s);
        return "student/student-form";
    }

    @RequestMapping("/student-edit/{id}")
    public String studentEdit(@PathVariable Integer id, Model model) {
        model.addAttribute("student", service.findById(id));
        return "student/student-form";
    }

    @RequestMapping("/student-delete/{id}")
    public String studentDelete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/students";
    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public String studentNew(Student student) {
        Student savedStudent = service.saveStudent(student);
        return "redirect:/student/"+savedStudent.getId();
    }



}
