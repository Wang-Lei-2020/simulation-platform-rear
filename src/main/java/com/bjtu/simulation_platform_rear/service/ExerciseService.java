package com.bjtu.simulation_platform_rear.service;

import com.bjtu.simulation_platform_rear.entity.Exercise;

import java.util.List;

/**
 * @author Liyuanbo
 */
public interface ExerciseService {
    /**
     * 获取课程的所有习题组
     * @param  courseId
     */
    List<Exercise> getGroupList(int courseId);
    /**
     * 获取指定习题组下的所有习题
     * @param  courseId,groupName
     */
    List<Exercise> getExerciseList(int courseId,String groupName);
    /**
     * 指定课程下新增习题组
     * @param  courseId,group
     */
    void addGroup(int courseId,List<Exercise> group);
    /**
     * 删除指定课程下的习题组
     * @param  courseId,groupName
     */
    void deleteGroup(int courseId, String groupName);

}
