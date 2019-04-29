package com.boot.mysql.service.impl;

import com.boot.mysql.entity.Student;
import com.boot.mysql.mapper.StudentMapper;
import com.boot.mysql.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student selectStudentById(Integer id) {
        return studentMapper.selectStudentById(id);
    }

    @Override
    public List<Student> selectStudent() {
        return studentMapper.selectStudent();
    }
}
