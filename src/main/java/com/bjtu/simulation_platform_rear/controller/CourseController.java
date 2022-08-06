package com.bjtu.simulation_platform_rear.controller;

import com.bjtu.simulation_platform_rear.common.Result;
import com.bjtu.simulation_platform_rear.entity.Course;
import com.bjtu.simulation_platform_rear.entity.PickCourse;
import com.bjtu.simulation_platform_rear.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WangLei
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public Result<Course> addCourse(@RequestBody Course newCourse){
        try{
            Course existCourse = courseService.findByCourseName(newCourse.getCourseName());
            if (existCourse != null && existCourse.getCourseTeacher().equals(newCourse.getCourseTeacher())){
                return Result.error("1", "该课程您已添加！");
            }else{
                //未实现上传课程文件！！！
                newCourse.setCourseFile("");
                courseService.addCourse(newCourse);
                return Result.success();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/search")
    public Result<List<Course>> search(@RequestBody Course course){
        try{
            int total = courseService.countByCourseName(course.getCourseName());
            if(total <= 0){
                return Result.error("3","没有查询到结果！");
            }else{
                List<Course> courseList = courseService.searchByCourseName(course.getCourseName());
                return Result.success(courseList);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/list")
    public Result<List<Course>> getCourseList(@RequestParam int from, @RequestParam int num){
        try{
            List<Course> courseList = courseService.getCourseList(from, num);
            return Result.success(courseList);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/count")
    public Result<Integer> getCourseCount(){
        try{
            int count = courseService.getCourseCount();
            return Result.success(count);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/update")
    private Result<Course> updateCourse(@RequestBody Course course){
        try{
            Course existCourse = courseService.findByCourseName(course.getCourseName());
            if (existCourse != null && existCourse.getCourseTeacher().equals(course.getCourseTeacher())){
                return Result.error("1", "该课程您已添加！");
            }else{
                //未实现上传课程文件！！！
                course.setCourseFile("");
                courseService.update(course);
                return Result.success(course,"更新成功！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/select")
    public Result<PickCourse> selectCourse(@RequestBody PickCourse pickCourse){
        try{
            PickCourse existPickCourse = courseService.findPickCourse(pickCourse.getCourseId(),pickCourse.getUserId());
            if(existPickCourse == null) {
                courseService.addPickCourse(pickCourse);
                return Result.success();
            }else{
                return Result.error("1","您已选过该课程，请勿重复选课！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/myCourseList")
    public Result<List<PickCourse>> getMyCourseList(@RequestParam int userId){
        try{
            List<PickCourse> myCourseList = courseService.getMyCourseList(userId);
            return Result.success(myCourseList);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
