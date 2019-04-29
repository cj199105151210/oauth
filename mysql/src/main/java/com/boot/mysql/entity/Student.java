package com.boot.mysql.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Student {
    private Integer id;
    private String name;
    private Integer age;
    //每个人对应一个教室
    private ClassRoom classRoom;
    public Student(){}

    public Student(String name,Integer age)
    {
        this.name = name;
        this.age = age;
    }
}
