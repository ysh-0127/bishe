package com.shy.bs.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

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

    private Date createTime;

    private String status;

    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}