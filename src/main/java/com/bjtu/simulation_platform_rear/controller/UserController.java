package com.bjtu.simulation_platform_rear.controller;

import com.bjtu.simulation_platform_rear.common.Result;
import com.bjtu.simulation_platform_rear.entity.User;
import com.bjtu.simulation_platform_rear.service.UserService;
import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author WangLei
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoding;

    @PostMapping("/login")
    public Result<User> login(@RequestParam String userName, @RequestParam String password, HttpServletRequest request){
        try{
            User user = userService.findByUserName(userName);
            if (user != null){
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
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return Result.success();
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User newUser){
        try{
            User existUser = userService.findByUserName(newUser.getUserName());
            if (existUser == null){
                newUser.setPassword(encoding.encode(newUser.getPassword()));

                //未实现角色管理！！！
                newUser.setRole("student");
                userService.register(newUser);
                return Result.success();
            }else{
                return Result.error("3", "用户名已被使用！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/search")
    public Result<List<User>> search(@RequestBody User user){
        try{
            int total = userService.countByRealName(user.getRealName());
            if(total <= 0){
                return Result.error("3","没有查询到结果！");
            }else{
                List<User> userList = userService.searchByRealName(user.getRealName());
                return Result.success(userList);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/list")
    public Result<List<User>> getUserList(@RequestParam int from, @RequestParam int num){
        try{
            List<User> userList = userService.getUserList(from, num);
            return Result.success(userList);
//            int total = userService.countByRealName(user.getRealName());
//            if(total <= 0){
//                return Result.error("3","没有查询到结果！");
//            }else{
//                List<User> userList = userService.searchByRealName(user.getRealName());
//                return Result.success(userList);
//            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/count")
    public Result<Integer> getUserCount(){
        try{
            int count = userService.getUserCount();
            return Result.success(count);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/checkPassword")
    public Result<Integer> checkPassword(@RequestParam int id,@RequestParam String password){
        try{
            if(encoding.matches(password,userService.getPasswordById(id))){
                return Result.success(1);
            }else{
                return Result.success(0);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/update")
    private Result<User> updateSystemUser(@RequestBody User user){
        user.setPassword(encoding.encode(user.getPassword()));
        userService.update(user);
        return Result.success(user,"更新成功！");
    }

    @PostMapping("/send")
    public Result<String> sendMSM(@RequestParam String telephone) throws Exception {
        String apiUrl = "http://sms_developer.zhenzikj.com";
        String appId = "107456";
        String appSecret = "bb3f909f-ef88-4343-8a19-9930e728a0f8";

        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl,appId, appSecret);

        HashMap<String, Object> map = new HashMap<>();
        //这个是榛子云短信平台用户中心下的短信管理的短信模板的模板id
        map.put("templateId", "2705");
        //生成验证码
        int pow = (int) Math.pow(10, 4 - 1);
        String verificationCode = String.valueOf((int) (Math.random() * 9 * pow + pow));
        //随机生成messageId，验证验证码的时候，需要携带这个参数去取验证码
        String messageId = UUID.randomUUID().toString();
        map.put("messageId", messageId);
        String[] templateParams = new String[2];
        //两个参数分别为验证码和过期时间
        templateParams[0] = verificationCode;
        templateParams[1] = String.valueOf("五分钟");
        map.put("templateParams", templateParams);
        map.put("number", telephone);

        String result = client.send(map);

        Result<String> dataResponse = new Result<>();
        dataResponse.setCode("0");
        dataResponse.setData(verificationCode);
        return dataResponse;
    }
}
