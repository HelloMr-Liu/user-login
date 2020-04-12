package com.king2.userlogin.cache;

import com.king2.userlogin.entity.*;
import com.king2.userlogin.mapper.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ================================================================
 * 说明：角色权限缓存
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/2  10:41            创建
 * =================================================================
 **/
@Component
@Data
public class RoleJurisdictionCache {

    @Autowired
    private TaModelMapper taModelMapper;

    @Autowired
    private TaJurisdictionListMapper taJurisdictionListMapper;

    @Autowired
    private TaRoleJurisdictionMapper taRoleJurisdictionMapper;

    @Autowired
    private TaRoleMemberMapper taRoleMemberMapper;

    @Autowired
    private TaRoleInfoMapper taRoleInfoMapper;

    //创建一个缓存容器
    private Map<Integer,Object> cacheMap;

    private RoleJurisdictionCache(){}


    //创建一个初始化缓存信息方法
    @PostConstruct
    private void initCache(){
        cacheMap=new ConcurrentHashMap<>();
        refreshCache();
    }

    //创建一个刷新当前缓存信息
    public void refreshCache(){
        cacheMap.put(1,getModels());        //1 代表存储模块信息
        cacheMap.put(2,getJurs());          //2 代表权限信息
        cacheMap.put(3,getRoleJurs());      //3 代表角色权限信息
        cacheMap.put(4,getRoleMembers());   //4 代表角色成员信息
        cacheMap.put(5,getRoles());         //5 代表角色信息
    }

    //获取模块信息
    private Map<String, TaModel> getModels(){
        return taModelMapper.getModels().stream().collect(Collectors.toMap(
            e1->{return e1.getModelId();},
            e2->{return e2;}
        ));
    }

    //获取权限信息
    private Map<Integer,TaJurisdictionList> getJurs(){
        return taJurisdictionListMapper.getJurList().stream().collect(Collectors.toMap(
            e1->{return e1.getJurisdictionListId();},
            e2->{return e2;}
        ));
    }
    //获取角色权限信息
    private List<TaRoleJurisdiction> getRoleJurs(){
        return taRoleJurisdictionMapper.getRoleJurs();
    }

    //获取角色成员信息
    private List<TaRoleMember> getRoleMembers(){
        return taRoleMemberMapper.getRoleMember();
    }
    //获取角色信息
    private List<TaRoleInfo> getRoles(){
        return  taRoleInfoMapper.getRoleInfos();
    }

    /**
     *  功能：获取当前成员对应的权限信息
     *  时间：2020/4/2 11:04
     *  参数：名称            类型            描述
     *       memberId      String          成员id
     *       flag          Integer         成员标志 1=学生  2=员工
     *
     *  返回：List<TaJurisdictionList>
     *  描述：返回封装好后对应的成员权限信息
     */
    public List<TaJurisdictionList> getMemberOnJur(String memberId,Integer flag){
        String flagStr=flag==1?"学":"员";
        //获取当前成员对应的角色信息
        List<TaRoleMember> roleMembers=((ArrayList<TaRoleMember>) cacheMap.get(4)).stream()
            .filter(
                e->{
                    return e.getMemberId().startsWith(flagStr)&&e.getMemberId().endsWith(memberId);
                }
        ).collect(Collectors.toList());
        if(roleMembers.size()<1)return null;


        //获取对应的角色权限信息
        List<TaRoleJurisdiction> roleJurs= ((ArrayList<TaRoleJurisdiction>)cacheMap.get(3)).stream()
            .filter(
                e->{
                    boolean currFlag=false;
                    for(TaRoleMember roleMember:roleMembers){
                        if(e.getRoleInfoId()==roleMember.getRoleInfoId()){currFlag=true;break;}
                    }
                    return currFlag;
                }
            )
        .collect(Collectors.toList());
        if(roleJurs.size()<1)return null;


        //获取当前成员拥有的权限信息a
        Map<Integer,TaJurisdictionList> jurMap= ((Map<Integer,TaJurisdictionList>)cacheMap.get(2));

        List<TaJurisdictionList> jurs=jurMap.entrySet().stream()
        .filter(e->{
            boolean currFlag=false;
            for(TaRoleJurisdiction jur:roleJurs){
                if(
                    jur.getJurisdictionListId()==e.getValue().getJurisdictionListId()||
                    jurMap.get(jur.getJurisdictionListId()).getParentId().equals(e.getValue().getModelId())
                ){currFlag=true;break;}
            }
            return currFlag;
        })
        .sorted(
            (e1,e2)->{
                return e1.getValue().getModelId().hashCode()-e2.getValue().getModelId().hashCode();
            }
        )
        .map(e->{
            return e.getValue();
        })
        .collect(Collectors.toList());
        return jurs;
    }
}
    
    
    