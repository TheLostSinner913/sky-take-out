package com.sky.mapper;

import com.sky.aop.AutoUpdateTimeAndUser;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    //修改员工信息
    @AutoUpdateTimeAndUser(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);
    //新增员工
    @AutoUpdateTimeAndUser(OperationType.INSERT)
    boolean add(Employee employee);
    //分页展示
    List<Employee> show(@Param("name") String name);

    /**
     * 根据id查询
     * @param id
     * @return com.sky.entity.Employee
     * @author 刘东钦
     * @create 2023/4/25,11:37
     **/
    Employee check(@Param("id") Long id);


}
