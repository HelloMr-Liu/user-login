package com.king2.userlogin.controller;

import com.king2.userlogin.pojo.SystemResult;
import com.king2.userlogin.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 * 说明：用户登录请求控制器
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/2  11:30            创建
 * =================================================================
 **/
@Controller
public class UserLoginController {


    @Autowired
    private UserLoginService userLoginService;


    /**
     *  功能：用户登录
     *  时间：2020/4/2 11:33
     *  参数：名称            类型            描述
     *       userName      String          用户名
     *       password      String          用户密码
     *       TOKEN         String          当前用户唯一标识
     *       httpSession   HttpSession     请求回话域
     *  返回：SystemResult
     *  描述：封装了对应的请求响应信息
     */
    @RequestMapping("/user/login")
    public String userLogin(String userName, String password, String TOKEN, HttpSession httpSession){
        userLoginService.userLogin(userName,password,TOKEN,httpSession);

        return "test";
    }

    @RequestMapping("/login3")
    public ModelAndView showTest(ModelAndView modelAndView){
        List<String> list=new ArrayList<>();
        list.add("刘梓江1");
        list.add("刘梓江2");
        list.add("刘梓江3");
        modelAndView.addObject("message", list);
        modelAndView.setViewName("index");
        return modelAndView;
    }



}
    
    
    