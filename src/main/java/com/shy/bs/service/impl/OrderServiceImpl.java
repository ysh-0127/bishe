package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.shy.bs.mapper.CarMapper;
import com.shy.bs.mapper.OrderDetailsMapper;
import com.shy.bs.mapper.OrderMapper;
import com.shy.bs.pojo.Car;
import com.shy.bs.pojo.Order;
import com.shy.bs.pojo.OrderDetails;
import com.shy.bs.service.OrderService;
import com.shy.bs.util.Const;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
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


    @Override
    @Transactional(rollbackFor = Exception.class)
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
            boolean    rs = updateById(order);
            if (!rs) {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateOrder(Long orderId, String status) {

        Order order = getById(orderId);
        order.setStatus(status);
        QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.ne("status", 1);
        boolean rs = update(order, queryWrapper1);
        if (!rs) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("更新订单失败");
        }
        if (status.equals(Const.Number.ONE)) {
            // status=1，支付订单，更新支付时间
            QueryWrapper<Order> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("status", 1);
            order.setPayTime(new Date());
            rs = update(order, queryWrapper2);
            if (!rs) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("更新订单失败");
            }
        } else if (status.equals(Const.Number.TWO)) {
            // status=2，取消订单，车辆回库
            QueryWrapper<OrderDetails> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("order_id", orderId);
            List<OrderDetails> details = detailsMapper.selectList(queryWrapper3);
            for (OrderDetails orderDetails : details) {
                int result = carMapper.addRepertoryByPrimaryKey(orderDetails.getCarId(), orderDetails.getCarNumber());
                if (result == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByErrorMessage("更新订单失败");
                }
            }
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateDetail(OrderDetails orderDetails) {
        OrderDetails oldDetails = detailsMapper.selectById(orderDetails.getId());
        int result = carMapper.addRepertoryByPrimaryKey(oldDetails.getCarId(), oldDetails.getCarNumber());
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("更新失败");
        }
        Car carRepertory = carMapper.selectById(orderDetails.getCarId());
        int repertory = carRepertory.getRepertory();
        int num = repertory - orderDetails.getCarNumber();
        if (num >= 0) {
            result = carMapper.updateRepertoryByid(orderDetails.getCarId(), num);
            if (result == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("添加订单失败");
            }
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("库存不足，添加订单失败");
        }
        result = detailsMapper.updateById(orderDetails);
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("更新失败");
        }
        BigDecimal oldPrice = carMapper.selectSalePriceByPrimaryKey(oldDetails.getCarId()).multiply(BigDecimal.valueOf(oldDetails.getCarNumber()));
        BigDecimal newPrice = carMapper.selectSalePriceByPrimaryKey(orderDetails.getCarId()).multiply(BigDecimal.valueOf(orderDetails.getCarNumber()));
        result = orderMapper.addTotalPriceByPrimaryKey(orderDetails.getOrderId(), newPrice.subtract(oldPrice));
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("更新失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteDetail(String id) {
        OrderDetails orderDetails = detailsMapper.selectById(id);
        BigDecimal totalPrice = carMapper.selectSalePriceByPrimaryKey(orderDetails.getCarId()).negate();
        int result = orderMapper.addTotalPriceByPrimaryKey(orderDetails.getOrderId(), totalPrice);
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("删除失败");
        }
        boolean rs = removeById(id);
        if (!rs) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("删除失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse getDetailsList(DetailsQuery detailsQuery) {
        List<DetailsList> list = PageHelper.startPage(detailsQuery.getPage(), detailsQuery.getLimit()).doSelectPage(() -> detailsMapper.selectSelective(detailsQuery));
        if (list != null) {
            ListVo listVo = new ListVo();
            listVo.setItems(list);
            listVo.setTotal(PageHelper.count(() -> detailsMapper.selectSelective(detailsQuery)));
            return ServerResponse.createBySuccess(listVo);
        }
        return ServerResponse.createByErrorMessage("获取订单详情列表失败");
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
     */
    private Long createOrderId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String format = dateFormat.format(new Date()) + "000000";
        return Long.parseLong(format) + (num++);
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
