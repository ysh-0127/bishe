package com.shy.bs.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName customer
 */
@Data
public class Customer implements Serializable {
    private Long id;

    private String name;

    private String phone;

    private String idCard;

    private Date createTime;

    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}