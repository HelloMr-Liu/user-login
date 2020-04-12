package com.king2.userlogin.cache;

import com.king2.userlogin.entity.EmployeeInfo;
import com.king2.userlogin.entity.StudentInfo;
import com.king2.userlogin.entity.StudentMgn;
import com.king2.userlogin.mapper.EmployeeInfoMapper;
import com.king2.userlogin.mapper.StudentInfoMapper;
import com.king2.userlogin.mapper.StudentMgnMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ================================================================
 * 说明：成员信息缓存
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/2  11:36            创建
 * =================================================================
 **/
@Component
@Data
public class MemberCache {

    @Autowired
    private StudentMgnMapper studentMgnMapper;
    @Autowired
    private StudentInfoMapper studentInfoMapper;
    @Autowired
    private EmployeeInfoMapper employeeInfoMapper;

    private Map<Integer,Object> mapCache;

    private MemberCache(){}


    //初始化当前缓存
    @PostConstruct
    private void initCache(){
        mapCache=new ConcurrentHashMap<>();
        refreshCache();
    }

    public void refreshCache(){
        mapCache.put(1,getStudentMgn());        //1代表 学生拓展信息
        mapCache.put(2,getStudentInfos());      //2代表 学生信息
        mapCache.put(3,getEmps());              //3代表 员工信息
    }


    /**
     *  功能：获取当前所以的学生拓展信息
     *  时间：2020/4/2 11:38
     *  参数：名称            类型            描述
     *  返回：Map<String,StudentMgn>
     *  描述：学生拓展信息map key 学号  value学生拓展信息
     */
    private Map<String,StudentMgn> getStudentMgn(){
        return studentMgnMapper.getStudentMgn().stream().collect(Collectors.toMap(
            e1->{return e1.getStudentcode();},
            e2->{return e2;}
        ));
    }

    /**
     *  功能：获取当前学生信息
     *  时间：2020/4/2 11:42
     *  参数：名称            类型            描述
     *  返回：Map<String, StudentInfo>
     *  描述：学生信息map key 学号  value学生信息
     */
    private Map<String, StudentInfo> getStudentInfos(){
        return studentInfoMapper.getStudents().stream().collect(
            Collectors.toMap(
                e1->{return e1.getStudentcode1();},
                e2->{return e2;}
            )
        );
    }

    /**
     *  功能：员工信息方法
     *  时间：2020/4/2 11:43
     *  参数：名称            类型            描述
     *  返回：Map<Integer,EmployeeInfo>
     *  描述：员工信息map key员工编号 value员工信息
     */
    private Map<String,EmployeeInfo> getEmps(){
        return employeeInfoMapper.getEmpls().stream().collect(
            Collectors.toMap(
                e1->{return e1.getEmployeeid().toString();},
                e2->{return e2;}
            )
        );
    }
}
    
    
    