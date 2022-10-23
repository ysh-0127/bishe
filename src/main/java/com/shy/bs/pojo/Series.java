package com.shy.bs.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
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

    @TableLogic//逻辑删除
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}