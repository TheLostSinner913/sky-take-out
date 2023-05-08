package com.sky;

import com.sky.mapper.DishMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 测试类
 * @Author: 刘东钦
 * @Date: 2023/4/25 19:53
 */
@SpringBootTest
public class test {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test() {
        //redis操作字符串类型
        ValueOperations valueOperations = redisTemplate.opsForValue();
        redisTemplate.opsForValue().set("name", "liudongqin");
        String name = (String) redisTemplate.opsForValue().get("name");
        //如果重复则返回false
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("name", "dapang");
        //设置数据存在时间为10秒
        redisTemplate.opsForValue().set("city","天津",10L, TimeUnit.SECONDS);
        System.out.println(name);
    }
    @Test
    public void hash(){
        //存值
        redisTemplate.opsForHash().put("001","name","大胖");
        redisTemplate.opsForHash().put("001","age","18");
        redisTemplate.opsForHash().put("001","gender","女");
        //取值
        Object name = redisTemplate.opsForHash().get("001", "name");
        //获取所有001的 所有字段
        Set keys = redisTemplate.opsForHash().keys("001");
        //获取所有001的 所有值
        List values = redisTemplate.opsForHash().values("001");
        //删除
        redisTemplate.opsForHash().delete("100","name");
        System.out.println(values);
        System.out.println(name);
    }
    @Test
    public void list(){
        ListOperations listOperations = redisTemplate.opsForList();
        //存值
        listOperations.leftPush("list","a");
        listOperations.leftPush("list","b");
        listOperations.leftPush("list","c");
        //一次性存储多个值
        listOperations.leftPushAll("list","1","2","3","4");
        //取值
        List list1 = listOperations.range("list", 0, -1);
        System.out.println(list1);
        //从末尾去除值
        listOperations.rightPop("list");
    }
    @Test
    public void set(){
        SetOperations setOperations = redisTemplate.opsForSet();
        //存值
        setOperations.add("list",1,2,3,4,5);
        //取值
        Set list = setOperations.members("list");
        for (Object o : list) {
            System.out.println(o);
        }
        //获取长度
        Long size = setOperations.size("list");
        //删除
        setOperations.remove("list",1,2,3,4);
    }
    @Test
    public void HttpClient() throws IOException {
        //创建HttpClinet的对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        //获取响应结果
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //获取服务器返回的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("服务器返回的状态码为:"+statusCode);
        //获取响应的实体对象
        HttpEntity entity = response.getEntity();
        //将实体对象转换为字符串形式打印输出
        String entityStr = EntityUtils.toString(entity);
        System.out.println("服务器响应的数据为:"+entityStr);
        //关闭资源
        response.close();
        httpClient.close();
    }
    @Test
    public void HttpClientEntity() throws IOException, JSONException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/admin/employee/login");
        httpClient.execute(httpGet);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password","123456");

    }
    @Test
    public void  doGet() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("www.baidu.com");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String jsonResult = EntityUtils.toString(entity);
        System.out.println(statusCode);
        System.out.println(jsonResult);
    }
    @Test
    public void doPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password","123456");
        String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(jsonObject);
        StringEntity stringEntity = new StringEntity(jsonString);
        stringEntity.setContentType("apllication/json");
        stringEntity.setContentEncoding("utf-8");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
    }
    @Test
    public void mapTest() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String Url="https://api.map.baidu.com/reverse_geocoding/v3/?";
        HttpGet httpGet = new HttpGet("http://localhost:8080/admin/employee/login");
        CloseableHttpResponse response = httpClient.execute(httpGet);
    }
    @Test
    public void writeExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("LDQ");
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(1).setCellValue("姓名");
        row0.createCell(2).setCellValue("年龄");
        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(1).setCellValue("陶大胖");
        row1.createCell(2).setCellValue("18");
        FileOutputStream fos=new FileOutputStream(new File("src/main/resources/static"));
        workbook.write(fos);
        fos.flush();
        fos.close();
        workbook.close();
    }
}
