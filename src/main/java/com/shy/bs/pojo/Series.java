package com.shy.bs.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @TableName series
 */
@Data
public class Series implements Serializable {
    private Integer seriesId;

    private Integer brandId;

    private String seriesName;

    private String status;


    private static final long serialVersionUID = 1L;
}