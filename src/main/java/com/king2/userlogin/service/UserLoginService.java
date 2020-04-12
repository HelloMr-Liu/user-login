package com.king2.userlogin.service;

import com.king2.userlogin.pojo.SystemResult;
import jdk.nashorn.internal.ir.FunctionNode;

import javax.servlet.http.HttpSession;

/**
 * ================================================================
 * 说明：用户登录业务接口
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/2  11:32            创建
 * =================================================================
 **/
public interface UserLoginService {


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
    SystemResult userLogin(String userName, String password, String TOKEN, HttpSession httpSession);

}
