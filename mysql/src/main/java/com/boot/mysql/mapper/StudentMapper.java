package com.boot.mysql.mapper;

import com.boot.mysql.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface StudentMapper {
    public Student selectStudentById(Integer id);
    public List<Student> selectStudent();


}
