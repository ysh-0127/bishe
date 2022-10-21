package com.shy.bs.vo;

import lombok.*;

/**
 * 接收库存的getList()请求
 * @author night
 * @date 2022/10/22 11:04
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreQuery {
    private Long id;

    private Integer brandId;

    private Integer seriesId;

    private String status;

    private int page = 1;

    private int limit = 5;

    private String orderBy;
}
