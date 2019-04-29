package com.boot.mysql.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ClassRoom {
    private Integer id;
    private String name;

    private List<Student> students;
    public ClassRoom(String name)
    {
        this.name = name;
    }


}
