package com.shy.bs.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

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

    private Date createTime;

    @TableLogic//逻辑删除
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}