package com.fcl.ccmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.entity.DO.GetADListDO;
import com.fcl.ccmall.entity.dto.GetADListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdvertiseDao {
    Page<GetADListDO> getAdList(@Param("page") Page<GetADListDO> page,
                                @Param("dto") GetADListDTO getADListDTO);
}
