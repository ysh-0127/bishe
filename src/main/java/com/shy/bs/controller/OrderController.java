package com.shy.bs.controller;

import com.shy.bs.pojo.OrderDetails;
import com.shy.bs.service.OrderService;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.DetailsQuery;
import com.shy.bs.vo.OrderQuery;
import com.shy.bs.vo.OrderVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author night
 * @date 2022/10/23 16:29
 */
@Api(tags = "订单控制器")
@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("addOrder")
    public ServerResponse addOrder(@RequestBody OrderVo orderVo) {
        return orderService.addOrder(orderVo);
    }

    @GetMapping("getList")
    public ServerResponse getList(OrderQuery orderQuery) {
        return orderService.getList(orderQuery);
    }

    @PostMapping("update")
    public ServerResponse update(Long orderId, String status) {
        return orderService.updateOrder(orderId, status);
    }

    @PostMapping(value = "updateDetail")
    public ServerResponse updateDetail(OrderDetails orderDetails) {
        return orderService.updateDetail(orderDetails);
    }

    @RequestMapping(value = "deleteDetail", method = RequestMethod.POST)
    public ServerResponse deleteDetail(String id) {
        return orderService.deleteDetail(id);
    }

    @RequestMapping(value = "getDetailsList", method = RequestMethod.GET)
    public ServerResponse getDetailsList(DetailsQuery detailsQuery) {
        return orderService.getDetailsList(detailsQuery);
    }
}
