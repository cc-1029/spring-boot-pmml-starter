package com.cc.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caichengjie
 * @date 2022/3/29
 */
@Data
@Builder
public class CouponRawData implements Serializable {
    private static final long serialVersionUID = -1350351779743361617L;
    private Double faceValue;
    private Double limitValue;
    private Integer type;
    private Double redPriceAvg;
    private Double redPrice50;
}
