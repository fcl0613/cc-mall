package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.dao.PersonaDao;
import com.fcl.ccmall.entity.DO.PersonaAnalysisDO;
import com.fcl.ccmall.entity.vo.PersonaAnalysisVO;
import com.fcl.ccmall.enums.AgeAnalysisEnum;
import com.fcl.ccmall.enums.CarAnalysisEnum;
import com.fcl.ccmall.enums.GenderAnalysisEnum;
import com.fcl.ccmall.enums.HomeAnalysisEnum;
import com.fcl.ccmall.enums.IncomeAnalysisEnum;
import com.fcl.ccmall.service.DataAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
@Slf4j
public class DataAnalysisImpl implements DataAnalysis {

    @Value("${PERSONA_ANALYSIS_REDIS_PRE}")
    private String PERSONA_ANALYSIS_REDIS_PRE;

    @Resource
    private PersonaDao personaDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public CommonResult getPersonaAnalysis() {
        String yyyy_mm = DateUtil.format(DateUtil.date(), "yyyy_MM");
        String tableName = "persona_" + yyyy_mm;
        // 判断表是否生成
        Integer count = personaDao.selectTableCount(tableName);
        if (count == 0) {
            return CommonResult.success(null, "暂无数据");
        }else {
            // 从redis中拿去数据
            PersonaAnalysisVO o = (PersonaAnalysisVO)redisTemplate.opsForValue()
                    .get(PERSONA_ANALYSIS_REDIS_PRE + yyyy_mm);
            if (Objects.isNull(o)) {
                return CommonResult.success(null, "暂无数据");
            }
            return CommonResult.success(o);
        }
    }

    @Override
    @Async
    public void personaAnalysis() {
        log.info("开始解析用户画像");
        String yyyy_mm = DateUtil.format(DateUtil.date(), "yyyy_MM");
        String tableName = "persona_" + yyyy_mm;
        List<PersonaAnalysisDO> analysisGender = personaDao.analysisGender(tableName);
        for (GenderAnalysisEnum value : GenderAnalysisEnum.values()) {
            handleData(value.getType(), analysisGender);
        }
        List<PersonaAnalysisDO> analysisAge = personaDao.analysisAge(tableName);
        for (AgeAnalysisEnum value : AgeAnalysisEnum.values()) {
            handleData(value.getType(), analysisAge);
        }
        List<PersonaAnalysisDO> analysisCareer = personaDao.analysisCareer(tableName);
        List<PersonaAnalysisDO> analysisIncome = personaDao.analysisIncome(tableName);
        for (IncomeAnalysisEnum value : IncomeAnalysisEnum.values()) {
            handleData(value.getType(), analysisIncome);
        }
        List<PersonaAnalysisDO> analysisHasCar = personaDao.analysisHasCar(tableName);
        for (CarAnalysisEnum value : CarAnalysisEnum.values()) {
            handleData(value.getType(), analysisHasCar);
        }
        List<PersonaAnalysisDO> analysisHasHome = personaDao.analysisHasHome(tableName);
        for (HomeAnalysisEnum value : HomeAnalysisEnum.values()) {
            handleData(value.getType(), analysisHasHome);
        }
        List<PersonaAnalysisDO> analysisEducationalLevel = personaDao.analysisEducationalLevel(tableName);
        List<PersonaAnalysisDO> analysisCountryOfOrigin = personaDao.analysisCountryOfOrigin(tableName);
        PersonaAnalysisVO personaAnalysisVO = new PersonaAnalysisVO();
        personaAnalysisVO.setAgeDimension(analysisAge);
        personaAnalysisVO.setCareerDimension(analysisCareer);
        personaAnalysisVO.setCountryOfOriginDimension(analysisCountryOfOrigin);
        personaAnalysisVO.setEducationalLevelDimension(analysisEducationalLevel);
        personaAnalysisVO.setGenderDimension(analysisGender);
        personaAnalysisVO.setHasCarDimension(analysisHasCar);
        personaAnalysisVO.setHasHomeDimension(analysisHasHome);
        personaAnalysisVO.setIncomeDimension(analysisIncome);
        redisTemplate.opsForValue().set(PERSONA_ANALYSIS_REDIS_PRE + yyyy_mm, personaAnalysisVO, 30, TimeUnit.DAYS);
    }

    private void handleData(String type, List<PersonaAnalysisDO> list) {
        boolean flag = false;
        for (PersonaAnalysisDO personaAnalysisDO : list) {
            if (type.equals(personaAnalysisDO.getTag())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            PersonaAnalysisDO personaAnalysisDO = new PersonaAnalysisDO();
            personaAnalysisDO.setCount(0);
            personaAnalysisDO.setTag(type);
            list.add(personaAnalysisDO);
        }
    }
}
