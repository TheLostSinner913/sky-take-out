package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 17:54
 */
@Mapper
public interface WorkSpaceMapper {
    Double businessData(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
    @Select("select count(*) from orders where status=5 and order_time between #{begin} and #{end}")
    Integer orderCount(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    @Select("select count(*) from orders where order_time between #{begin} and #{end}")
    Integer totalOrderCount(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
    @Select("select count(*) from user where create_time between #{begin} and #{end}")
    Integer newUsers(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
    @Select("select count(*) from setmeal where status=1")
    Integer openSetMeals();
    @Select("select count(*) from setmeal where status=0")
    Integer closeSetMeals();
    @Select("select count(*) from dish where status=1")
    Integer openDishes();
    @Select("select count(*) from dish where status=0")
    Integer closeDishes();

    @Select("select count(*) from orders where order_time between #{begin} and #{end}")
    Integer allOrders(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    @Select("select count(*) from orders where status=2 and order_time between #{begin} and #{end}")
    Integer waitingOrders(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    @Select("select count(*) from orders where status=4 and order_time between #{begin} and #{end}")
    Integer deliveredOrders(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    @Select("select count(*) from orders where status=5 and order_time between #{begin} and #{end}")
    Integer completedOrders(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    @Select("select count(*) from orders where status=6 and order_time between #{begin} and #{end}")
    Integer cancelledOrders(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
}
