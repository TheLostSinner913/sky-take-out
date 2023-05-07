package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 12:17
 */
@Mapper
public interface ReportMapper {


    Double amount(@Param("begin") LocalDate begin);

    Integer newUser(@Param("begin") LocalDate begin);

    Integer totalUser(@Param("begin") LocalDate begin);

    Integer orderCount(@Param("begin") LocalDate begin);


    Integer doneOrderDaily(@Param("begin") LocalDate begin);

    Integer doneOrderTotal(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    Integer orderTotal(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    List<GoodsSalesDTO> top10(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
}
