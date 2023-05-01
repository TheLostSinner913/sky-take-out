package com.sky.mapper;

import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper {
    @Select("select * from user where openid=#{openId} ")
    User getByOpenId(String openId);
    @Insert("insert into user(openid,create_time)values (#{openid},#{createTime})")
    Integer add(User user);
    @Select("select * from category where type=#{type} ")
    List<Category> show(Integer type);
    @Select("select * from dish d ,category c where d.category_id=#{categoryId} ")
    List<DishVO> getById(Long categoryId);
    @Select("select * from category where id=#{categoryId} ")
    List<Category> getId(Long categoryId);
}
