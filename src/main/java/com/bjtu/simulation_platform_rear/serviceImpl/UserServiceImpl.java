package com.bjtu.simulation_platform_rear.serviceImpl;

import com.bjtu.simulation_platform_rear.dao.IUserDao;
import com.bjtu.simulation_platform_rear.entity.User;
import com.bjtu.simulation_platform_rear.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author WangLei
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private IUserDao userDao;

    @Override
    public void register(User user) {
        userDao.register(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public int countByRealName(String realName){
        return userDao.countByRealName(realName);
    }

    @Override
    public List<User> searchByRealName(String realName){
        return userDao.searchByRealName(realName);
    }

    @Override
    public List<User> getUserList(int from, int num){
        return userDao.getUserList(from, num);
    }

    @Override
    public int getUserCount(){
        return userDao.getUserCount();
    }

    @Override
    public String getPasswordById(int userId){
        return userDao.getPasswordById(userId);
    }

    @Override
    public void update(User user){
        userDao.update(user);
    }
}
