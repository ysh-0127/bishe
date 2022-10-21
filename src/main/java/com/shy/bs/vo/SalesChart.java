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

    private BigDecimal income;

    private BigDecimal expenditure;

    private BigDecimal profit;
}
