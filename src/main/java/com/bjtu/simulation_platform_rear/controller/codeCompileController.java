package com.bjtu.simulation_platform_rear.controller;

import com.bjtu.simulation_platform_rear.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

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
        File file = null;
        try{
            String fileFormat;  //生成代码文件的格式
            String commandHead; //运行代码文件的命令头
            String filename = "";    //生成代码文件的路径
            switch (lang){
                case "python":
                    fileFormat = ".py";
                    commandHead = "python";
                    break;
                case "java":
                    fileFormat = ".java";
                    commandHead = "javac";

                    break;
                default:
                    return Result.error("2", "语言错误！");
            }
            //生成代码文件，文件名用uuid命名
            UUID uuid = randomUUID();
            filename = System.getProperty("user.dir")+"/src/main/java/com/bjtu/simulation_platform_rear/codeFile/" + uuid.toString() + fileFormat;
            file = new File(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filename))));
            bufferedWriter.write(code);
            bufferedWriter.close();

            //通过cmd命令，执行代码文件
            Process ps = null;
            String[] cmd = null;
            switch (lang){
                case "python":
                    cmd = new String[]{"cmd", "/c", commandHead + " " + file.getAbsolutePath()};
                    ps = Runtime.getRuntime().exec(cmd);
                    break;
                case "java":
                    cmd = new String[]{"cmd", "/c", commandHead + " " + file.getAbsolutePath()};
                    Process ps1 = Runtime.getRuntime().exec(cmd);
                    commandHead = "java";
                    cmd = new String[]{"cmd", "/c", commandHead + " " + file.getAbsolutePath().replace(".java", "")};
                    log.info(Arrays.toString(cmd));
                    ps = Runtime.getRuntime().exec(cmd);
                    break;
            }

            //获取执行结果，通过接口返回
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
//            file.delete();
            ps.destroy();
            return Result.success(result);
        }catch (Exception e){
//            if(file != null && file.exists())
//                file.delete();
            e.printStackTrace();
            return Result.error("1", "编译错误！");
        }
    }
}
