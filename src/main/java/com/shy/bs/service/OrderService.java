package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.Order;
import com.shy.bs.pojo.OrderDetails;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.DetailsQuery;
import com.shy.bs.vo.OrderQuery;
import com.shy.bs.vo.OrderVo;


/**
 * @author night
 * @date 2022/10/21 20:09
 */
public interface OrderService extends IService<Order> {
    ServerResponse getEmpChart(String date);

    ServerResponse getIndexChart();

    ServerResponse addOrder(OrderVo orderVo);


    ServerResponse getList(OrderQuery orderQuery);

    ServerResponse updateOrder(Long orderId, String status);

    ServerResponse updateDetail(OrderDetails orderDetails);

    ServerResponse deleteDetail(String id);

    ServerResponse getDetailsList(DetailsQuery detailsQuery);
}
