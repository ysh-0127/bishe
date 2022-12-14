package com.shy.bs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shy.bs.pojo.OrderDetails;
import com.shy.bs.vo.*;

import java.util.List;

/**
 * @author night
 * @description 针对表【order_details】的数据库操作Mapper
 * @createDate 2022-10-21 19:22:27
 * @Entity com.shy.bs.pojo.OrderDetails
 */
public interface OrderDetailsMapper extends BaseMapper<OrderDetails> {

    int selectYesterdayNum();

    List<SalesChart> selectSalesChart(String start, String end);

    IndexSales selectIndexSales(Integer id);

    List<EmpChart> selectEmpChart(Integer id, String date);

    List<Details> selectDetailsByOrderId(Long orderId);

    List<DetailsList> selectSelective(DetailsQuery detailsQuery);

}




