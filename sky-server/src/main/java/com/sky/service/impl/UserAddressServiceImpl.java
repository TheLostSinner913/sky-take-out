package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.UserAddressMapper;
import com.sky.result.Result;
import com.sky.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/2 20:10
 */
@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    /**
     * 获取用户所有地址
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,20:19
     **/
    @Override
    public Result allAddress() {
        Long userId = BaseContext.getCurrentId();
        List<AddressBook> all = userAddressMapper.getAll(userId);
        return Result.success(all);
    }

    /**
     * 添加用户地址簿
     *
     * @param addressBook
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,20:34
     **/
    @Override
    public Result add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        userAddressMapper.add(addressBook);
        return Result.success();
    }

    /**
     * 查询用户默认地址
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,21:55
     **/
    @Override
    public Result defaultAddress() {
        Long userId = BaseContext.getCurrentId();
        AddressBook aDefault = userAddressMapper.getDefault(userId);
        return Result.success(aDefault);
    }

    /**
     * 修改用户地址
     *
     * @param addressBook
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:09
     **/
    @Override
    public Result update(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        userAddressMapper.update(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:10
     **/
    @Override
    public Result getAddress(Long id) {
        Long userId = BaseContext.getCurrentId();
        AddressBook address = userAddressMapper.getAddress(id, userId);
        return Result.success(address);
    }

    /**
     * 根据ID删除地址
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:19
     **/
    @Override
    public Result delete(Long id) {
        Long userId = BaseContext.getCurrentId();
        userAddressMapper.delete(id, userId);
        return Result.success();
    }

    /**
     * 修改默认地址
     * @param addressBook
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:20
     **/
    @Override
    public Result updateDefault(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        //将当前用户所有地址改为非默认地址(0)
        userAddressMapper.updateAll(userId);
        //将当前地址修改为默认地址(1)
        addressBook.setIsDefault(1);
        userAddressMapper.update(addressBook);
        return Result.success();
    }
}
