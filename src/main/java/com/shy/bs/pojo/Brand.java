package com.shy.bs.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName brand
 */
@Data
public class Brand implements Serializable {
    private Integer brandId;

    private String brandName;

    private String status;

    private static final long serialVersionUID = 1L;
}