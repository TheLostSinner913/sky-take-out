package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.template.AliOSSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description: 文件上传
 * @Author: 刘东钦
 * @Date: 2023/4/25 11:50
 */
@RestController
@RequestMapping("/admin/common/upload")
@Api(tags = "通用接口")
@Slf4j
public class UploadController {

    @Autowired
    private AliOSSUtils aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,15:57
     **/
    @PostMapping
    @ApiOperation(value = "文件上传")
    public Result upload(MultipartFile file) throws IOException {
        try {
            String url = aliOssUtil.upload(file);
            return Result.success(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.error("上传失败");
    }
}
