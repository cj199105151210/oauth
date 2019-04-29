package com.boot.mysql.controller;

import com.boot.mysql.entity.Student;
import com.boot.mysql.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/stu")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/one")
    public Student getOne(Integer id){
        return studentService.selectStudentById(id);
    }

}
