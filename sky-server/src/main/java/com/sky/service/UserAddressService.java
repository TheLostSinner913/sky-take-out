package com.sky.service;

import com.sky.entity.AddressBook;
import com.sky.result.Result;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/2 20:09
 */
public interface UserAddressService {
    Result allAddress();

    Result add(AddressBook addressBook);

    Result defaultAddress();

    Result update(AddressBook addressBook);

    Result getAddress(Long id);

    Result delete(Long id);

    Result updateDefault(AddressBook addressBook);
}
