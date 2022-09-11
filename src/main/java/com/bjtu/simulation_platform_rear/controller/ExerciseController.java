package com.bjtu.simulation_platform_rear.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bjtu.simulation_platform_rear.common.Result;
import com.bjtu.simulation_platform_rear.config.ExcelUtils;
import com.bjtu.simulation_platform_rear.dao.IExerciseDao;
import com.bjtu.simulation_platform_rear.entity.Exercise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public Result<List<Exercise>> getAllExercise(@RequestBody Exercise exercise){

//        LambdaQueryWrapper<Exercise> lambdaQueryWrapper = Wrappers.lambdaQuery();
//        lambdaQueryWrapper.eq(Exercise::getExerciseId,43);
//        List<Exercise> list = iExerciseDao.selectOne()
        try {
            List<Exercise> exerciseList = iExerciseDao.getExerciseList(exercise.getCourseId(),exercise.getGroupName());
            for(Exercise ex :exerciseList)
                System.out.println(ex.toString());
            if(exerciseList!=null){
                return Result.success(exerciseList);
            }
            else{
                return  Result.error("1","获取失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("/uploadGroup")
    @ResponseBody
    public Result<Exercise> loadGroup(@RequestParam MultipartFile file,@RequestParam("groupName") String groupName,@RequestParam("courseId") int courseId) {
        try {
            String existList = "";
            List<List<String>> list = ExcelUtils.readExcel(file);
            list.remove(0);
            List<Exercise> exerciseList = new ArrayList<>();
            int exerciseNo = 0;
            int successRow = 0;
            for (List<String> a : list) {
                exerciseNo++;
                if(a.size() < 6){
                    return Result.error("1","必填项为空！");
                }
                else if(a.size() == 6){
                    a.add("略");
                }
                if (a.get(0) != null && a.get(1) != null && a.get(2) != null && a.get(5) != null) {
                    if(iExerciseDao.findExerciseByCGE(courseId,groupName,a.get(0))!=null){
                        Exercise e = new Exercise(courseId, groupName, a.get(0), a.get(1), a.get(2), a.get(3), a.get(4), a.get(5), a.get(6));
                        exerciseList.add(e);
                        successRow++;
                    }
                    else{
                        System.out.println("本题已存在!");
                        existList += exerciseNo+"、";
                    }
                }
            }
            if(successRow==0){
                return Result.error("1","题目均已存在！");
            }
            else if(successRow < exerciseNo){
                iExerciseDao.addGroup(courseId,exerciseList);
                String msg = "第"+existList.substring(0,existList.length()-1)+"行题目已存在！";
                return Result.error("2",msg);
            }
            else{
                iExerciseDao.addGroup(courseId,exerciseList);
                return Result.success();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
