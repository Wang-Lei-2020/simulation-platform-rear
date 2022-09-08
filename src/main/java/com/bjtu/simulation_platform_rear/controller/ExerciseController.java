package com.bjtu.simulation_platform_rear.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bjtu.simulation_platform_rear.config.ExcelUtils;
import com.bjtu.simulation_platform_rear.dao.IExerciseDao;
import com.bjtu.simulation_platform_rear.entity.Exercise;
import com.bjtu.simulation_platform_rear.entity.ImportFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author Liyuanbo
 */
@Slf4j
@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    @Resource
    private IExerciseDao iExerciseDao;
    @PostMapping("/getAllExercise")
    public void getAllExercise(){
//        LambdaQueryWrapper<Exercise> lambdaQueryWrapper = Wrappers.lambdaQuery();
//        List<Exercise> list = "";
//        = lambdaQueryWrapper.eq(Exercise::," ");
        return;
    }
    @PostMapping("/uploadGroup")
    @ResponseBody
    public void loadGroup(@RequestParam MultipartFile file,@RequestParam("courseId") int courseId){
        System.out.println("courseId："+courseId);
        System.out.println(file.getContentType());
        List<List<String>> list = ExcelUtils.readExcel(file);
        for(List<String> a:list){
//            if(a.get(0) !=null&& a.get(1) !=null&& a.get(2) !=null&& a.get(5) !=null){

//            }
            for(String s:a){
                System.out.println(s);
            }
        }
//        List<Exercise> exerciseList =
//        iExerciseDao.addGroup(courseId,);
    }
}
