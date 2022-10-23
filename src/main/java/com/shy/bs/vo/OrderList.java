package com.shy.bs.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author night
 * @date 2022/10/22 11:03
 */
@Data
@Accessors(chain = true)
public class OrderList {
    //    orderId
    private Long orderId;

    private Long customerId;
    //11
    private String customerName;
    //11
    private String customerPhone;
    //11
    private String customerIdCard;
    //11
    private String employeeName;

    private BigDecimal totalPrice;

    private String status;

    private Date createTime;

    private Date payTime;

    private Date updateTime;

    private List<Details> details;
}
