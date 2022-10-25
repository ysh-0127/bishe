package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.BrandMapper;
import com.shy.bs.pojo.Brand;
import com.shy.bs.pojo.Series;
import com.shy.bs.service.BrandService;
import com.shy.bs.service.SeriesService;
import com.shy.bs.util.Const;
import com.shy.bs.util.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author night
 * @date 2022/10/21 20:34
 */
@Slf4j
@Transactional
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Resource
    private SeriesService seriesService;
    @Override
    public ServerResponse brandOpt() {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("status", 1);
        List<Brand> brandList = list(queryWrapper);

        if (!CollectionUtils.isEmpty(brandList)) {
            return ServerResponse.createBySuccess(brandList);
        } else {
            return ServerResponse.createByError();
        }
    }

    @Override
    public ServerResponse addBrand(String brandName) {
        // 验证brandName是否存在
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_name", brandName);
        Brand result = getOne(queryWrapper);
        if (result != null) {
            if (result.getStatus().equals(Const.Number.ONE)) {
                return ServerResponse.createByErrorMessage(brandName + "已存在");
            } else if (result.getStatus().equals(Const.Number.ZERO)) {
                // brand已存在，但处于删除状态
                result.setStatus(Const.Number.ONE);
                boolean updateResult = updateById(result);
                if (updateResult) {
                    return ServerResponse.createBySuccess();
                }
            }
        }
        Brand brand = new Brand();
        brand.setBrandName(brandName);
        boolean resultCount = save(brand);
        if (resultCount) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    @Override
    public ServerResponse delBrand(Integer brandId) {
        QueryWrapper<Series> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("brand_id",brandId).eq("status",1);
        List <Series> seriesList = seriesService.list(queryWrapper);
        if (CollectionUtils.isEmpty(seriesList)) {
            boolean result = removeById(brandId);
            if (result) {
                return ServerResponse.createBySuccess();
            }
        }else {
            // 品牌有对应车系，把该品牌status置为0
            Brand brand = new Brand();
            brand.setBrandId(brandId);
            brand.setStatus(Const.Number.ZERO);
            boolean result = updateById(brand);
            if (result) {
                return ServerResponse.createBySuccess();
            }
        }

        return ServerResponse.createByErrorMessage("未知错误");
    }
}
