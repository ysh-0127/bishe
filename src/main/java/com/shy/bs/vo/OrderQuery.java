package com.shy.bs.vo;

import lombok.Data;

/**
 * @author night
 * @date 2022/10/22 11:03
 */
@Data
public class OrderQuery {
    private int page = 1;

    private int limit = 5;

    private Long orderId;

    private String customerName;

    private String employeeName;

    private String status;

    private String orderBy;
}
