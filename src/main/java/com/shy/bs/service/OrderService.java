package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.Order;
import com.shy.bs.util.ServerResponse;


/**
 * @author night
 * @date 2022/10/21 20:09
 */
public interface OrderService extends IService<Order> {
    ServerResponse getEmpChart(String date);

    ServerResponse getIndexChart();
}
