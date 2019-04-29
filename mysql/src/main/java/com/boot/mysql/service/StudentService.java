package com.boot.mysql.service;

import com.boot.mysql.entity.Student;

import java.util.List;

public interface StudentService {

    public Student selectStudentById(Integer id);

    public List<Student> selectStudent();

}
