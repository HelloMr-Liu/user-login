package com.king2.userlogin.service.impl;

import com.king2.userlogin.cache.LoginErrorCache;
import com.king2.userlogin.cache.MemberCache;
import com.king2.userlogin.cache.RoleJurisdictionCache;
import com.king2.userlogin.entity.EmployeeInfo;
import com.king2.userlogin.entity.StudentInfo;
import com.king2.userlogin.entity.StudentMgn;
import com.king2.userlogin.entity.TaJurisdictionList;
import com.king2.userlogin.pojo.SystemResult;
import com.king2.userlogin.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * ================================================================
 * 说明：用户登录业务接口实现
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/2  11:34            创建
 * =================================================================
 **/
@Service
public class UserLoginServiceImpl  implements UserLoginService {

    @Autowired
    private MemberCache memberCache;    //注入用户信息缓存

    @Autowired
    private RoleJurisdictionCache roleJurisdictionCache; //注入角色权限信息缓存

    @Autowired
    private LoginErrorCache loginErrorCache; //存入对的用户登录错误信息


    private String USER_TOKEN="USER_TOKEN";

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
    public SystemResult userLogin(String userName, String password, String TOKEN,HttpSession httpSession){
        //获取当前用户信息缓存
        Map<Integer, Object> mapCache = memberCache.getMapCache();

        //获取当前用户登录错误记录缓存
        Map<String, Integer> errorCache = loginErrorCache.getErrorCache();

        //校验当前用户是否正确
        Object obj = jugdeUserNameIsCorrect(userName, password, mapCache);
        if(obj==null){
            //用户登录失败
            errorCache.put(TOKEN,errorCache.get(TOKEN)==null?1:errorCache.get(TOKEN)+1);
            return SystemResult.build(401,"用户名和密码错误",errorCache.get(TOKEN));
        }


        List<TaJurisdictionList> memberOnJur=null;
        if(userName.length()==12){
            //当前是学生
            StudentInfo studentInfo=(StudentInfo)obj;
            memberOnJur = roleJurisdictionCache.getMemberOnJur(studentInfo.getStudentid().toString(), 1);
            if(memberOnJur==null||memberOnJur.size()<1){
                //用户没有权限登录
                errorCache.put(TOKEN,errorCache.get(TOKEN)==null?1:errorCache.get(TOKEN)+1);
                return SystemResult.build(402,"当前您没有权限",errorCache.get(TOKEN));
            }
        }else{
            //当前是员工
            EmployeeInfo employeeInfo=(EmployeeInfo)obj;
            memberOnJur = roleJurisdictionCache.getMemberOnJur(employeeInfo.getEmployeeid().toString(), 2);
            if(memberOnJur==null||memberOnJur.size()<1){
                //用户没有权限登录
                errorCache.put(TOKEN,errorCache.get(TOKEN)==null?1:errorCache.get(TOKEN)+1);
                return SystemResult.build(402,"当前您没有权限",errorCache.get(TOKEN));
            }
        }
        //将当前用户权限信息存储到session中
        httpSession.setAttribute(TOKEN,memberOnJur);
        errorCache.put(TOKEN,0);
        return SystemResult.build(200,"用户登录成功",errorCache.get(TOKEN));
    }

    /**
     *  功能：判断当前用户名是否正确
     *  时间：2020/4/2 11:58
     *  参数：名称            类型                 描述
     *       userName      String               用户名
     *       password      String               用户密码
     *       mapCache      Map<Integer, Object> 用户缓存信息
     *  返回：Object
     *  描述：如果不为空,代表用对应的用户信息
     */
    private  Object  jugdeUserNameIsCorrect(String userName,String password,Map<Integer, Object> mapCache){
        Object obj=null;

        Map<String, StudentMgn> studentMgnMap= (Map<String, StudentMgn>)mapCache.get(1);            //学生拓展信息
        Map<String, StudentInfo> studentInfoMap= ( Map<String, StudentInfo>)mapCache.get(2);        //学生信息
        Map<String, EmployeeInfo> employeeInfoMap= (Map<String, EmployeeInfo>)mapCache.get(3);      //员工信息

        //判断当前是否是学生
        if(userName.length()==12){
            StudentMgn studentMgn = studentMgnMap.get(userName);

            //将当前用户输入的密码转换成MD5
            String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
            System.out.println(studentMgn.getPassword());
            System.out.println(md5Password);
            if(studentMgn!=null&&(studentMgn.getPassword().equals(md5Password))){
                obj=studentInfoMap.get(studentMgn.getStudentcode()); //说明用户名或密码错误
            }

        }
        else{
            //判断员工密码是否输入正确
            EmployeeInfo employeeInfo = employeeInfoMap.get(userName);
            if(employeeInfo!=null&&(employeeInfo.getPassword().equals(password))){
                obj=employeeInfo; //说明用户名或密码错误
            }
        }
        return obj;
    }
}
    
    
    