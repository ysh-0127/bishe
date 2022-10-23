package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.OrderDetailsMapper;
import com.shy.bs.pojo.OrderDetails;
import com.shy.bs.service.OrderDetailsService;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.EmpChart;
import com.shy.bs.vo.IndexSales;
import com.shy.bs.vo.SalesChart;
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
public class OrderDetailsServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetails> implements OrderDetailsService {

    @Resource
    private OrderDetailsMapper detailsMapper;

    @Override
    public ServerResponse getSaleNum() {
        int num = detailsMapper.selectYesterdayNum();
        return ServerResponse.createBySuccess(num);
    }

    @Override
    public ServerResponse getSalesChart(String start, String end) {
        List<SalesChart> salesCharts = detailsMapper.selectSalesChart(start, end);
        if (salesCharts != null) {
            return ServerResponse.createBySuccess(salesCharts);
        }
        return ServerResponse.createByErrorMessage("木有数据哦");
    }

    @Override
    public ServerResponse getIndexSales(Integer id) {
        IndexSales indexSales = detailsMapper.selectIndexSales(id);
        if (indexSales != null) {
            return ServerResponse.createBySuccess(indexSales);
        }
        return ServerResponse.createByErrorMessage("木有数据哦");
    }

    @Override
    public ServerResponse getEmpSalesChart(Integer id, String date) {
        List<EmpChart> chartData = detailsMapper.selectEmpChart(id, date);
        if (chartData != null) {
            return ServerResponse.createBySuccess(chartData);
        }
        return ServerResponse.createByErrorMessage("木有数据哦");
    }
}
