package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.OrderDetails;
import com.shy.bs.util.ServerResponse;


/**
 * @author night
 * @date 2022/10/21 20:09EmployeeService
 */
public interface OrderDetailsService extends IService<OrderDetails> {
    ServerResponse getSaleNum();

    ServerResponse getSalesChart(String start, String end);

    ServerResponse getIndexSales(Integer id);

    ServerResponse getEmpSalesChart(Integer id, String date);
}
