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
            // æćšćæ»
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("æ·»ć èźąćć€±èŽ„");
        }
        if (orderVo.getStatus().equals(Const.Number.ONE)) {
            order.setPayTime(new Date());
            boolean  rs = updateById(order);
            if (!rs) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("æ·»ć èźąćć€±èŽ„");
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
                return ServerResponse.createByErrorMessage("æ·»ć èźąćć€±èŽ„");
            }
            Car carRepertory = carMapper.selectById(detailVo.getCarId());
            int repertory = carRepertory.getRepertory();
            num = repertory - num;
            if (num >= 0) {
                result = carMapper.updateRepertoryByid(carRepertory.getId(), num);
                if (result == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByErrorMessage("æ·»ć èźąćć€±èŽ„");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("ćșć­äžè¶łïŒæ·»ć èźąćć€±èŽ„");
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
        return ServerResponse.createByErrorMessage("è·ćèźąććèĄšć€±èŽ„");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateOrder(Long orderId, String status) {

        Order order = getById(orderId);
        order.setStatus(status);
        QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.ne("status", 1).eq("id",orderId);
        boolean rs = update(order, queryWrapper1);
        if (!rs) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("æŽæ°èźąćć€±èŽ„");
        }
        if (status.equals(Const.Number.ONE)) {
            // status=1ïŒæŻä»èźąćïŒæŽæ°æŻä»æ¶éŽ
            QueryWrapper<Order> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("status", 1).eq("id",orderId);
            order.setPayTime(new Date());
            order.setUpdateTime(new Date());
            rs = update(order, queryWrapper2);
            if (!rs) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("æŽæ°èźąćć€±èŽ„");
            }
        } else if (status.equals(Const.Number.TWO)) {
            // status=2ïŒćæ¶èźąćïŒèœŠèŸććș
            Order updateTime =getById(orderId);
            updateTime.setUpdateTime(new Date());
            updateById(updateTime);
            QueryWrapper<OrderDetails> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("order_id", orderId);
            List<OrderDetails> details = detailsMapper.selectList(queryWrapper3);
            for (OrderDetails orderDetails : details) {
                int result = carMapper.addRepertoryByPrimaryKey(orderDetails.getCarId(), orderDetails.getCarNumber());
                if (result == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByErrorMessage("æŽæ°èźąćć€±èŽ„");
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
            return ServerResponse.createByErrorMessage("æŽæ°ć€±èŽ„");
        }
        Car carRepertory = carMapper.selectById(orderDetails.getCarId());
        int repertory = carRepertory.getRepertory();
        int num = repertory - orderDetails.getCarNumber();
        if (num >= 0) {
            result = carMapper.updateRepertoryByid(orderDetails.getCarId(), num);
            if (result == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByErrorMessage("æ·»ć èźąćć€±èŽ„");
            }
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("ćșć­äžè¶łïŒæ·»ć èźąćć€±èŽ„");
        }
        result = detailsMapper.updateById(orderDetails);
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("æŽæ°ć€±èŽ„");
        }
        BigDecimal oldPrice = carMapper.selectSalePriceByPrimaryKey(oldDetails.getCarId()).multiply(BigDecimal.valueOf(oldDetails.getCarNumber()));
        BigDecimal newPrice = carMapper.selectSalePriceByPrimaryKey(orderDetails.getCarId()).multiply(BigDecimal.valueOf(orderDetails.getCarNumber()));
        result = orderMapper.addTotalPriceByPrimaryKey(orderDetails.getOrderId(), newPrice.subtract(oldPrice));
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("æŽæ°ć€±èŽ„");
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
            return ServerResponse.createByErrorMessage("ć é€ć€±èŽ„");
        }
        boolean rs = removeById(id);
        if (!rs) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("ć é€ć€±èŽ„");
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
        return ServerResponse.createByErrorMessage("è·ćèźąćèŻŠæćèĄšć€±èŽ„");
    }


    //ćŸèĄš
    @Override
    public ServerResponse getEmpChart(String date) {
        List<EmpChart> chartData = orderMapper.selectChartByDate(date);
        if (chartData != null) {
            return ServerResponse.createBySuccess(chartData);
        }
        return ServerResponse.createByErrorMessage("æČĄææ°æźćŠ");
    }

    @Override
    public ServerResponse getIndexChart() {
        List<EmpChart> chartData = orderMapper.selectYesterdayChart();
        if (chartData != null) {
            return ServerResponse.createBySuccess(chartData);
        }
        return ServerResponse.createByErrorMessage("æČĄææ°æźćŠ");
    }


    /**
     * èźąćçŒć·
     * æ ŒćŒäžșïŒyyMMdd ć 6äœéćąçæ°ć­ïŒæ°ć­æŻć€©éçœźäžș1
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
        // 0 ä»ŁèĄšćéąèĄ„ć0
        // 4 ä»ŁèĄšéżćșŠäžș4
        // d ä»ŁèĄšćæ°äžșæ­Łæ°ć
        return first + String.format("%015d", hashCodeV);
    }
}
