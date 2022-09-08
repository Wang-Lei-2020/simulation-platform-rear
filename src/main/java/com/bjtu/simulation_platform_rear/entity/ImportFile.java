package com.bjtu.simulation_platform_rear.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonIgnoreProperties("file")
public class ImportFile {
    private MultipartFile file;
    private int courseId;
}
