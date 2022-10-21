package com.shy.bs.vo;

import lombok.Data;

/**
 * @author night
 * @date 2022/10/22 11:02
 */
@Data
public class DetailsQuery {
    private int page = 1;

    private int limit = 5;

    private String id;

    private Long orderId;

    private Long customerId;

    private Long carId;

    private String employeeName;

    private String status;

    private String orderBy;
}
