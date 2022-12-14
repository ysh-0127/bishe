package com.shy.bs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shy.bs.pojo.Order;
import com.shy.bs.vo.EmpChart;
import com.shy.bs.vo.OrderList;
import com.shy.bs.vo.OrderQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author night
 * @description 针对表【order】的数据库操作Mapper
 * @createDate 2022-10-21 19:22:27
 * @Entity com.shy.bs.pojo.Order
 */
public interface OrderMapper extends BaseMapper<Order> {

    List<EmpChart> selectChartByDate(String date);

    List<EmpChart> selectYesterdayChart();


    List<OrderList> selectSale(OrderQuery orderQuery);

    int addTotalPriceByPrimaryKey(Long orderId, BigDecimal totalPrice);
}




