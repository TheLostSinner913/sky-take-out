package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    //完成订单
    @Update("update orders set status = 5 where id = #{id}")
    Integer complete(Long id);
    //根据id查询订单信息
    @Select("select * from orders where id = #{id}")
    OrderVO msg(Long id);

    //确认订单(接单)
    @Update("update orders set status =3  where id = #{id} and status = 2")
    Integer confirm(@Param("id") Long id, @Param("status") Integer status);

    //拒绝订单
    Integer reject(@Param("rejectionReason") String rejectionReason, @Param("id") Long id, @Param("cancelTime") LocalDateTime cancelTime);

    //发货
    Integer deliver(@Param("id") Long id);

    //定时查询超时订单
    @Select("select * from orders where order_time < #{time}  and status = #{status}")
    List<Orders> checkOverTime(@Param("time") LocalDateTime time, @Param("status") Integer status);
    //定时取消超时订单
    void update(Orders order);

    //练习
    List<Orders> againOverTime(@Param("time") LocalDateTime time);
}
