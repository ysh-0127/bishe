package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.shy.bs.mapper.CarMapper;
import com.shy.bs.pojo.Car;
import com.shy.bs.service.CarService;
import com.shy.bs.util.Const;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.ListVo;
import com.shy.bs.vo.StoreList;
import com.shy.bs.vo.StoreQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
    @Resource
    private CarMapper carMapper;
    @Override
    public ServerResponse storeOpt(Integer seriesId) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(seriesId != null, "series_id", seriesId)
                .gt("repertory", 0);

        List<Car> list = baseMapper.selectList(queryWrapper);

        if (!CollectionUtils.isEmpty(list)) {
            return ServerResponse.createBySuccess(list);
        } else {
            return ServerResponse.createByError();
        }
    }

    @Override
    public ServerResponse addStore(Car car) {
        car.setId(createCarId());
        car.setStatus(Const.Number.ONE);
        boolean resultCount = save(car);
        if (resultCount) {
            return ServerResponse.createBySuccess();
        }
        log.error(car.toString());
        return ServerResponse.createByErrorMessage("添加失败");
    }

    @Override
    public ServerResponse getList(StoreQuery storeQuery) {
        List<StoreList> list = PageHelper.startPage(storeQuery.getPage(), storeQuery.getLimit()).doSelectPage(()-> carMapper.selectSelective(storeQuery));
        if (list != null) {
            ListVo listVo = new ListVo();
            listVo.setItems(list);
            listVo.setTotal(PageHelper.count(()->carMapper.selectSelective(storeQuery)));
            return ServerResponse.createBySuccess(listVo);
        }
        return ServerResponse.createByErrorMessage("获取库存列表失败");
    }

    @Override
    public ServerResponse updateStore(Car car) {
        log.info(car.toString());
        boolean resultCount = updateById(car);
        if (resultCount) {
            return ServerResponse.createBySuccess();
        }
        log.error(car.toString());
        return ServerResponse.createByErrorMessage("更新失败");
    }

    /**
     * 车辆编号
     * 格式为：yyMMdd 加 五位递增的数字，数字每天重置为1
     * @return
     */
    private Long createCarId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String format = dateFormat.format(new Date()) + "00000";
        return Long.parseLong(format) + (num++);
    }

    private int num = 1;

    @Scheduled(cron="0 0 0 * * ?")
    private void clearNum() {
        num = 1;
    }
}
