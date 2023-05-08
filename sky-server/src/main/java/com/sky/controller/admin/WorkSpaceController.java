package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 17:53
 */
@RestController
@Api(tags = "工作台")
@Slf4j
@RequestMapping("/admin/workspace")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 今日运营数据
     *
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation(value = "今日运营数据")
    public Result businessData() {
        //获取当天起点时间
        LocalDateTime begin = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        //获取当天终点时间
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        log.info("今日运营数据");
        BusinessDataVO businessDataVO = workSpaceService.businessData(begin,end);
        return Result.success(businessDataVO);
    }

    /**
     * 套餐概览
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,18:55
     **/
    @GetMapping("/overviewSetmeals")
    @ApiOperation(value = "套餐概览")
    public Result overviewSetmeals() {
        DishOverViewVO dishOverViewVO = workSpaceService.setMeals();
        log.info("套餐概览:起售{},停售{}", dishOverViewVO.getSold(), dishOverViewVO.getDiscontinued());
        return Result.success(dishOverViewVO);
    }

    /**
     * 菜品概览
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,19:14
     **/
    @GetMapping("/overviewDishes")
    @ApiOperation(value = "菜品概览")
    public Result overviewDishes() {
        DishOverViewVO dishOverViewVO = workSpaceService.dishes();
        log.info("菜品概览:起售{},停售{}", dishOverViewVO.getSold(), dishOverViewVO.getDiscontinued());
        return Result.success(dishOverViewVO);
    }

    /**
     * 订单概览
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,20:43
     **/
    @GetMapping("/overviewOrders")
    @ApiOperation(value = "订单概览")
    public Result overviewOrders() {
        log.info("订单概览");
        OrderOverViewVO orderOverViewVO = workSpaceService.overviewOrders();
        return Result.success(orderOverViewVO);
    }
}
