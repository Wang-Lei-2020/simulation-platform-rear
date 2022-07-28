package com.bjtu.simulation_platform_rear.service;

import com.bjtu.simulation_platform_rear.entity.User;

public interface UserService {
    void add(User user);
    User findByEmail(String email);
}
