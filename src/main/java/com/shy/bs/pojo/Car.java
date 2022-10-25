package com.shy.bs.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName car
 */
@Data
public class Car implements Serializable {
    private Long id;

    private Integer seriesId;

    private String type;

    private String color;

    private BigDecimal price;

    private BigDecimal salePrice;

    private Integer repertory;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    private String status;


    private static final long serialVersionUID = 1L;
}