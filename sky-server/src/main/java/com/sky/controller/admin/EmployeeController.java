package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增用户
     *
     * @param employeeDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,16:07
     **/
    @PostMapping
    @ApiOperation("新增员工")
    public Result add(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工{}", employeeDTO);
        Result add = employeeService.add(employeeDTO);
        return add;
    }

    /**
     * 分页展示员工信息
     *
     * @param page
     * @param pageSize
     * @param name
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,18:46
     **/
    @GetMapping("/page")
    @ApiOperation("员工信息分页展示")
    public Result update(@RequestParam(defaultValue = "1") Integer page
            , @RequestParam(defaultValue = "10") Integer pageSize, String name) {
        Result show = employeeService.show(page, pageSize, name);
        log.info("分页查询展示员工信息{}", show.getData());
        return show;
    }

    /**
     * 员工状态设置
     *
     * @param status
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,19:40
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("员工状态设置")
    public Result status(@PathVariable Integer status, Long id) {
        log.info("启用/禁用员工账号{}->{}", id, status);
        Result status1 = employeeService.status(id, status);
        return status1;
    }

    /**
     * 员工修改信息回显
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,19:56
     **/
    @GetMapping("/{id}")
    @ApiOperation("员工修改信息回显")
    public Result check(@PathVariable Long id) {
        log.info("员工信息回显id{}", id);
        Result check = employeeService.check(id);
        return check;
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,20:05
     **/
    @PutMapping
    @ApiOperation("修改员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,8:41
     **/
    @PutMapping("/editPassword")
    @ApiOperation("修改密码")
    public Result resetPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改员工信息{}", passwordEditDTO);
        employeeService.resetPassword(passwordEditDTO);
        return Result.success();
    }
}
