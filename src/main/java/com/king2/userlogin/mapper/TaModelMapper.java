package com.king2.userlogin.mapper;


import com.king2.userlogin.entity.TaModel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TaModelMapper {
    int deleteByPrimaryKey(String modelId);

    int insert(TaModel record);

    int insertSelective(TaModel record);

    TaModel selectByPrimaryKey(String modelId);

    int updateByPrimaryKeySelective(TaModel record);

    int updateByPrimaryKey(TaModel record);

    @Select("select * from  ta_model")
    List<TaModel> getModels();
}