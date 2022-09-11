package com.bjtu.simulation_platform_rear;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bjtu.simulation_platform_rear.dao.IExerciseDao;
import com.bjtu.simulation_platform_rear.dao.IUserDao;
import com.bjtu.simulation_platform_rear.entity.Exercise;
import com.bjtu.simulation_platform_rear.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private IUserDao userDao;
    @Autowired
    private IExerciseDao iexerciseDao;
    @Test
    void contextLoads() {
//        LambdaQueryWrapper<Exercise> lambdaQueryWrapper = Wrappers.lambdaQuery();
//        lambdaQueryWrapper.eq(Exercise::getExerciseId,43);
//        List<Exercise> list = iexerciseDao.select
        List<Exercise> list = iexerciseDao.getGroupList(10);
        for (Exercise ex:list) {
            System.out.println(ex.toString());
        }
    }
//    @Test
//    void getUserCountTest(){
//        int count = userDao.getUserCount();
//        System.out.println(count);
//    }
//    @Test
//    void addGroup(){
//        List<Exercise> list= new ArrayList<>();
//        Exercise e1 = new Exercise(1,"2","题目一","5","6","7","8","9","10");
//        Exercise e2 = new Exercise(1,"2","题目二","5","6","7","8","9","10");
//        list.add(e1);
//        list.add(e2);
//        iexerciseDao.addGroup(1,list);
//    }
    @Test
    void getExerciseList(){
        List<Exercise> list = iexerciseDao.getExerciseList(10,"练习题组一");
        for (Exercise l:list) {
            System.out.println(l.toString());
        }
    }
//    @Test
//    void deleteGroup(){
//        iexerciseDao.deleteGroup(1,"2");
//    }


}
