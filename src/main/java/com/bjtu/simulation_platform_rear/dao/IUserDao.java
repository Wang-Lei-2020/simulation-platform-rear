package com.bjtu.simulation_platform_rear.dao;

import com.bjtu.simulation_platform_rear.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WangLei
 */
@Mapper
@Repository("userDao")
public interface IUserDao {
    /**
     * 根据用户名查找
     * @param userName 用户名
     * @return User
     */
    User findByUserName(String userName);

    /**
     * 添加用户
     * @param user 用户
     */
    void register(User user);

    /**
     * 根据真实姓名模糊查询用户个数
     * @param realName 真实姓名
     * @return int
     */
    int countByRealName(String realName);

    /**
     * 根据真实姓名查询用户列表
     * @param realName 真实姓名
     * @return list
     */
    List<User> searchByRealName(String realName);

    /**
     * 获取所有用户列表
     * @param from 从第几个开始
     * @param num 查多少个
     * @return list
     */
    List<User> getUserList(int from, int num);

    /**
     * 得到所有用户数
     * @return int
     */
    int getUserCount();

    /**
     * 根据Id返回加密后的密码
     * @param id id
     * @return String
     */
    String getPasswordById(int id);

    /**
     * 更新用户
     * @param user 用户
     */
    void update(User user);
}
