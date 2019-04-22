package com.security.security.mapper;

import com.security.security.bean.UserPermissionDO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionMapper {

    @Select("select up.id,up.role_id,up.user_id,r.`name` as role_name from user_permission up join user u on up.user_id = u.id join role r on up.role_id = r.id where user_id = #{userId}")
    List<UserPermissionDO> selectByUserId(Integer userId);
}
