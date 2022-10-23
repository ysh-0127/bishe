package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.CarMapper;
import com.shy.bs.pojo.Car;
import com.shy.bs.service.CarService;
import com.shy.bs.util.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author night
 * @date 2022/10/21 20:34
 */
@Slf4j
@Transactional
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
    @Override
    public ServerResponse storeOpt(Integer seriesId) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper();
        queryWrapper
                .eq(seriesId != null, "series_id", seriesId)
                .gt("repertory", 0);

        List<Car> list = baseMapper.selectList(queryWrapper);

        if (!CollectionUtils.isEmpty(list)) {
            return ServerResponse.createBySuccess(list);
        } else {
            return ServerResponse.createByError();
        }
    }
}
