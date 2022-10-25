package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.CustomerMapper;
import com.shy.bs.pojo.Customer;
import com.shy.bs.service.CustomerService;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.CustomerQuery;
import com.shy.bs.vo.ListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author night
 * @date 2022/10/21 20:34
 */
@Slf4j
@Transactional
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Override
    public ServerResponse getList(CustomerQuery customerQuery) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like(customerQuery.getId() != null, "id", customerQuery.getId())
                .like(customerQuery.getName() != null, "name", customerQuery.getName())
                .like(customerQuery.getIdCard() != null, "id_card", customerQuery.getIdCard())
                .like(customerQuery.getPhone() != null, "phone", customerQuery.getPhone());


        // 设置排序规则
        queryWrapper.orderByAsc("id");
        Page<Customer> pageParam = new Page<>(customerQuery.getPage(), customerQuery.getLimit());

        IPage<Customer> pageRs = baseMapper.selectPage(pageParam, queryWrapper);
        List<Customer> list = pageRs.getRecords();
        if (list != null) {
            ListVo listVo = new ListVo();
            listVo.setItems(list);
            listVo.setTotal(pageRs.getTotal());
            return ServerResponse.createBySuccess(listVo);
        }
        return ServerResponse.createByErrorMessage("获取客户列表失败");
    }

    @Override
    public ServerResponse updateCustomer(Customer customer) {
        UpdateWrapper<Customer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", customer.getId());
        int resultCount = baseMapper.update(customer, updateWrapper);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse addCustomer(Customer customer) {
        customer.setId(createCustomerId());
        int resultCount = baseMapper.insert(customer);
        if (resultCount != 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    @Override
    public ServerResponse getCustomer(String idCard) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_card", idCard);
        Customer customer = baseMapper.selectOne(queryWrapper);

        if (customer != null) {
            return ServerResponse.createBySuccess(customer);
        } else {
            return ServerResponse.createByErrorMessage("客户不存在");
        }
    }

    /**
     * 客户编号
     * 格式为：yyMMdd 加 五位递增的数字，数字每天重置为1
     */
    private Long createCustomerId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String format = dateFormat.format(new Date()) + "10000";
        return Long.parseLong(format) + (num++);
    }

    private int num = 1;

    @Scheduled(cron = "0 0 0 * * ?")
    private void clearNum() {
        num = 1;
    }
}
