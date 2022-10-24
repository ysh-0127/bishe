package com.shy.bs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shy.bs.pojo.Car;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author night
 * @description 针对表【car】的数据库操作Mapper
 * @createDate 2022-10-21 19:22:27
 * @Entity com.shy.bs.pojo.Car
 */
public interface CarMapper extends BaseMapper<Car> {

    int updateRepertoryByid(Long carId, @Param("carNumber") int num);

    int addRepertoryByPrimaryKey(Long carId, Integer carNumber);

    BigDecimal selectSalePriceByPrimaryKey(Long carId);
}




