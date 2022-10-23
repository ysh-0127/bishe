package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.Car;
import com.shy.bs.util.ServerResponse;


/**
 * @author night
 * @date 2022/10/21 20:09
 */
public interface CarService extends IService<Car> {
    ServerResponse storeOpt(Integer seriesId);
}
