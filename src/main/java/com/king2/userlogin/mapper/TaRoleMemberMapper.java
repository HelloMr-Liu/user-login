package com.king2.userlogin.mapper;


import com.king2.userlogin.entity.TaRoleMember;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TaRoleMemberMapper {
    int deleteByPrimaryKey(Integer roleMemberId);

    int insert(TaRoleMember record);

    int insertSelective(TaRoleMember record);

    TaRoleMember selectByPrimaryKey(Integer roleMemberId);

    int updateByPrimaryKeySelective(TaRoleMember record);

    int updateByPrimaryKey(TaRoleMember record);

    @Select("select * from ta_role_member")
    List<TaRoleMember> getRoleMember();
}