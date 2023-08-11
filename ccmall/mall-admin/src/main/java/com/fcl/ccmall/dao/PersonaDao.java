package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fcl.ccmall.entity.DO.PersonaAnalysisDO;
import com.fcl.ccmall.entity.excelmodel.Persona;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonaDao extends BaseMapper<Persona> {
    void batchInsert(@Param("list") List<Persona> list,
                     @Param("tableName") String tableName);
    void creatTable(@Param("tableName") String tableName);
    Integer getCount(@Param("tableName") String tableName);
    List<Persona> getList(@Param("page") Integer page,
                          @Param("limit") Integer limit,
                          @Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisGender(@Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisAge(@Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisCareer(@Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisIncome(@Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisHasCar(@Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisHasHome(@Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisEducationalLevel(@Param("tableName") String tableName);
    List<PersonaAnalysisDO> analysisCountryOfOrigin(@Param("tableName") String tableName);
    Integer selectTableCount(@Param("tableName") String tableName);
}
