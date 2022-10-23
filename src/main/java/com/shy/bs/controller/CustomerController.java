package com.shy.bs.controller;

import com.shy.bs.pojo.Customer;
import com.shy.bs.service.CustomerService;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.CustomerQuery;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author night
 * @date 2022/10/23 16:00
 */
@Api(tags = "顾客信息控制器")
@Slf4j
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @GetMapping("addCustomer")
    public ServerResponse addCustomer(Customer customer) {
        return customerService.addCustomer(customer);
    }

    @GetMapping("getList")
    public ServerResponse getList(CustomerQuery customerQuery) {
        return customerService.getList(customerQuery);
    }

    @PostMapping("update")
    public ServerResponse update(Customer customer) {
        return customerService.updateCustomer(customer);
    }
}
