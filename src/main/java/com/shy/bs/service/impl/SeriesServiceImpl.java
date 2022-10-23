package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.BrandMapper;
import com.shy.bs.mapper.SeriesMapper;
import com.shy.bs.pojo.Brand;
import com.shy.bs.pojo.Series;
import com.shy.bs.service.SeriesService;
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
    private BrandMapper brandMapper;

    @Override
    public ServerResponse seriesOpt() {
        QueryWrapper<Brand> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("status", 1);
        List<Brand> brandList = brandMapper.selectList(queryWrapper1);

        if (!CollectionUtils.isEmpty(brandList)) {
            List<SeriesTree> treeList = new ArrayList<>();
            for (Brand brandItem : brandList) {
                SeriesTree tree = new SeriesTree();
                tree.setValue(brandItem.getBrandId());
                tree.setLabel(brandItem.getBrandName());
                QueryWrapper<Series> queryWrapper2 = new QueryWrapper();
                queryWrapper2
                        .eq("status", 1)
                        .eq("brand_id", brandItem.getBrandId());
                List<Series> seriesList = baseMapper.selectList(queryWrapper2);
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
}
