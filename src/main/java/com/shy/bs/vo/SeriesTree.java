package com.shy.bs.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author night
 * @date 2022/10/22 11:04
 */
@Getter
@Setter
public class SeriesTree {
    private Integer value;
    private String label;
    private List<SeriesTree> children;
}
