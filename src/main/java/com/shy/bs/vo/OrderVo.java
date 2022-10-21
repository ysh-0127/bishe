package com.shy.bs.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author night
 * @date 2022/10/22 11:04
 */
@Data
public class OrderVo {
    private Long customerId;

    private Integer employeeId;

    private String status;

    private BigDecimal totalPrice;

    private List<OrderDetailVo> detailVos;
}
