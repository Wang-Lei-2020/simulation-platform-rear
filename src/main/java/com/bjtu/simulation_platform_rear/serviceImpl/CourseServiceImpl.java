package com.bjtu.simulation_platform_rear.serviceImpl;

import com.bjtu.simulation_platform_rear.dao.ICourseDao;
import com.bjtu.simulation_platform_rear.entity.Course;
import com.bjtu.simulation_platform_rear.entity.PickCourse;
import com.bjtu.simulation_platform_rear.service.CourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author WangLei
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private ICourseDao courseDao;

    @Override
    public void addCourse(Course course) {
        courseDao.addCourse(course);
    }

    @Override
    public Course findByCourseName(String courseName) {
        return courseDao.findByCourseName(courseName);
    }

    @Override
    public int countByCourseName(String courseName){
        return courseDao.countByCourseName(courseName);
    }

    @Override
    public List<Course> searchByCourseName(String courseName){
        return courseDao.searchByCourseName(courseName);
    }

    @Override
    public List<Course> getCourseList(int from, int num){
        return courseDao.getCourseList(from, num);
    }

    @Override
    public int getCourseCount(){
        return courseDao.getCourseCount();
    }

    @Override
    public void update(Course course){
        courseDao.update(course);
    }

    @Override
    public PickCourse findPickCourse(int courseId, int userId){
        return courseDao.findPickCourse(courseId,userId);
    }

    @Override
    public void addPickCourse(PickCourse pickCourse){
        courseDao.addPickCourse(pickCourse);
    }

    @Override
    public List<PickCourse> getMyCourseList(int userId){
        return courseDao.getMyCourseList(userId);
    }

    @Override
    public void deleteCourse(int courseId){
        courseDao.deleteCourse(courseId);
    }

    @Override
    public void deletePickCourse(int courseId){
        courseDao.deletePickCourse(courseId);
    }

    @Override
    public void deletePickCourseWithUserId(PickCourse pickCourse){
        courseDao.deletePickCourseWithUserId(pickCourse);
    }
}
