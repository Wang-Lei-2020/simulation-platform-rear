package com.bjtu.simulation_platform_rear.controller;

import com.bjtu.simulation_platform_rear.common.Result;
import com.bjtu.simulation_platform_rear.entity.User;
import com.bjtu.simulation_platform_rear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoding;

    @PostMapping("/login")
    public Result<User> login(@RequestParam String email, @RequestParam String password, HttpServletRequest request){
        try{
            User user = userService.findByEmail(email);
            if (user != null){

//                if (user.getPassword().equals(password)){
                if (encoding.matches(password,user.getPassword())){
                    //设置session值
                    HttpSession session = request.getSession();
                    session.setAttribute("user",user);
                    session.setAttribute("msg","测试session");

                    //消除返回前端的收能过户数据中的重要信息
                    user.setPassword("");
                    return Result.success(user);
                }else {
                    return Result.error("1", "用户名或密码错误！");
                }
            }else{
                return Result.error("2", "用户名或密码错误！");
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
//        return userService.findByEmail(email);
    }

//    @PostMapping
//    public User findByEmail(HttpServletRequest request){
//        User user = userService.findByEmail("hjh@qq.com");
//        HttpSession session = request.getSession();
//        session.setAttribute("msg","session信息");
//        session.setAttribute("user",user);
//        System.out.println(session);
//        return user;
//    }

    @GetMapping("/test")
    public void test(HttpServletRequest request){
        System.out.println("test");
        HttpSession session = request.getSession();
        String msg = session.getAttribute("msg").toString();
        System.out.println(msg);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return Result.success();
    }
}
