package com.king2.userlogin.cache;

import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ================================================================
 * 说明：连续登录错误缓存
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/2  12:17            创建
 * =================================================================
 **/
@Component
@Data
public class LoginErrorCache {

    //创建记录每天的请求连续登录次数
    private Map<String,Integer> errorCache=new ConcurrentHashMap<>();

    private LoginErrorCache(){
    }

    //创建一个定时任务每天晚上的23:59:59后将当前所有的用户登录次数
    @Scheduled(cron = "59 59 23 ? * *")
    private void clearCache(){
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"一共有"+errorCache.size()+"唯一标识");
        errorCache.clear();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"清除后有"+errorCache.size()+"唯一标识");
    }

}
    
    
    