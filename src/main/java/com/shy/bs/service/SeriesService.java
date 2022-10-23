package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.Series;
import com.shy.bs.util.ServerResponse;


/**
 * @author night
 * @date 2022/10/21 20:09
 */
public interface SeriesService extends IService<Series> {
    ServerResponse seriesOpt();
}
