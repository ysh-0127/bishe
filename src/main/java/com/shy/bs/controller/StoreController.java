package com.shy.bs.controller;

import com.shy.bs.pojo.Car;
import com.shy.bs.service.BrandService;
import com.shy.bs.service.CarService;
import com.shy.bs.service.SeriesService;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.StoreQuery;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author night
 * @date 2022/10/24 15:18
 */
@Api(tags = "商店控制器")
@Slf4j
@RestController
@RequestMapping("store")
public class StoreController {
    @Resource
    private BrandService brandService;
    @Resource
    private SeriesService seriesService;
    @Resource
    private CarService carService;
    @GetMapping("addBrand")
    public ServerResponse addBrand(String brand) {
        return brandService.addBrand(brand);
    }

    @GetMapping("delBrand")
    public ServerResponse delBrand(Integer brandId) {
        return brandService.delBrand(brandId);
    }

    @GetMapping("addSeries")
    public ServerResponse addSeries(Integer brandId, String seriesName) {
        return seriesService.addSeries(brandId, seriesName);
    }

    @GetMapping("delSeries")
    public ServerResponse delSeries(Integer seriesId) {
        return seriesService.delSeries(seriesId);
    }

    @PostMapping("addStore")
    public ServerResponse addStore(Car car) {
        return carService.addStore(car);
    }

    @GetMapping("getList")
    public ServerResponse getList(StoreQuery storeQuery) {
        return carService.getList(storeQuery);
    }

    @PostMapping("update")
    public ServerResponse update(Car car) {
        return carService.updateStore(car);
    }
}
