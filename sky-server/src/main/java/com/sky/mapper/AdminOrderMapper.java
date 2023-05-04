package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 21:44
 */
@Mapper
public interface AdminOrderMapper {
    // 条件分页查询
    List<OrderVO> conditionSearch(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("number") Integer number, @Param("phone") String phone, @Param("status") Integer status);
    //根据orderId查询订单详情
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> orderDetail(Long orderId);
    //取消订单
    void cancel(@Param("orderId") Long orderId, @Param("cancelReason") String cancelReason, @Param("cancelTime") LocalDateTime cancelTime);
    //统计待接单数量
    @Select("select count(*) from orders where status = 2")
    Integer toBeConfirmed();
    //统计已接单数量
    @Select("select count(*) from orders where status = 3")
    Integer confirmed();
    //统计派送中数量
    @Select("select count(*) from orders where status = 4")
    Integer deliveryInProgress();
}
