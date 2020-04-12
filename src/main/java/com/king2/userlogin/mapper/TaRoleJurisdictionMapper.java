package com.king2.userlogin.mapper;


import com.king2.userlogin.entity.TaRoleJurisdiction;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TaRoleJurisdictionMapper {
    int deleteByPrimaryKey(Integer roleJurisdictionId);

    int insert(TaRoleJurisdiction record);

    int insertSelective(TaRoleJurisdiction record);

    TaRoleJurisdiction selectByPrimaryKey(Integer roleJurisdictionId);

    int updateByPrimaryKeySelective(TaRoleJurisdiction record);

    int updateByPrimaryKey(TaRoleJurisdiction record);

    @Select("select * from ta_role_jurisdiction")
    List<TaRoleJurisdiction> getRoleJurs();
}