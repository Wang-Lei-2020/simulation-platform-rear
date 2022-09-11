package com.bjtu.simulation_platform_rear.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author Liyuanbo
 */
@TableName("Exercise")
@Data
public class Exercise {
    @TableField("course_id")
    private int courseId;
    @TableField("group_name")
    private String groupName;
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private int exerciseId;
    @TableField("exercise_name")
    private String exerciseName;
    @TableField("section_a")
    private String sectionA;
    @TableField("section_b")
    private String sectionB;
    @TableField("section_c")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sectionC;
    @TableField("section_d")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sectionD;
    @TableField("right_answer")
    private String rightAnswer;
    @TableField("analysis")
    private String analysis;
    public Exercise(int courseId,String groupName,String exerciseName,String sectionA,String sectionB,String sectionC,String sectionD,String rightAnswer,String analysis){
        this.courseId = courseId;
        this.groupName = groupName;
        this.exerciseName = exerciseName;
        this.sectionA = sectionA;
        this.sectionB = sectionB;
        this.sectionC = sectionC;
        this.sectionD = sectionD;
        this.rightAnswer = rightAnswer;
        this.analysis = analysis;
    }
}
