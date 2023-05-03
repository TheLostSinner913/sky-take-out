package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/2 20:08
 */
@Mapper
public interface UserAddressMapper {
    @Select("select * from address_book where user_id=#{userId} ")
    List<AddressBook> getAll(Long userId);

    void add(AddressBook addressBook);

    AddressBook getDefault(@Param("userId") Long userId);

    void update(AddressBook addressBook);

    @Select("select * from address_book where id=#{id}  and user_id=#{userId}")
    AddressBook getAddress(@Param("id") Long id, @Param("userId") Long userId);

    @Delete("delete from address_book where id=#{id}  and user_id=#{userId}")
    void delete(@Param("id") Long id, @Param("userId") Long userId);

    void updateAll(@Param("userId") Long userId);
}
