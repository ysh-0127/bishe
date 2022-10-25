package com.shy.bs.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName order
 */
@TableName("`order`")
@Data
public class Order implements Serializable {
    @TableId
    private Long id;

    private Long customerId;

    private Integer employeeId;

    private BigDecimal totalPrice;

    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}