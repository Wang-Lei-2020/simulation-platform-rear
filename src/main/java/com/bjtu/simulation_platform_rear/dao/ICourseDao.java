package com.bjtu.simulation_platform_rear.dao;

import com.bjtu.simulation_platform_rear.entity.Course;
import com.bjtu.simulation_platform_rear.entity.PickCourse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WangLei
 */
@Mapper
@Repository("courseDao")
public interface ICourseDao {
    /**
     * 根据课程名查找
     * @param courseName 课程名
     * @return Course
     */
    Course findByCourseName(String courseName);

    /**
     * 添加课程
     * @param course 课程
     */
    void addCourse(Course course);

    /**
     * 根据课程名模糊查询课程个数
     * @param courseName 课程名
     * @return int
     */
    int countByCourseName(String courseName);

    /**
     * 根据课程名查询课程列表
     * @param courseName 课程名
     * @return list
     */
    List<Course> searchByCourseName(String courseName);

    /**
     * 获取所有课程列表
     * @param from 从第几个开始
     * @param num 查多少个
     * @return list
     */
    List<Course> getCourseList(int from, int num);

    /**
     * 得到所有课程数
     * @return int
     */
    int getCourseCount();

    /**
     * 更新课程
     * @param course 课程
     */
    void update(Course course);

    /**
     * 查找是否已经选过课
     * @param courseId 课程id
     * @param userId 用户id
     * @return PickCourse
     */
    PickCourse findPickCourse(int courseId, int userId);

    /**
     * 添加选课
     * @param pickCourse 选课
     */
    void addPickCourse(PickCourse pickCourse);

    /**
     * 返回我的课表
     * @param userId 用户id
     * @return list
     */
    List<PickCourse> getMyCourseList(int userId);

    /**
     * 删除课程
     * @param courseId 课程id
     */
    void deleteCourse(int courseId);


    /**
     * 删除已选课程
     * @param courseId 课程id
     */
    void deletePickCourse(int courseId);

    /**
     * 删除已选课程
     * @param pickCourse 已选课
     */
    void deletePickCourseWithUserId(PickCourse pickCourse);
}
