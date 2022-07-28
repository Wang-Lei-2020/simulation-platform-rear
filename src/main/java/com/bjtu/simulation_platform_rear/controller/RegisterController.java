package com.bjtu.simulation_platform_rear.controller;

import com.bjtu.simulation_platform_rear.common.Result;
import com.bjtu.simulation_platform_rear.entity.User;
import com.bjtu.simulation_platform_rear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoding;

    @PostMapping
    public Result<User> createSingleUser(@RequestBody User newUser){
        try{
            User existUser = userService.findByEmail(newUser.getEmail());
            if (existUser == null){
                newUser.setPassword(encoding.encode(newUser.getPassword()));
                newUser.setRole("customer");
                userService.add(newUser);
                return Result.success();
            }else{
                return Result.error("3", "邮箱已被使用！");
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
