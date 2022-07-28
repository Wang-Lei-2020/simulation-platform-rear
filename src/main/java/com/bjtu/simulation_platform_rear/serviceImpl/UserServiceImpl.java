package com.bjtu.simulation_platform_rear.serviceImpl;

import com.bjtu.simulation_platform_rear.dao.IUserDao;
import com.bjtu.simulation_platform_rear.entity.User;
import com.bjtu.simulation_platform_rear.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private IUserDao userDao;

    @Override
    public void add(User user) {
        userDao.addUser(user);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
