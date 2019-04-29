package com.boot.mysql.mapper;

import com.boot.mysql.entity.ClassRoom;

import java.util.List;

public interface ClassRoomMapper {
    public List<ClassRoom> selectClassRoom();
    public ClassRoom selectClassRoomById(Integer id);


}

