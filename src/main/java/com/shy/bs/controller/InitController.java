package com.shy.bs.controller;

import com.shy.bs.service.BrandService;
import com.shy.bs.service.CarService;
import com.shy.bs.service.CustomerService;
import com.shy.bs.service.SeriesService;
import com.shy.bs.util.ServerResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author night
 * @date 2022/10/23 17:04
 */
@Api(tags = "初始化控制器")
@Slf4j
@RestController
@RequestMapping("init")
public class InitController {
    @Resource
    private CustomerService customerService;
    @Resource
    private SeriesService seriesService;
    @Resource
    private BrandService brandService;
    @Resource
    private CarService carService;

    @RequestMapping(value = "seriesOpt", method = RequestMethod.GET)
    public ServerResponse seriesOpt() {
        return seriesService.seriesOpt();
    }

    @RequestMapping(value = "brandOpt", method = RequestMethod.GET)
    public ServerResponse brandOpt() {
        return brandService.brandOpt();
    }

    @RequestMapping(value = "storeOpt", method = RequestMethod.GET)
    public ServerResponse storeOpt(Integer seriesId) {
        return carService.storeOpt(seriesId);
    }

    @RequestMapping(value = "getCustomer", method = RequestMethod.GET)
    public ServerResponse getCustomer(String idCard) {
        return customerService.getCustomer(idCard);
    }
}
