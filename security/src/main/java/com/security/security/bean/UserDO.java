package com.security.security.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDO {

    private Integer id;
    private String username;
    private String password;
    private String roleName;
}

