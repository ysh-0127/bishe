package com.shy.bs.controller;

import com.shy.bs.service.OrderDetailsService;
import com.shy.bs.service.OrderService;
import com.shy.bs.util.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author night
 * @date 2022/10/22 16:45
 */
@Api(tags = "图表控制器")
@Slf4j
@RestController
@RequestMapping("chart")
public class ChartController {
    @Resource
    private OrderDetailsService detailsService;
    @Resource
    private OrderService orderService;


    @ApiOperation("获取全部员工的月销量报表数据")
    @GetMapping("getEmpChart")
    public ServerResponse getEmpChart(String date) {
        return orderService.getEmpChart(date);
    }

    @ApiOperation("获取经理主页昨日销量报表数据")
    @GetMapping("getIndexChart")
    public ServerResponse getIndexChart() {
        return orderService.getIndexChart();
    }

    @ApiOperation("获取经理主页昨日销量")
    @GetMapping("getSaleNum")
    public ServerResponse getSaleNum() {
        return detailsService.getSaleNum();
    }

    @ApiOperation("获取销售报表数据")
    @GetMapping("getSalesChart")
    public ServerResponse getSalesChart(String start, String end) {
        return detailsService.getSalesChart(start, end);
    }

    @ApiOperation("获取员工主页本月销售额数据")
    @GetMapping("getIndexSales")
    public ServerResponse getIndexSales(Integer id) {
        return detailsService.getIndexSales(id);
    }

    @ApiOperation("获取员工销售额数据")
    @GetMapping("getEmpSalesChart")
    public ServerResponse getEmpSalesChart(Integer id, String date) {
        return detailsService.getEmpSalesChart(id, date);
    }

}
