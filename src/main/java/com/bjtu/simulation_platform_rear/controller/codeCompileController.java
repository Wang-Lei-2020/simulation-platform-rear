package com.bjtu.simulation_platform_rear.controller;

import com.bjtu.simulation_platform_rear.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.UUID.randomUUID;

/**
 * 执行在线编译的接口
 * @author zhangxingyu
 */

@Slf4j
@RestController
@RequestMapping("/code")
public class codeCompileController {
    @PostMapping("/compile")
    public Result<String> compile(@RequestParam String code, @RequestParam String lang){
        try{
            String fileFormat;  //生成代码文件的格式
            switch (lang){
                case "python":
                    fileFormat = ".py";
                    break;
                case "java":
                    fileFormat = ".java";
                    break;
                default:
                    return Result.error("2", "语言错误！");
            }

            //生成代码文件，文件所在文件夹用uuid命名，文件命名Solution.*
            String uuid = randomUUID().toString();
            String filepath = System.getProperty("user.dir")+"\\src\\main\\java\\com\\bjtu\\simulation_platform_rear\\codeFile\\"+
                    uuid;
            Process createFolderProcess = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "md "+ filepath});
            createFolderProcess.waitFor();
            createFolderProcess.destroy();

            String filename = filepath + "\\Solution" + fileFormat;
            File file = new File(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filename))));
            bufferedWriter.write(code);
            bufferedWriter.close();

            //通过cmd命令，执行代码文件
            Process compileProcess = null;
            switch (lang){
                case "python":
                    compileProcess = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "python" + " " + file.getAbsolutePath()});
                    break;
                case "java":
                    compileProcess = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "javac " +
                            file.getAbsolutePath() + " && java -cp " + filepath + " Solution"});
                    break;
            }

            //获取执行结果，通过接口返回
            BufferedReader br = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "rd /s /q "+ filepath});
            compileProcess.destroy();
            return Result.success(result);
        }catch (Exception e){
            try{
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "rd /s /q "+
                        System.getProperty("user.dir")+"\\src\\main\\java\\com\\bjtu\\simulation_platform_rear\\codeFile\\*.*"});
            }catch (IOException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
            return Result.error("1", "编译错误！");
        }
    }
}
