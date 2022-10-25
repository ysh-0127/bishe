package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.Brand;
import com.shy.bs.util.ServerResponse;


/**
 * @author night
 * @date 2022/10/21 20:09
 */
public interface BrandService extends IService<Brand> {
    ServerResponse brandOpt();

    ServerResponse addBrand(String brand);

    ServerResponse delBrand(Integer brandId);
}
