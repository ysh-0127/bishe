package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.OrderMapper;
import com.shy.bs.pojo.Order;
import com.shy.bs.service.OrderService;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.EmpChart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author night
 * @date 2022/10/21 20:34
 */
@Slf4j
@Transactional
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public ServerResponse getEmpChart(String date) {
        List<EmpChart> chartData = orderMapper.selectChartByDate(date);
        if (chartData != null) {
            return ServerResponse.createBySuccess(chartData);
        }
        return ServerResponse.createByErrorMessage("没有数据哦");
    }

    @Override
    public ServerResponse getIndexChart() {
        List<EmpChart> chartData = orderMapper.selectYesterdayChart();
        if (chartData != null) {
            return ServerResponse.createBySuccess(chartData);
        }
        return ServerResponse.createByErrorMessage("mei有数据哦");
    }
}
