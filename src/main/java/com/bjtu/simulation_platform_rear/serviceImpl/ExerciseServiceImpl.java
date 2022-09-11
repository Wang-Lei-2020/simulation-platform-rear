package com.bjtu.simulation_platform_rear.serviceImpl;

import com.bjtu.simulation_platform_rear.dao.IExerciseDao;
import com.bjtu.simulation_platform_rear.entity.Exercise;
import com.bjtu.simulation_platform_rear.service.ExerciseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    @Resource
    IExerciseDao iExerciseDao;
    @Override
    public List<Exercise> getGroupList(int courseId){
        return iExerciseDao.getGroupList(courseId);
    };
    @Override
    public List<Exercise> getExerciseList(int courseId,String groupName){
        return iExerciseDao.getExerciseList(courseId,groupName);
    };
    @Override
    public void addGroup(int courseId,List<Exercise> group){
        iExerciseDao.addGroup(courseId,group);
    };
    @Override
    public void deleteGroup(int courseId, String groupName){
        iExerciseDao.deleteGroup(courseId, groupName);
    };
    @Override
    public List<Exercise> findExerciseByCGE(int courseId,String groupName,String exerciseName){return iExerciseDao.findExerciseByCGE(courseId,groupName,exerciseName);};
}
