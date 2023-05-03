package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.UserAddressService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/2 20:05
 */
@RestController
@Slf4j
@Api(tags = "用户地址簿")
@RequestMapping("/user/addressBook")
public class UserAddressController {
    @Autowired
    private UserAddressService addressService;

    /**
     * 添加用户地址簿
     *
     * @param addressBook
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,20:34
     **/
    @PostMapping
    public Result addAddress(@RequestBody AddressBook addressBook) {
        log.info("新增地址{}", addressBook);
        Result add = addressService.add(addressBook);
        return Result.success();
    }

    /**
     * 获取用户所有地址
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,20:19
     **/
    @GetMapping("/list")
    public Result allAddress() {
        Result all = addressService.allAddress();
        return all;
    }

    /**
     * 查询用户默认地址
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,21:55
     **/
    @GetMapping("/default")
    public Result defaultAddress() {
        Result defaultAddress = addressService.defaultAddress();
        return defaultAddress;
    }

    /**
     * 修改用户地址
     *
     * @param addressBook
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:09
     **/
    @PutMapping
    public Result updateAddress(@RequestBody AddressBook addressBook) {
        Result update = addressService.update(addressBook);
        return update;
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:10
     **/
    @GetMapping("/{id}")
    public Result getAddress(@PathVariable("id") Long id) {
        Result address = addressService.getAddress(id);
        return address;
    }

    /**
     * 根据ID删除地址
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:19
     **/
    @DeleteMapping
    public Result deleteAddress(Long id) {
        Result delete = addressService.delete(id);
        return delete;
    }
    @PutMapping("/default")
    public Result updateDefault(@RequestBody AddressBook addressBook){
        log.info("修改默认地址{}",addressBook);
        Result result = addressService.updateDefault(addressBook);
        return result;
    }
}