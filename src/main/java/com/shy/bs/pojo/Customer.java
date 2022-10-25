package com.shy.bs.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName customer
 */
@Data
public class Customer implements Serializable {
    private Long id;

    private String name;

    private String phone;

    private String idCard;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;



    private static final long serialVersionUID = 1L;
}