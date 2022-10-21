package com.shy.bs.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName order
 */
@Data
public class Order implements Serializable {
    private Long id;

    private Long customerId;

    private Integer employeeId;

    private BigDecimal totalPrice;

    private String status;

    private Date createTime;

    private Date payTime;

    private Date updateTime;

    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}