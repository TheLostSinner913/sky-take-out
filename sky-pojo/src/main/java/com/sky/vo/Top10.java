package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 15:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Top10 implements Serializable {
    private String dishName;
    private Integer salesCount;
}
