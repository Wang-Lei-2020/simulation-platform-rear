package com.bjtu.simulation_platform_rear.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author WangLei
 */
@Setter
@Getter
public class PickCourse {
    private int courseId;
    private int userId;
    private int grade;

    private String courseName;
    private String courseTeacher;
    private String introduction;
    private int courseCredit;
    private int capacity;
}
