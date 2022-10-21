package com.shy.bs.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName order_details
 */
@Data
public class OrderDetails implements Serializable {
    private String id;

    private Long orderId;

    private Long carId;

    private Integer carNumber;

    private static final long serialVersionUID = 1L;
}