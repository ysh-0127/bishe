package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.SeriesMapper;
import com.shy.bs.pojo.Brand;
import com.shy.bs.pojo.Series;
import com.shy.bs.service.BrandService;
import com.shy.bs.service.SeriesService;
import com.shy.bs.util.Const;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.SeriesTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author night
 * @date 2022/10/21 20:34
 */
@Slf4j
@Transactional
@Service
public class SeriesServiceImpl extends ServiceImpl<SeriesMapper, Series> implements SeriesService {
    @Resource
    private BrandService brandService;

    @Override
    public ServerResponse seriesOpt() {
        QueryWrapper<Brand> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("status", 1);
        List<Brand> brandList = brandService.list(queryWrapper1);

        if (!CollectionUtils.isEmpty(brandList)) {
            List<SeriesTree> treeList = new ArrayList<>();
            for (Brand brandItem : brandList) {
                SeriesTree tree = new SeriesTree();
                tree.setValue(brandItem.getBrandId());
                tree.setLabel(brandItem.getBrandName());
                QueryWrapper<Series> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2
                        .eq("status", 1)
                        .eq("brand_id", brandItem.getBrandId());
                List<Series> seriesList = list(queryWrapper2);
                if (!CollectionUtils.isEmpty(seriesList)) {
                    List<SeriesTree> seriesTreeList = new ArrayList<>();
                    for (Series series : seriesList) {
                        SeriesTree seriesTree = new SeriesTree();
                        seriesTree.setValue(series.getSeriesId());
                        seriesTree.setLabel(series.getSeriesName());
                        seriesTreeList.add(seriesTree);
                    }
                    tree.setChildren(seriesTreeList);
                    treeList.add(tree);
                }
            }
            return ServerResponse.createBySuccess(treeList);
        }
        return ServerResponse.createByErrorMessage("未知错误，请联系管理员");
    }

    @Override
    public ServerResponse addSeries(Integer brandId, String seriesName) {
        QueryWrapper<Series> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id",brandId).eq("series_name",seriesName);
        Series result = getOne(queryWrapper);
        if (result != null) {
            if (result.getStatus().equals(Const.Number.ONE)) {
                return ServerResponse.createByErrorMessage(seriesName + "已存在");
            } else if (result.getStatus().equals(Const.Number.ZERO)) {
                // series已存在，但处于删除状态
                Series series = new Series();
                series.setSeriesId(result.getSeriesId());
                series.setStatus((Const.Number.ONE));
                boolean updateResult = updateById(series);
                if (updateResult) {
                    return ServerResponse.createBySuccess();
                }
            }
        }

        Series series = new Series();
        series.setBrandId(brandId);
        series.setSeriesName(seriesName);
        boolean resultCount = save(series);
        if (resultCount) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    @Override
    public ServerResponse delSeries(Integer seriesId) {
        Series series = new Series();
        series.setSeriesId(seriesId);
        series.setStatus(Const.Number.ZERO);
        boolean resultCount = updateById(series);
        if (resultCount) {

            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }
}
