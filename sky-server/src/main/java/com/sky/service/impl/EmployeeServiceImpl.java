package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.*;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!md5.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,17:17
     **/
    @Override
    public Result add(EmployeeDTO employeeDTO) {
        //从ThreadLocal中获取Interceptor中赋的ID值
        Employee employee = Employee.builder()
                .name(employeeDTO.getName())
                .username(employeeDTO.getUsername())
                .sex(employeeDTO.getSex())
                .idNumber(employeeDTO.getIdNumber())
                .phone(employeeDTO.getPhone())
                .password(DigestUtils.md5DigestAsHex((PasswordConstant.DEFAULT_PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .build();
        boolean add = employeeMapper.add(employee);
        if (!add) {
            throw new BaseException("注册失败");
        }
        return Result.success("添加成功");
    }

    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,18:54
     **/
    @Override
    public Result show(Integer page, Integer pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        List<Employee> show = employeeMapper.show(name);
        Page<Employee> p = (Page<Employee>) show;
        PageResult pageResult = new PageResult(p.getTotal(), p.getResult());
        return Result.success(pageResult);
    }


    /**
     * 员工修改信息回显
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,19:56
     **/
    @Override
    public Result check(Long id) {
        Employee check = employeeMapper.check(id);
        if (check == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success(check);
    }

    /**
     * 禁用/启用状态修改
     *
     * @param id
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,21:32
     **/
    @Override
    public Result status(Long id, Integer status) {
        Employee employee = Employee.builder().id(id).status(status).build();
        employeeMapper.update(employee);
        return null;
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,20:05
     **/
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Long currentId = BaseContext.getCurrentId();
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
       /* employee.setUpdateUser(currentId);
        employee.setUpdateTime(LocalDateTime.now());*/
        employeeMapper.update(employee);
        return;
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,8:41
     **/
    @Override
    public Result resetPassword(PasswordEditDTO passwordEditDTO) {
        //ThreadLocal 获取登陆者ID信息
        Long currentId = BaseContext.getCurrentId();
        Employee employee=new Employee();
        //获取前端的老密码,新密码信息
        String oldPassword = passwordEditDTO.getOldPassword();
        String newPassword = passwordEditDTO.getNewPassword();
        //将老密码md5加密 后与数据库内容进行对比
        String md5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes(StandardCharsets.UTF_8));
        Employee check = employeeMapper.check(currentId);
        //如果不一致则抛异常
        if (!check.getPassword().equals(md5)){
            throw new PasswordEditFailedException("原始密码错误");
        }
        //创建employee对象 并对其赋值
        employee.setId(currentId);
        employee.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8)));
        employee.setUpdateUser(currentId);
        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.update(employee);
        return Result.success();
    }

}
