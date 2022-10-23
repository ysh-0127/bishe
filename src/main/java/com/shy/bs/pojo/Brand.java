package com.shy.bs.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName brand
 */
@Data
public class Brand implements Serializable {
    private Integer brandId;

    private String brandName;

    private Integer status;

    @TableLogic//逻辑删除
    private Integer isDelete;
    private static final long serialVersionUID = 1L;
}