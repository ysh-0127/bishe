package com.shy.bs.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author night
 * @date 2022/10/22 11:04
 */
@Data
public class SalesChart {
    private String date;
    //收入
    private BigDecimal income;
    //支出
    private BigDecimal expenditure;
    //利润
    private BigDecimal profit;
}
