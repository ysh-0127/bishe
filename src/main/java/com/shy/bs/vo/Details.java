package com.shy.bs.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author night
 * @date 2022/10/22 11:02
 */
@Data
public class Details {
    private String id;

    private Long carId;

    private Integer brandId;

    private Integer seriesId;

    private String brandName;

    private String seriesName;

    private String type;

    private String color;

    private BigDecimal salePrice;

    private Integer carNumber;
}
