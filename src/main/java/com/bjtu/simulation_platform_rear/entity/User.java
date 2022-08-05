package com.bjtu.simulation_platform_rear.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

/**
 * @author WangLei
 */
@Setter
@Getter
public class User {

    private int userId;
    private String userName;
    private String realName;
    private String password;
    private String sex;
    private String email;
    private String phone;
    private String description;
    private String logoImage;
    private String role;
}
