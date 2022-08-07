package com.bjtu.simulation_platform_rear.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author WangLei
 */
@Setter
@Getter
public class Course {
    private int courseId;
    private String courseName;
    private String courseTeacher;
    private String courseFile;
    private String introduction;
    private int courseCredit;
    private int capacity;
    private int leftCapacity;// TODO 尚未用到
}
