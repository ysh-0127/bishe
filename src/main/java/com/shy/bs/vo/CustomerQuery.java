package com.shy.bs.vo;

import lombok.Data;

/**
 * @author night
 * @date 2022/10/22 11:02
 */
@Data
public class CustomerQuery {
    private int page = 1;

    private int limit = 5;

    private Long id;

    private String name;

    private String phone;

    private String idCard;

    private String orderBy;
}
