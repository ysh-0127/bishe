package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.Customer;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.CustomerQuery;


/**
 * @author night
 * @date 2022/10/21 20:09
 */
public interface CustomerService extends IService<Customer> {
    ServerResponse getList(CustomerQuery customerQuery);

    ServerResponse updateCustomer(Customer customer);

    ServerResponse addCustomer(Customer customer);

    ServerResponse getCustomer(String idCard);
}
