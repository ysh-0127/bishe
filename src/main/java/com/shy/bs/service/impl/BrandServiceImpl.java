package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.BrandMapper;
import com.shy.bs.pojo.Brand;
import com.shy.bs.service.BrandService;
import com.shy.bs.util.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author night
 * @date 2022/10/21 20:34
 */
@Slf4j
@Transactional
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Override
    public ServerResponse brandOpt() {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper();
        queryWrapper
                .eq("status", 1);
        List<Brand> brandList = baseMapper.selectList(queryWrapper);

        if (!CollectionUtils.isEmpty(brandList)) {
            return ServerResponse.createBySuccess(brandList);
        } else {
            return ServerResponse.createByError();
        }
    }
}
