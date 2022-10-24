package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.shy.bs.mapper.CarMapper;
import com.shy.bs.mapper.OrderDetailsMapper;
import com.shy.bs.mapper.OrderMapper;
import com.shy.bs.pojo.Car;
import com.shy.bs.pojo.Order;
import com.shy.bs.pojo.OrderDetails;
import com.shy.bs.service.CustomerService;
import com.shy.bs.service.OrderService;
import com.shy.bs.util.Const;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
    @Resource
    private OrderDetailsMapper detailsMapper;
    @Resource
    private CarMapper carMapper;
    @Autowired
    private CustomerService customerService;
    @Override
    public ServerResponse addOrder(OrderVo orderVo) {
        Order order = new Order();
        Long orderId = createOrderId();
        System.out.println(createOrderId());
        order.setId(orderId);
        order.setCustomerId(orderVo.getCustomerId());
        order.setEmployeeId(orderVo.getEmployeeId());
        order.setStatus(orderVo.getStatus());
        order.setTotalPrice(orderVo.getTotalPrice());
        int result = baseMapper.insert(order);
        if (result == 0) {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("添加订单失败");
        }
        if (orderVo.getStatus().equals(Const.Number.ONE)) {
            result = orderMapper.updateById(order);
            if (result == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("添加订单失败");
            }
        }
        for (OrderDetailVo detailVo : orderVo.getDetailVos()) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setId(createOrderDetailsId());
            orderDetails.setCarId(detailVo.getCarId());
            int num = detailVo.getCarNumber();
            orderDetails.setCarNumber(num);
            orderDetails.setOrderId(orderId);
            result = detailsMapper.insert(orderDetails);
            if (result == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("添加订单失败");
            }
            Car carRepertory = carMapper.selectById(detailVo.getCarId());
            int repertory = carRepertory.getRepertory();
            num = repertory - num;
            if (num >= 0) {
                //
                result = carMapper.updateRepertoryByid(carRepertory.getId(), num);
                if (result == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByErrorMessage("添加订单失败");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("库存不足，添加订单失败");
            }
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse getList(OrderQuery orderQuery) {
        List<OrderList> list = PageHelper.startPage(orderQuery.getPage(), orderQuery.getLimit()).doSelectPage(() -> orderMapper.selectSale(orderQuery));
        if (list != null) {
            for (OrderList orderList : list) {
                List<Details> details = detailsMapper.selectDetailsByOrderId(orderList.getOrderId());
                orderList.setDetails(details);
            }
            ListVo listVo = new ListVo();
            listVo.setItems(list);
            listVo.setTotal(PageHelper.count(() -> orderMapper.selectSale(orderQuery)));
            return ServerResponse.createBySuccess(listVo);
        }
        return ServerResponse.createByErrorMessage("获取订单列表失败");
    }


    //图表
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
        return ServerResponse.createByErrorMessage("没有数据哦");
    }


    /**
     * 订单编号
     * 格式为：yyMMdd 加6位递增的数字，数字每天重置为1
     * @return
     */
    private Long createOrderId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String format = dateFormat.format(new Date()) + "000000";
        return Long.valueOf(format) + (num++);
    }


    private int num = 1;

    @Scheduled(cron = "0 0 0 * * ?")
    private void clearNum() {
        num = 1;
    }

    private String createOrderDetailsId() {
        int first = new Random(10).nextInt(8) + 1;
        System.out.println(first);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return first + String.format("%015d", hashCodeV);
    }
}
