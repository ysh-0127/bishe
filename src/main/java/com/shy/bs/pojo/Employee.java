package com.shy.bs.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName employee
 */
@Data
public class Employee implements Serializable {
    private Integer id;

    private String role;

    private String name;

    private String password;

    private String idCard;

    private String phone;

    private String gender;

    private BigDecimal salary;

    private Date entryTime;

    private String status;

    private static final long serialVersionUID = 1L;
}